package com.yands.streaming.statistic

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.HasOffsetRanges
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.kafka.OffsetRange
import org.apache.zookeeper.ZooDefs
import com.yands.streaming.manager.StreamingManager
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.ZKGroupTopicDirs
import kafka.utils.ZkUtils
import scala.collection.JavaConversions

object AnalysisVidDid {

  @transient lazy val logg = Logger.getLogger(this.getClass)

  val dataName = "Vid"

  val sm: StreamingManager = StreamingManager.getInstance(dataName)
  
  val TOPIC_CAPTURE = sm.config.get("topic")

  val GROUP_ID = sm.config.get("viddid.group.id")
  
  val OFFSET_RESET = sm.config.get("auto.offset.reset")

  val topicCapture = Set(TOPIC_CAPTURE)
  
  val zk_offset_record_dir = "VID_TOPIC_DIR"

  val _CaptureTopicDirs = new ZKGroupTopicDirs(zk_offset_record_dir, TOPIC_CAPTURE) //创建一个 ZKGroupTopicDirs 对象，对保存

  val _CaptureChildren = sm.zkClient.countChildren(s"${_CaptureTopicDirs.consumerOffsetDir}") //查询该路径下是否字节点（默认有字节点为我们自己保存不同 partition 时生成的）

  var _capturefromOffsets: Map[TopicAndPartition, Long] = Map() //如果 zookeeper 中有保存 offset，我们会利用这个 offset 作为 kafkaStream 的起始位置

  var _captureKafkaStream: InputDStream[(String, String)] = null

  val _captureKafkaParams = Map[String, String]("metadata.broker.list" -> sm.brokers, "auto.offset.reset" -> OFFSET_RESET, "group.id" -> GROUP_ID, "zookeeper.connect" -> sm.zkHost)

  val checkpoint = "/spark/checkpoint/" + dataName

  sm.ssc.checkpoint(checkpoint)

  var _macoffsetRanges = Array[OffsetRange]()

  def main(args: Array[String]): Unit = {
    logg.setLevel(Level.WARN)
    Logger.getRootLogger.setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.kafka").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)
    logg.info("Vid与设备ID之间关系统计开始！")
    val _macKafkaStream: InputDStream[(String, String)] = createMacDirectStream()

    //处理抓拍的消息
    logg.info("抓拍消息处理！")
    new VidDidPostgres().analysisVidDid(_macKafkaStream)
    //处理完消息后将offset更新至zookeeper保存
    updateMacOffsetToZookeeper(_macKafkaStream)

    //启动任务
    sm.sscRun()
  }

  def createMacDirectStream(): InputDStream[(String, String)] = {
    if (_CaptureChildren > 0) { //如果保存过 offset，这里更好的做法，还应该和  kafka 上最小的 offset 做对比，不然会报 OutOfRange 的错误
      for (i <- 0 until _CaptureChildren) {
        val partitionOffset = sm.zkClient.readData[String](s"${_CaptureTopicDirs.consumerOffsetDir}/${i}")
        val tp = TopicAndPartition(TOPIC_CAPTURE, i)
        _capturefromOffsets += (tp -> partitionOffset.toLong) //将不同 partition 对应的 offset 增加到 fromOffsets 中
      }
      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message()) //这个会将 kafka 的消息进行 transform，最终 kafak 的数据都会变成 (topic_name, message) 这样的 tuple
      _captureKafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](sm.ssc, _captureKafkaParams, _capturefromOffsets, messageHandler, zk_offset_record_dir)
    } else {
      _captureKafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](sm.ssc, _captureKafkaParams, topicCapture, zk_offset_record_dir) //如果未保存，根据 kafkaParam 的配置使用最新或者最旧的 offset
    }
    _captureKafkaStream
  }

  def updateMacOffsetToZookeeper(stream: InputDStream[(String, String)]): Unit = {
    stream.transform { rdd =>
      _macoffsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges //得到该 rdd 对应 kafka 的消息的 offset
      rdd
    }.map(_._2).foreachRDD(rdd => {
      for (o <- _macoffsetRanges) {
        val zkPath = s"${_CaptureTopicDirs.consumerOffsetDir}/${o.partition}"
//        ZkUtils.apply(sm.zkClient, false).updatePersistentPath(zkPath, o.fromOffset.toString, ZooDefs.Ids.OPEN_ACL_UNSAFE)
        ZkUtils.updatePersistentPath(sm.zkClient, zkPath, o.fromOffset.toString) //将该 partition 的 offset 保存到 zookeeper
      }
    })
  }
}