package com.yands.streaming.util

import java.text.SimpleDateFormat
import java.util.{ Calendar, Date, Locale }

import scala.collection.mutable.ArrayBuffer

object DateUtil {

  private val YYYY_MM_DD = "yyyy-MM-dd"

  private val YYYYMMDD = new SimpleDateFormat("yyyyMMdd")

  private val HH = new SimpleDateFormat("HH")
  
  private val MMddHHmm = new SimpleDateFormat("MMddHHmm")
  
  private val MMddHH = new SimpleDateFormat("MMddHH")

  private val YYYYMM = "yyyyMM"

  private val YYYY = "yyyy"
  // 长日期格式
  private val TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

  private val TIME_FORMAT2 = "MMdd"

  /**
   * 获取当天的日期
   * @return
   */
  def getCurrentDate() = {
    new SimpleDateFormat(YYYY_MM_DD).format(new Date)
  }

  /**
   * 当前时间前N天的时间集合
   */
  def preDayFromNow(n: Int): Array[String] = {
    var array = ArrayBuffer[String]()
    val now = new Date

    val nowStr = YYYYMMDD.format(now)

    for (i <- 0 until n) {
      val calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.DAY_OF_MONTH, -i);
      val temp = YYYYMMDD.format(calendar.getTime)
      array += (temp)
    }
    array.toArray
  }

  /**
   * 当前时间前N月的时间集合
   */
  def preMonthFromNow(n: Int): Array[String] = {
    var array = ArrayBuffer[String]()
    val now = new Date

    val formate = new SimpleDateFormat(YYYYMM)

    val nowStr = formate.format(now)

    for (i <- 0 until n) {
      val calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.MONTH, -i);
      val temp = formate.format(calendar.getTime)
      array += (temp)
    }
    array.toArray
  }

  /**
   * 当前时间前N月的时间集合
   */
  def preYearFromNow(n: Int): Array[String] = {
    var array = ArrayBuffer[String]()
    val now = new Date

    val formate = new SimpleDateFormat(YYYY)

    val nowStr = formate.format(now)

    for (i <- 0 until n) {
      val calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.YEAR, -i);
      val temp = formate.format(calendar.getTime)
      array += (temp)
    }
    array.toArray
  }

  def convert2long(date: String) = {
    val loc = new Locale("en")
    val fm = new SimpleDateFormat(TIME_FORMAT, loc)
    val dt2 = fm.parse(date)
    dt2.getTime()
  }

  def convertLong2String(time: Long) = {
    val sf = new SimpleDateFormat(TIME_FORMAT)
    val date = new Date(time)
    sf.format(date);
  }

  def convertLong2StringMMdd(time: Long) = {
    val sf = new SimpleDateFormat(TIME_FORMAT2)
    val date = new Date(time)
    sf.format(date);
  }

  def convertDate2StringMMddHH(date: Date) = {
    MMddHH.format(date);
  }

  def convertDate2StringMMddHHmm(date: Date) = {
    MMddHHmm.format(date);
  }
  
  def long2StringYMD(time: Long) = {
    YYYYMMDD.format(new Date(time))
  }
  
  def long2StringH(time: Long) = {
    HH.format(new Date(time))
  }

  def convertLong2StringFormat(time: Long, format: String) = {
    val sf = new SimpleDateFormat(format)
    val date = new Date(time * 1000)
    sf.format(date);
  }
}