package com.yands.streaming.create;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.yands.streaming.vo.CaptureBody;
import com.yands.streaming.vo.CaptureFace;
import com.yands.streaming.vo.CaptureImg;

public class CaptureImgModel {
	
	public static void main(String[] args) {
		String date = "2018-04-03";
		int days = 10;
		int seed = 10;
		int faceNum = 4;
		CaptureImgModel model = new CaptureImgModel(date, days, seed, faceNum);
		
		int x = 0;
		while (x++ < 1000) {
			CaptureImg create = model.create();
			System.out.println(JSON.toJSONString(create));
		}
		
	}
	
	private Date stand = new Date();
	
	public int days = 5;
	
	public int seed = 100;
	
	public int faceNum = 5;
	
	private FaceBodyModel model = null;
	
	private static Long[] cameraIds = new Long[] {
			2147500032l, 2147500033l, 2147500034l, 2147500035l, 2147500036l, 2147500037l, 2147500038l, 2147500039l, 2147500040l, 2147500041l, 
			2147500042l, 2147500043l, 2147500044l, 2147500045l, 2147500046l, 2147500047l, 2147500048l, 2147500049l, 2147500050l, 2147500051l
	};
	
	private static String[] cameraNames = new String[] {
			"测试部的摄像机1", "测试部的摄像机2", "测试部的摄像机3", "测试部的摄像机4", "测试部的摄像机5", "测试部的摄像机6", "测试部的摄像机7", "测试部的摄像机8", "测试部的摄像机9", "测试部的摄像机10",
			"测试部的摄像机11", "测试部的摄像机12", "测试部的摄像机13", "测试部的摄像机14", "测试部的摄像机15", "测试部的摄像机16", "测试部的摄像机17", "测试部的摄像机18", "测试部的摄像机19", "测试部的摄像机20"
	};
	
	private static String[] address = new String[] {
			"地区1", "地区2", "地区3", "地区4", "地区5", "地区6", "地区7", "地区8", "地区9", "地区10",
			"地区11", "地区12", "地区13", "地区14", "地区15", "地区16", "地区17", "地区18", "地区19", "地区20",
	};
	
	private static double[][] ll = new double[][] {
		{116.403861999512, 39.9152145385742}, {118.403861999512, 37.9152145385742}, {112.403861999512, 35.9152145385742}, 
		{117.403861999512, 39.9152145385742},{116.403861999512, 36.9152145385742}, {126.403861999512, 36.9152145385742}, 
		{126.403861889512, 39.7752145385742}, {116.423861999512, 39.9182145385742} , {111.423861999512, 40.9182145385742} , {112.423861999512, 38.9182145385742} ,
		{116.403861999512, 39.9152145385742}, {118.403861999512, 37.9152145385742}, {112.403861999512, 35.9152145385742}, 
		{117.403861999512, 39.9152145385742},{116.403861999512, 36.9152145385742}, {126.403861999512, 36.9152145385742}, 
		{126.403861889512, 39.7752145385742}, {116.423861999512, 39.9182145385742} , {111.423861999512, 40.9182145385742} , {112.423861999512, 38.9182145385742}
	};

	private static Long[][] orgIds = new Long[][] {
		{ 100101000207l, 100101000172l }, 
		{ 100101000218l, 100101000208l }, 
		{ 100101000208l, 100101000173l }, 
		{ 100101000217l, 100101000216l },
		{ 100101000207l, 100101000172l, 100101000197l }, 
		{ 100101000218l, 100101000208l, 100101000199l }, 
		{ 100101000208l, 100101000173l, 100101000198l }, 
		{ 100101000217l, 100101000207l, 100101000189l }, 
		{ 100101000217l, 100101000216l, 100101000207l },
		{ 100101000217l, 100101000207l, 100101000189l, 100101000172l }, 
		{ 100101000217l, 100101000216l, 100101000207l, 100101000189l },
		{ 100101000217l, 100101000207l, 100101000189l, 100101000172l, 100101000214l }, 
		{ 100101000217l, 100101000216l, 100101000207l, 100101000189l, 100101000221l },
		{ 100101000217l, 100101000207l, 100101000189l, 100101000172l, 100101000214l, 100101000197l }, 
		{ 100101000217l, 100101000216l, 100101000207l, 100101000189l, 100101000221l, 100101000172l },
		{ 100101000217l, 100101000216l, 100101000207l, 100101000189l, 100101000221l, 100101000172l, 100101000214l },
		{ 100101000217l, 100101000216l, 100101000207l, 100101000189l, 100101000221l, 100101000172l, 100101000214l, 100101000197l }
	};
	
	private static Long[][] cameraTags = new Long[][] {
		{ 2147500032l, 72057602147500032l }, 
		{ 100100000082l, 100100000097l, 100100000102l }, 
		{ 100100000088l, 100100000027l, 100100000122l }, 
		{ 100100000107l, 100100000105l, 100100000093l, 100100000082l, 100100000097l, 100100000102l }
	};

	public CaptureImgModel(int seed, int faceNum) {
		this.seed = seed;
		ConstantsUtil.createRandom(seed);
		model = new FaceBodyModel(seed);
	}

	public CaptureImgModel(int days, int seed, int faceNum) {
		this.seed = seed;
		this.days = days;
		ConstantsUtil.createRandom(seed);
		model = new FaceBodyModel(seed);
	}

	/**
	 * @param date 指定日期
	 * @param days capture_time在指定日期的前后days天随机取值
	 * @param seed 随机种子时间单位ms
	 * @param faceNum 指定一张图片中人脸的数量0-faceNum
	 */
	public CaptureImgModel(String date, int days, int seed, int faceNum) {
		this.seed = seed;
		try {
			this.stand = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			this.stand = new Date();
		}
		ConstantsUtil.createRandom(seed);
		model = new FaceBodyModel(seed);
	}
	
	/**
	 * @param faces 人脸个数
	 * @return
	 */
	public CaptureImg create() {
		int num = 1 + ConstantsUtil.getNumber(faceNum - 1, seed);
		return create(num);
	}
	
	public CaptureImg create(int face_count) {
		return create(face_count, false);
	}
	
	/**
	 * @param face_count 人脸个数
	 * @param current抓拍时间是否设置为当前时间
	 * @return
	 */
	public CaptureImg create(int face_count, boolean current) {
		CaptureImg img = new CaptureImg();
		img.setId(ConstantsUtil.getConvertID(15, seed));
		int camearNum = ConstantsUtil.getNumber(cameraIds.length, seed);
		img.setCameraId(cameraIds[camearNum]);
		img.setCameraName(cameraNames[camearNum]);
		img.setCaptureTime(current ? ConstantsUtil.getCaptureTime() : ConstantsUtil.getCaptureTime(stand, days, seed));
		img.setCaptureAddress(address[camearNum]);
		img.setLongitude(ll[camearNum][0]);
		img.setLatitude(ll[camearNum][1]);
		img.setInstallation_location(new long[] {123l, 345l});
		img.setOrgIds(orgIds[ConstantsUtil.getNumber(orgIds.length, seed)]);
		img.setOperation_center_ids(new long[] {678l, 910l});
		img.setCameraTags(cameraTags[ConstantsUtil.getNumber(cameraTags.length, seed)]);
		img.setScenePath("lujin");
		CaptureFace[] faces = new CaptureFace[face_count];
		CaptureBody[] bodys = new CaptureBody[face_count];
		for (int x = 0; x < face_count; x++) {
			model.create();
			faces[x] = model.getFace();
			bodys[x] = model.getBody();
		}
		img.setCaptureFaces(faces);
		img.setCaptureBodys(bodys);
		return img;
	}
}
