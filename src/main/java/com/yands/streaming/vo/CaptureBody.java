/**
 * 
 */
package com.yands.streaming.vo;

/**
 * @author Administrator
 *
 */
public class CaptureBody {
    private String id;
    /**
     * 特征资源
     */
    private String featureResource;
    /**
     * 特征版本
     */
    private String featureVersion;
    /**
     * 人体标签
     */
    private Long[] bodyTags;
    /**
     * 短裤
     */
    private Integer isPants;
    /**
     * 袖子
     */
    private Integer isSleeve;
    /**
     * 上身衣服颜色
     */
    private Integer upperColor;
    /**
     * 下身衣服颜色
     */
    private Integer bottomColor;
    /**
     * 朝向
     */
    private Float orientation;
    private String featureBase64;
    /**
     * 人体地址路径
     */
    private String bodyPath;
    private String personRect;
    /**
     * 对应人脸id
     */
    private String faceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long[] getBodyTags() {
        return bodyTags;
    }

    public void setBodyTags(Long[] bodyTags) {
        this.bodyTags = bodyTags;
    }

    public Integer getIsPants() {
        return isPants;
    }

    public void setIsPants(Integer isPants) {
        this.isPants = isPants;
    }

    public Integer getIsSleeve() {
        return isSleeve;
    }

    public void setIsSleeve(Integer isSleeve) {
        this.isSleeve = isSleeve;
    }

    public Integer getUpperColor() {
        return upperColor;
    }

    public void setUpperColor(Integer upperColor) {
        this.upperColor = upperColor;
    }

    public Integer getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(Integer bottomColor) {
        this.bottomColor = bottomColor;
    }

    public Float getOrientation() {
        return orientation;
    }

    public void setOrientation(Float orientation) {
        this.orientation = orientation;
    }

    public String getFeatureBase64() {
        return featureBase64;
    }

    public void setFeatureBase64(String featureBase64) {
        this.featureBase64 = featureBase64;
    }

    public String getBodyPath() {
        return bodyPath;
    }

    public void setBodyPath(String bodyPath) {
        this.bodyPath = bodyPath;
    }

    public String getPersonRect() {
        return personRect;
    }

    public void setPersonRect(String personRect) {
        this.personRect = personRect;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

}
