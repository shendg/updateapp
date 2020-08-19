package com.sdg.update.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.sdg.update.UpdateAppBean;
import com.sdg.update.UpdateAppManager;
import com.sdg.update.UpdateCallback;
import com.sdg.update.bean.AppUpdateBean;
import com.sdg.update.interfac.CheckCallback;
import com.sdg.update.service.DownloadService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VersionUtil {

    private CheckCallback mCheckCallback;
    public static VersionUtil versionUtil;

    public static VersionUtil getInstance() {
        if (versionUtil == null) {
            versionUtil = new VersionUtil();
            return versionUtil;
        }
        return versionUtil;
    }

    /**
     * 版本检测
     * @param versionUrl 版本包地址
     */
    public void checkVersion(final Activity activity, String versionUrl,CheckCallback checkCallback) {
        this.mCheckCallback = checkCallback;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android" +
                "/data/" + getPackageName(activity) + "/files/apk/";
        Map<String, String> params = new HashMap<>();
        params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(activity)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(versionUrl)

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        AppUpdateBean appInfoEntity = JSON.parseObject(json, AppUpdateBean.class);
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        if (appInfoEntity.getCode() == 200) {
                            AppUpdateBean.DataBean dataBean = appInfoEntity.getData();
                            if (AppUpdateUtils.getVersionCode(activity) < Integer.parseInt(dataBean.getAppServerversion())) {
                                updateAppBean.setUpdate("Yes");
                                if (dataBean.getAppLastforce() == 1) {
                                    //强制
                                    updateAppBean.setConstraint(true);
                                } else {
                                    //0
                                    updateAppBean.setConstraint(false);
                                }
                                updateAppBean
                                        .setNewVersion(dataBean.getAppName())//这里改为版本名称1.0.50
                                        // dataBean.getAppServerversion()
                                        .setUpdateLog(dataBean.getAppRemark())
                                        .setApkFileUrl("http://120.79.146.36:18095" + dataBean.getAppDownloadurl());
                            } else {
                                updateAppBean.setUpdate("No");
                            }
                        }
                        return updateAppBean;
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp,
                                          UpdateAppManager updateAppManager) {
                        //强制更新，
                        if (updateApp.isConstraint()) {

                        } else {

                        }
                        //自定义对话框
                        showDiyDialog(activity,updateApp, updateAppManager);
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                    }

                    @Override
                    protected void noNewApp() {
                        super.noNewApp();
                        mCheckCallback.noNewVersion();
                    }
                });
    }

    /**
     * 自定义对话框
     *
     * @param updateApp
     * @param updateAppManager
     */
    private void showDiyDialog(final Activity activity, UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {
        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog().replace("\\n", "\n");//换行
        String msg = "";

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：" + targetSize + "\n\n";
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
        }
        if (updateApp.isConstraint()) {
            new AlertDialog.Builder(activity)
                    .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                    .setMessage(msg)
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //升级后需要再次登录
                            mCheckCallback.update();
                            loadApp(activity,updateAppManager, dialog);
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                    .setMessage(msg)
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //升级后需要再次登录
                            mCheckCallback.update();
                            loadApp(activity,updateAppManager, dialog);
                        }
                    })
                    .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCheckCallback.cancelUpdate();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private boolean isShowDownloadProgress = true;

    private void loadApp(final Activity activity, UpdateAppManager updateAppManager, DialogInterface dialog) {
        //显示下载进度
        if (isShowDownloadProgress) {
            updateAppManager.download(new DownloadService.DownloadCallback() {
                @Override
                public void onStart() {
                    HProgressDialogUtils.showHorizontalProgressDialog(activity, "下载进度", false);
                }

                /**
                 * 进度
                 *
                 * @param progress  进度 0.00 -1.00 ，总大小
                 * @param totalSize 总大小 单位B
                 */
                @Override
                public void onProgress(float progress, long totalSize) {
                    HProgressDialogUtils.setProgress(Math.round(progress * 100));
                }

                /**
                 *
                 * @param total 总大小 单位B
                 */
                @Override
                public void setMax(long total) {

                }

                @Override
                public boolean onFinish(File file) {
                    HProgressDialogUtils.cancel();
                    activity.finish();
                    return true;
                }

                @Override
                public void onError(String msg1) {
                    Toast.makeText(activity,msg1,Toast.LENGTH_SHORT).show();
                    HProgressDialogUtils.cancel();
                }
            });
        } else {
            //不显示下载进度
            updateAppManager.download();
        }
        dialog.dismiss();
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    private static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
