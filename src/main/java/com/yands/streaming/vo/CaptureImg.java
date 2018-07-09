package com.yands.streaming.vo;

/**
 * @author Administrator
 *
 */

public class CaptureImg {
    private String id;
    /**
     * 相机id
     */
    private Long cameraId;
    /**
     * 相机名称
     */
    private String cameraName;
    /**
     * 捕获时间
     */
    private String captureTime;
    /**
     * 捕获地址
     */
    private String captureAddress;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    
    private long[] installation_location;
    /**
     * 机构id列表
     */
    private Long[] orgIds;
    
    private long[] operation_center_ids;
    /**
     * 相机标签列表
     */
    private Long[] cameraTags;
    
    
    public long[] getInstallation_location() {
		return installation_location;
	}

	public void setInstallation_location(long[] installation_location) {
		this.installation_location = installation_location;
	}

	public long[] getOperation_center_ids() {
		return operation_center_ids;
	}

	public void setOperation_center_ids(long[] operation_center_ids) {
		this.operation_center_ids = operation_center_ids;
	}

	/**
     * 场景图片路径
     */
    private String scenePath;
    /**
     * 人体特征列表
     */
    private CaptureFace[] captureFaces;
    /**
     * 人脸特征列表
     */
    private CaptureBody[] captureBodys;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public String getCaptureAddress() {
        return captureAddress;
    }

    public void setCaptureAddress(String captureAddress) {
        this.captureAddress = captureAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Long[] orgIds) {
        this.orgIds = orgIds;
    }

    public Long[] getCameraTags() {
        return cameraTags;
    }

    public void setCameraTags(Long[] cameraTags) {
        this.cameraTags = cameraTags;
    }

    public String getScenePath() {
        return scenePath;
    }

    public void setScenePath(String scenePath) {
        this.scenePath = scenePath;
    }

    public CaptureFace[] getCaptureFaces() {
        return captureFaces;
    }

    public void setCaptureFaces(CaptureFace[] captureFaces) {
        this.captureFaces = captureFaces;
    }

    public CaptureBody[] getCaptureBodys() {
        return captureBodys;
    }

    public void setCaptureBodys(CaptureBody[] captureBodys) {
        this.captureBodys = captureBodys;
    }

}
