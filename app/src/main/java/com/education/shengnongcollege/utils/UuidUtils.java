package com.education.shengnongcollege.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import com.jkys.jkyscommon.baseclass.CommonTopActivity;

/**
 * Created by on
 * Author: xiaoke
 * DATE: 2016/9/9
 * Time: 9:47
 * email: fmqin@91jkys.com
 */
public class UuidUtils {
    public static String UUID_KEY = "uuid";
    public static String UUID_FILE_NAME = "uuid.txt";
    public static String UUID_HIDE_FILE_DIR = "/.UUID";
    public static String UUID_FILE_DIR = "/UUID";

    public static void setUuidToSpf(Context context, String uuid) {
        SpUtil.inputSP(context, UUID_KEY, uuid);
    }

    public static String getUuidFromSpf(Context context) {
        String uuid = (String) SpUtil.getSP(context, UUID_KEY, "");
        return uuid;
    }

    public static void setUuidToSetting(Context context, String uuid) {
        try {
            Settings.System.putString(context.getContentResolver(), UUID_KEY, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUuidFromSetting(Context context) {
        String uuid = null;
        try {
            uuid = Settings.System.getString(context.getContentResolver(), UUID_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }

    /**
     * 检验SDcard状态
     *
     * @return boolean
     */
    public static boolean checkExternalStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            return false;
        }
    }


//    public static String getDir(Context context, String pathName) {
//        String filePath = FileUtil.getExternalStorageDir(context).getAbsolutePath();
//        if (!TextUtils.isEmpty(filePath)) {
//            if (pathName.startsWith(File.separator)) {
//                filePath = filePath + pathName;
//            } else {
//                filePath = filePath + File.separator + pathName;
//            }
//        }
//        if (filePath != null) {
//            File file = new File(filePath);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//        }
//        return filePath;
//    }

//    public static void setUuidToFileByCheck(Context context, String pathName, String fileName, String uuid) {
//        BaseTopActivity activity = (BaseTopActivity) BaseTopActivity.getTopActivity();
//        if (activity != null && activity.checkWriteStoragePermission()) {
//            boolean result = setUuidToFile(context, pathName, fileName, uuid);
//            if (!result) {
//                activity.showSpecialPermissionsJumpDialog("存储");
//                return;
//            }
//            return;
//        } else {
////            if (activity != null) {
////                activity.showPermissionsJumpDialog("存储");
////            }
//            return;
//        }
//    }

    public static boolean setUuidToFile(Context context, String pathName, String fileName, String uuid) {
        try {
            String externalDir = FileUtil.getExternalStorageDir(context, pathName);
            if (!TextUtils.isEmpty(externalDir)) {
                String filePath = externalDir + File.separator + fileName;
                File file = new File(filePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException ex) {
                        return false;
                    }
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(uuid.getBytes());
                    fos.close();
                } catch (IOException ex) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

//    public static String getUuidFromFileByCheck(Context context, String pathName, String fileName) {
//        BaseTopActivity activity = (BaseTopActivity) BaseTopActivity.getTopActivity();
//        if (activity != null && activity.checkWriteStoragePermission()) {
//            String result = getUuidFromFile(context, pathName, fileName);
//            if (result != null && result.equals("permission deny")) {
//                activity.showSpecialPermissionsJumpDialog("存储");
//                return null;
//            }
//            return result;
//        } else {
////            if (activity != null) {
////                activity.showPermissionsJumpDialog("存储");
////            }
//            return null;
//        }
//    }


    public static String getUuidFromFile(Context context, String pathName, String fileName) {
        try {
            String externalDir = FileUtil.getExternalStorageDir(context, pathName);
            if (!TextUtils.isEmpty(externalDir)) {
                File file = new File(externalDir, fileName);
                String result = null;
                if (file.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] data = new byte[1024];
                        int len = -1;
                        while ((len = fis.read(data)) != -1) {
                            baos.write(data, 0, len);
                        }
                        result = new String(baos.toByteArray());
                        fis.close();
                    } catch (IOException ex) {
                        return "permission deny";
                    }
                    return result;
                } else {
                    //文件不存在
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //为了解决一种情况就是有些文件被用户删掉，则一一检查并保存
    public static void saveUuidToAllFile(Context context, String UUID) {
        String deviceUUID = "";
        deviceUUID = UuidUtils.getUuidFromFile(context, UuidUtils.UUID_FILE_DIR, UuidUtils.UUID_FILE_NAME);
        if (TextUtils.isEmpty(deviceUUID)) {
            UuidUtils.setUuidToFile(context, UuidUtils.UUID_FILE_DIR, UuidUtils.UUID_FILE_NAME, UUID);
        }

        deviceUUID = UuidUtils.getUuidFromFile(context, UuidUtils.UUID_HIDE_FILE_DIR, UuidUtils.UUID_FILE_NAME);
        if (TextUtils.isEmpty(deviceUUID)) {
            UuidUtils.setUuidToFile(context, UuidUtils.UUID_HIDE_FILE_DIR, UuidUtils.UUID_FILE_NAME, UUID);
        }

        deviceUUID = UuidUtils.getUuidFromSpf(context);
        if (TextUtils.isEmpty(deviceUUID)) {
            UuidUtils.setUuidToSpf(context, UUID);
        }

        deviceUUID = UuidUtils.getUuidFromSetting(context);
        if (TextUtils.isEmpty(deviceUUID)) {
            UuidUtils.setUuidToSetting(context, UUID);
        }
    }

}
