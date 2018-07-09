package com.yands.streaming.statistic

import java.util.UUID
import scala.collection.JavaConversions
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import org.apache.spark.Logging
import org.apache.spark.streaming.dstream.DStream
import org.apache.commons.lang.StringUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.util.Date
import com.yands.streaming.util.DateUtil
import com.yands.streaming.util.Util
import com.yands.streaming.util.RedisUtil



class VidDidPostgres extends java.io.Serializable with Logging {

  private val DID = "did";

  private val SPILT = "\t"

  def analysisVidDid(stream: DStream[(String, String)]): Unit = {

    try { //按天统计
      logInfo("对vid和设备Id之间的关系进行记录：")
      val lines = stream.flatMap { sourceData =>
        var viddids = new ArrayBuffer[String]
        var sourceJsonData: JSONObject = null
        try {
          sourceJsonData = JSON.parseObject(sourceData._2)
        } catch {
          case t: Exception => logError("json error format! json : " + sourceData._2)
        }
        if (sourceJsonData != null) {
          val cameraId = sourceJsonData.getOrDefault("cameraId", "").toString
          val captureTime = sourceJsonData.getOrDefault("captureTime", "0").toString.toLong
          val orgIds = Util.sortArrToStr(sourceJsonData.getJSONArray("orgIds"))
          var captureFaces: JSONArray = null
          if (sourceJsonData.containsKey("captureFaces") && sourceJsonData.getJSONArray("captureFaces") != null) {
            captureFaces = sourceJsonData.getJSONArray("captureFaces")
          }
          if (captureFaces != null) {
            val mmdd = DateUtil.convertLong2StringMMdd(captureTime)
            for (a <- 0 until captureFaces.size) {
              val vid = captureFaces.getJSONObject(a).getOrDefault("vid", "");
              if (!"".equals(vid)) {
                //logger.info("从kafka接收消息对象,相机ID:" + cameraId + " 虚拟ID:" + vid)
                viddids += (vid + SPILT + cameraId + SPILT + orgIds + SPILT + mmdd)
              } else {
                logInfo("无vid" + captureFaces.getJSONObject(a))
              }
            }
          }
        }
        Some(viddids)
      }.filter(_.nonEmpty).flatMap(data => data).map { data => (data, 1) }.reduceByKey(_ + _)

      lines.foreachRDD(rdd =>
        rdd.foreachPartition {
          iterator =>
            if (iterator.hasNext) {
              //              val a = System.currentTimeMillis()
              val redis = new RedisUtil();
              //              val b = System.currentTimeMillis()
              while (iterator.hasNext) {
                val next = iterator.next
                val key = DID + DateUtil.convertDate2StringMMddHHmm(new Date())
                val num = next._2
                val value = redis.hget(key, next._1)
                if (StringUtils.isEmpty(value)) {
                  redis.lpush(DID, key)
                  redis.hset(key, next._1, num.toString)
                } else {
                  redis.hset(key, next._1, (value.toInt + num).toString)
                }
              }
              //              val c = System.currentTimeMillis()
              redis.close
              //              val d = System.currentTimeMillis()
              //              logError("redis >>  connect : " + (b - a) + "; operate : " + (c - b) + "; close : " + (d - c) + "; total : " + (d - a))
            }
        })
    } catch {
      case t: Exception => logError("错误信息:" + t.getMessage)
    }
  }
}