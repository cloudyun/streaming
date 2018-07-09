package com.yands.streaming;

import com.alibaba.fastjson.JSONObject;
import com.yands.streaming.create.CaptureImgModel;
import com.yands.streaming.kafka.KfkProducer;
import com.yands.streaming.vo.CaptureBody;
import com.yands.streaming.vo.CaptureFace;
import com.yands.streaming.vo.CaptureImg;

/**  
 * @Title:  FaceBodyCreateCaptureImg.java   
 * @Package com.lingda.vid   
 * @Description:    (人脸人体统计测试造数据)   
 * @author: gaoyun     
 * @edit by: 
 * @date:   2018年6月5日 下午5:04:44   
 * @version V1.0 
 */ 
public class FaceBodyCreateCaptureImg {

	public static void main(String[] args) {
		String zkConnect = "cldmng01.lingda.com:2181,cldmng02.lingda.com:2181,cldmng02.lingda.com:2181";
		String brokerlist = "cldmng01.lingda.com:9092,cldmng02.lingda.com:9092,cldmng02.lingda.com:9092";
		String topicName = args[0];
		String date = "2018-06-28";
		CaptureImgModel model = new CaptureImgModel(date, 10, 10, 5);

		int pre_face_count = Integer.parseInt(args[1]);
		long total = Long.parseLong(args[2]);
		
		int face_count = 0;
		int body_count = 0;
		int i = 0;
		while (i < total) {
			CaptureImg create = model.create(pre_face_count, true);
			CaptureFace[] faces = create.getCaptureFaces();
			face_count += faces.length;
			CaptureBody[] bodys = create.getCaptureBodys();
			body_count += bodys.length;
			String data = JSONObject.toJSONString(create);
			try {
				KfkProducer.sendMessage(zkConnect, brokerlist, topicName, 1, data);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			i++;
			if (i % 5000 == 0) {
				System.out.println("count : " + i);
			}
			sleep(10);
		}
		System.out.println("face count : " + face_count);
		System.out.println("body count : " + body_count);
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}