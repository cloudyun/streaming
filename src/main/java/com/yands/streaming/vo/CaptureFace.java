/**
 * 
 */
package com.yands.streaming.vo;

/**
 * @author Administrator
 *
 */

public class CaptureFace {
    private String id;
    
    /**
     * 虚拟身份id
     */
    private Long vid;
    /**
     * 人脸评分列表
     */
    private float[] vidScores;
    /**
     * 人脸标签列表
     */
    private Long[] faceTags;
    
    private double eyeglass;
    
    private double facemask;
    
    private double male;
    
    private double female;
    
    /**
     * 是否戴眼镜
     */
    private Integer isEyeglass;
    /**
     * 是否戴面罩
     */
    private Integer isFacemask;
    /**
     * 男
     */
    private Integer isMale;
    /**
     * 女
     */
    private Integer isFemale;
    /**
     * 质量
     */
    private Float quality;
    /**
     * 民族
     */
    private String nation;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 姿势
     */
    private String pose;
    private String faceRect;
    private String featureBase64;
    /**
     * 人体路径
     */
    private String facePath;
    /**
     * 特征资源
     */
    private String featureResource;
    /**
     * 特征版本
     */
    private String featureVersion;
    /**
     * 对应人体id
     */
    private String bodyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public float[] getVidScores() {
        return vidScores;
    }

    public void setVidScores(float[] vidScores) {
        this.vidScores = vidScores;
    }

    public Long[] getFaceTags() {
        return faceTags;
    }

    public void setFaceTags(Long[] faceTags) {
        this.faceTags = faceTags;
    }

    public double getEyeglass() {
		return eyeglass;
	}

	public void setEyeglass(double eyeglass) {
		this.eyeglass = eyeglass;
	}

	public double getFacemask() {
		return facemask;
	}

	public void setFacemask(double facemask) {
		this.facemask = facemask;
	}

	public double getMale() {
		return male;
	}

	public void setMale(double male) {
		this.male = male;
	}

	public double getFemale() {
		return female;
	}

	public void setFemale(double female) {
		this.female = female;
	}

	public Integer getIsEyeglass() {
        return isEyeglass;
    }

    public void setIsEyeglass(Integer isEyeglass) {
        this.isEyeglass = isEyeglass;
    }

    public Integer getIsFacemask() {
        return isFacemask;
    }

    public void setIsFacemask(Integer isFacemask) {
        this.isFacemask = isFacemask;
    }

    public Integer getIsMale() {
        return isMale;
    }

    public void setIsMale(Integer isMale) {
        this.isMale = isMale;
    }

    public Integer getIsFemale() {
        return isFemale;
    }

    public void setIsFemale(Integer isFemale) {
        this.isFemale = isFemale;
    }

    public Float getQuality() {
        return quality;
    }

    public void setQuality(Float quality) {
        this.quality = quality;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPose() {
        return pose;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public String getFaceRect() {
        return faceRect;
    }

    public void setFaceRect(String faceRect) {
        this.faceRect = faceRect;
    }

    public String getFeatureBase64() {
        return featureBase64;
    }

    public void setFeatureBase64(String featureBase64) {
        this.featureBase64 = featureBase64;
    }

    public String getFacePath() {
        return facePath;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }

    public String getFeatureResource() {
        return featureResource;
    }

    public void setFeatureResource(String featureResource) {
        this.featureResource = featureResource;
    }

    public String getFeatureVersion() {
        return featureVersion;
    }

    public void setFeatureVersion(String featureVersion) {
        this.featureVersion = featureVersion;
    }

    public String getBodyId() {
        return bodyId;
    }

    public void setBodyId(String bodyId) {
        this.bodyId = bodyId;
    }

}
