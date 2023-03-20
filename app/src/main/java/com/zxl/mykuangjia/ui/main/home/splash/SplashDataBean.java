package com.zxl.mykuangjia.ui.main.home.splash;

import java.io.Serializable;

public class SplashDataBean implements Serializable {

    private static final long serialVersionUID = 7382351359868556980L;//这里需要写死 序列化Id
    private String id;//序列表
    private String imgUrl;//图片资源地址
    private String linkUrl;//链接地址
    private String publishTime;//发布时间(秒)
    private String saveDuration;//存续时间(秒)
    private String fileName;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getSaveDuration() {
        return saveDuration;
    }

    public void setSaveDuration(String saveDuration) {
        this.saveDuration = saveDuration;
    }
}
