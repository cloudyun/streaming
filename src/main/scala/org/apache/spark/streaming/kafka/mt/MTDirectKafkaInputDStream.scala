package org.apache.spark.streaming.kafka.mt
 
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.reflect.ClassTag
import scala.util.control.Breaks.{break, breakable}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.streaming.{StreamingContext, Time}
import org.apache.spark.streaming.kafka.{DirectKafkaInputDStream, KafkaRDD}
import kafka.common.{ErrorMapping, TopicAndPartition}
import kafka.javaapi.{TopicMetadata, TopicMetadataRequest}
import kafka.javaapi.consumer.SimpleConsumer
import kafka.message.MessageAndMetadata
import kafka.serializer.Decoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import com.yands.streaming.util.LoadConfig
 

/**  
 * @Title:  MTDirectKafkaInputDStream.scala   
 * @Package org.apache.spark.streaming.kafka.mt   
 * @Description:    (SparkStreaming自适应上游kafka topic partition数目变化)   
 * @author: gaoyun     
 * @edit by: 
 * @date:   2018年7月9日 上午10:58:43   
 * @version V1.0 
 */ 
class MTDirectKafkaInputDStream[
  K: ClassTag,
  V: ClassTag,
  U <: Decoder[K]: ClassTag,
  T <: Decoder[V]: ClassTag,
  R: ClassTag](
    @transient ssc_ : StreamingContext,
    val MTkafkaParams: Map[String, String],
    val MTfromOffsets: Map[TopicAndPartition, Long],
    messageHandler: MessageAndMetadata[K, V] => R, 
    zk_offset_record_dir: String
) extends DirectKafkaInputDStream[K, V, U, T, R](ssc_, MTkafkaParams , MTfromOffsets, messageHandler) {
  
    @transient val config = LoadConfig.getInstance("sparkStreaming")
  
    private val kafkaBrokerList : String = config.get("brokers") //根据自己的情况自行修改
    
    private val zkHost : String = config.get("zkHost") //根据自己的情况自行修改
    
    @transient val _CaptureTopicDirs = new ZKGroupTopicDirs(zk_offset_record_dir, config.get("topic"))
 
    override def compute(validTime: Time) : Option[KafkaRDD[K, V, U, T, R]] = {
      /**
        * 在这更新 currentOffsets 从而做到自适应上游 partition 数目变化
        */
        updateCurrentOffsetForKafkaPartitionChange()
        super.compute(validTime)
    }
 
    private def updateCurrentOffsetForKafkaPartitionChange() : Unit = {
      val topic = currentOffsets.head._1.topic
      val nextPartitions : Int = getTopicMeta(topic) match {
          case Some(x) => x.partitionsMetadata.size()
          case _ => 0
      }
      val currPartitions = currentOffsets.keySet.size
 
      if (nextPartitions > currPartitions) {
        var i = currPartitions
        while (i < nextPartitions) {
           currentOffsets = currentOffsets + (TopicAndPartition(topic, i) -> 0)
           val zkClient = new ZkClient(zkHost)
           val zkPath = s"${_CaptureTopicDirs.consumerOffsetDir}/${i}"
           ZkUtils.createPersistentPath(zkClient, zkPath, currentOffsets.toString)
           i = i + 1
        }
      }
      logInfo(s"######### ${nextPartitions}  currentParttions ${currentOffsets.keySet.size} ########")
    }
 
    private def getTopicMeta(topic: String) : Option[TopicMetadata] = {
        var metaData : Option[TopicMetadata] = None
        var consumer : Option[SimpleConsumer] = None
 
        val topics = List[String](topic)
        val brokerList = kafkaBrokerList.split(",")
        brokerList.foreach(
          item => {
            val hostPort = item.split(":")
            try {
              breakable {
                  for (i <- 0 to 3) {
                      consumer = Some(new SimpleConsumer(host = hostPort(0), port = hostPort(1).toInt,
                                            soTimeout = 10000, bufferSize = 64 * 1024, clientId = "leaderLookup"))
                      val req : TopicMetadataRequest = new TopicMetadataRequest(topics.asJava)
                      val resp = consumer.get.send(req)
 
                      metaData = Some(resp.topicsMetadata.get(0))
                      if (metaData.get.errorCode == ErrorMapping.NoError) break()
                  }
              }
            } catch {
              case e: Exception => logInfo(s" ###### Error in MTDirectKafkaInputDStream ${e} ######")
            }
          }
        )
        metaData
    }
}
