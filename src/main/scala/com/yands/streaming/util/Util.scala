package com.yands.streaming.util

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSON

object Util {

  /**
   * 对JSONArray排序
   * @param arr
   * @return
   */
  def sortArrToStr(arr: JSONArray) : String = {
    if (arr == null) {
      return ""
    }
    return arr.toString.replace("[", "").replace("]", "").split(",")
      .map { x =>
        if (x == null || "".equals(x)) 0L
        else x.toLong
      }.sorted.mkString(",")
  }
  
  /**
   * 对JSONArray排序
   * @param arr
   * @return
   */
  def sortArr(arr: JSONArray) : Array[Long] = {
    if (arr == null) {
      return null
    }
    return arr.toString.replace("[", "").replace("]", "").split(",")
      .map { x =>
        if (x == null || "".equals(x)) 0L
        else x.toLong
      }.sorted
  }

  def main(args: Array[String]): Unit = {
    println("sort : " + sortArrToStr(JSON.parseArray("[2,1,3]")))
    println("sort : " + sortArr(JSON.parseArray("[2,1,3]")))
    println("sort : " + sortArr(null))
  }
}