package com.yands.streaming.manager

import org.I0Itec.zkclient.ZkClient
import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import com.yands.streaming.util.LoadConfig

class StreamingManager private(taskName: String, durationWindow: Long) {

  //获取sparkstreaming
  val sparkConf = new SparkConf().setAppName("vidServer" + taskName)
  
  val config = LoadConfig.getInstance("sparkStreaming")

  sparkConf.set("spark.streaming.unpersist", config.get("spark.streaming.unpersist"))

  //设置一个批次从kafka拉取的数据
  sparkConf.set("spark.streaming.kafka.maxRatePerPartition", config.get("spark.streaming.kafka.maxRatePerPartition"))

//  sparkConf.setMaster("local[3]")

  sparkConf.set("spark.default.parallelism", config.get("spark.default.parallelism"))

  sparkConf.set("spark.serializer","org.apache.spark.serializer.KryoSerializer")

  sparkConf.set("spark.executor.extraJavaOptions","-XX:+UseConcMarkSweepGC")
  
  val duration = if (durationWindow == 0l) config.get("Durations.seconds").toLong else durationWindow

  val ssc = new StreamingContext(sparkConf, Seconds(duration))

  val brokers = config.get("brokers")

  val zkHost = config.get("zkHost")

  val zkClient = new ZkClient(zkHost)

  def sscRun() = {
    //开启
    ssc.start()
    //等待
    ssc.awaitTermination()
  }

}

object StreamingManager {

  private var streamingManager: StreamingManager = null

  def getInstance(taskName: String, durationWindow: Long): StreamingManager = {
    if (streamingManager == null) {
      streamingManager = new StreamingManager(taskName, durationWindow);
    }
    streamingManager
  }

  def getInstance(taskName: String): StreamingManager = {
    getInstance(taskName, 0l)
  }

}