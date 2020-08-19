package com.sdg.update.interfac;

/**
 * @author sdg
 * @createTime 2020/4/10
 * */
public interface CheckCallback {

    /**
     * 有版本且点击了升级
     * @desc 该方法主要用于让用户点击升级后可操作（
     * 例如：清空记住的密码，升级后需要再次输入密码可登录，取消免登录功能）
     * */
    void update();

    /**
     * 没有新版本
     * */
    void noNewVersion();

    /**
     * 有新版本未设置强制升级且点击暂不升级
     * */
    void cancelUpdate();
}
