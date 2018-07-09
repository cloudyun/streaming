package com.yands.streaming.create;

import java.util.Date;

public class CaptureImgConstants {

	public CaptureImgConstants() {
		this(1000);
	}

	public CaptureImgConstants(int seed) {
		super();
		ConstantsUtil.createRandom(seed);
	}

	public CaptureImgModel create(int days, Date curDate) {
		CaptureImgModel model = new CaptureImgModel();
		int seed = 1;// 1毫秒一个种子

		return create(seed, days, curDate, model);
	}
	protected CaptureImgModel create(int seed, int days, Date curDate, CaptureImgModel model) {
		return model;
	}
	


	// JDCTX的model基类
	public abstract class JDCTXBaseModel {
		protected int seed = 0;

		public JDCTXBaseModel() {}

		public JDCTXBaseModel(int seed) {
			this.seed = seed;
		}
	}
	
	
	public class CaptureImgModel {
		@SuppressWarnings("unused")
		private IDModel idModel;
	}
	
	public class IDModel extends JDCTXBaseModel {
		
		private String id = "";
		
		public String getID() {
			id = getConvertID(15, seed);
			return id;
		}
		
	}

	public static String getConvertID(int length, int seed) {
		String id = String.valueOf(System.currentTimeMillis());
		if (id.length() > length) {
			id = id.substring(0, 15);
		}
		int len = length - id.length();
		String tmp = "";
		for (int i = 1; i <= len; i++) {
			tmp += "0";
		}
		id = tmp + id;
		StringBuffer sb = new StringBuffer(id);
		return sb.reverse().toString() + String.format("%03d", ConstantsUtil.getNumber(1000, seed));
	}
	
}
