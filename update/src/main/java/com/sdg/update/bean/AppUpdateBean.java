package com.sdg.update.bean;

/**
 * @author sdg
 * @createTime 2020/4/10
 * */
public class AppUpdateBean {

    private int code;
    private DataBean data;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {

        private String appDownloadurl;
        private int appLastforce;
        private String appName;
        private String appRemark;
        private String appServerversion;
        private int appStatus;
        private Object appStr1;
        private Object appStr2;
        private String createTime;
        private String createUser;
        private String id;
        private String updateTime;
        private String updateUser;

        public String getAppDownloadurl() {
            return appDownloadurl;
        }

        public void setAppDownloadurl(String appDownloadurl) {
            this.appDownloadurl = appDownloadurl;
        }

        public int getAppLastforce() {
            return appLastforce;
        }

        public void setAppLastforce(int appLastforce) {
            this.appLastforce = appLastforce;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppRemark() {
            return appRemark;
        }

        public void setAppRemark(String appRemark) {
            this.appRemark = appRemark;
        }

        public String getAppServerversion() {
            return appServerversion;
        }

        public void setAppServerversion(String appServerversion) {
            this.appServerversion = appServerversion;
        }

        public int getAppStatus() {
            return appStatus;
        }

        public void setAppStatus(int appStatus) {
            this.appStatus = appStatus;
        }

        public Object getAppStr1() {
            return appStr1;
        }

        public void setAppStr1(Object appStr1) {
            this.appStr1 = appStr1;
        }

        public Object getAppStr2() {
            return appStr2;
        }

        public void setAppStr2(Object appStr2) {
            this.appStr2 = appStr2;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }
    }
}
