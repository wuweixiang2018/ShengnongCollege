package com.education.shengnongcollege.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by wuweixiang on 17/9/22.
 */

public class FileUtil {
    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static boolean isCreateFileSuccess(String dir) {
        try {
            if (TextUtils.isEmpty(dir))
                return false;
            File file = new File(dir + "/" + System.currentTimeMillis() + ".txt");
            boolean result = file.createNewFile();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEmptyImage(String path) {
        JkysLog.d("wuweixiang16", "1");
        if (!new File(path).exists())
            return false;
        int width = -1;
        int height = -1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        JkysLog.d("wuweixiang16", "2");
        if (options.outHeight == -1 || options.outWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                int tempHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH
                        , ExifInterface.ORIENTATION_NORMAL);
                int tempWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH
                        , ExifInterface.ORIENTATION_NORMAL);

                width = tempHeight;
                height = tempWidth;
                JkysLog.d("wuweixiang16", "3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            width = options.outWidth;
            height = options.outHeight;
        }
        JkysLog.d("wuweixiang16", "width=" + width + " height=" + height);
        if (width == 0 || height == 0)
            return true;
        return false;
    }

    /**
     * 申请读的权限,用户同意之后,还需要申请写的权限,不过不会弹出权限允许对话框,因为读和写是同一个权限组的
     * 但直接申请写权限,用户同意之后,不需要再申请读权限
     *
     * @return
     */
    public static boolean checkWriteStoragePermission(Context ctx) {
        //访问存储卡权限
        int checkPermissionByStorage = ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkPermissionByStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
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

    /**
     * 优先外部存储空间,再手机存储空间
     *
     * @param context
     * @return
     */
    public static File getFilesDir(Context context) {
//        return getFilesDir(context, true);
        File file = null;
        if (checkExternalStorage()) {
            //sdcard外部存储
            //存储在sd卡上与包名相关的目录下,不需要申请读写权限
            file = context.getExternalFilesDir(null);
        } else {
            //内部存储
            file = context.getFilesDir();
        }
        return file;
    }

    /**
     * 优先外部存储空间,再手机存储空间
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
//        return getCacheDir(context, true);
        File file = null;
        if (checkExternalStorage()) {
            //sdcard外部存储
            //存储在sd卡上与包名相关的目录下,不需要申请读写权限
            file = context.getExternalCacheDir();
        } else {
            //内部存储
            file = context.getCacheDir();
        }
        return file;
    }

    public static File getExternalCacheDir(Context context) {
//        return getCacheDir(context, true);
        File file = null;
        if (checkExternalStorage()) {
            //sdcard外部存储
            //存储在sd卡上与包名相关的目录下,不需要申请读写权限
            file = context.getExternalCacheDir();
        }
        return file;
    }


//    public static File getCacheDir(Context context, boolean isNeedCheckPermission) {
//        File file = null;
//        if (checkExternalStorage()
//                && (!isNeedCheckPermission || checkWriteStoragePermission(context))) {
//            //sdcard外部存储
//            file = context.getExternalCacheDir();
//        } else {
//            //内部存储
//            file = context.getCacheDir();
//        }
//        return file;
//    }

    public static File getExternalStorageDir(Context context) {
        return getExternalStorageDir(context, true);
    }

    /**
     * 获取外部存储路径,不会随着app卸载而删除
     *
     * @param context
     * @return 可能返回null
     */
    public static File getExternalStorageDir(Context context, boolean isNeedCheckPermission) {
        File file = null;

        if (checkExternalStorage()) {
            //sdcard外部存储
            //context == null 需要外层逻辑确保,已checkWriteStoragePermission
            if (context == null
                    || (!isNeedCheckPermission || checkWriteStoragePermission(context)))
                file = Environment.getExternalStorageDirectory();
//            else
//                file = context.getCacheDir();
        }
//        else {
//            if (context != null)
//                file = context.getCacheDir();
//        }

        return file;
    }

    public static String getExternalStorageDir(Context context, String pathName) {
        File fileTemp = FileUtil.getExternalStorageDir(context);
        if (fileTemp == null)
            return null;
        String filePath = fileTemp.getAbsolutePath();
        if (!TextUtils.isEmpty(filePath)) {
            if (pathName.startsWith(File.separator)) {
                filePath = filePath + pathName;
            } else {
                filePath = filePath + File.separator + pathName;
            }
        }
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return filePath;
    }

    /**
     * 手机存储空间
     *
     * @param context
     * @param pathName
     * @return
     */
    public static String getInternalFilesDir(Context context, String pathName) {
        File fileTemp = context.getFilesDir();
        if (fileTemp == null)
            return null;
        String filePath = fileTemp.getAbsolutePath();
        if (!TextUtils.isEmpty(filePath)) {
            if (pathName.startsWith(File.separator)) {
                filePath = filePath + pathName;
            } else {
                filePath = filePath + File.separator + pathName;
            }
        }
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return filePath;
    }


    /**
     * 计算Sdcard的剩余大小
     *
     * @return MB
     */
    public static long getExternalStorageAvailableSize() {

//        //得到外部储存sdcard的状态
//        String sdcard = Environment.getExternalStorageState();
//        //外部储存sdcard存在的情况
//        String state = Environment.MEDIA_MOUNTED;

        //获得路径
        File file = FileUtil.getExternalStorageDir(null);
        StatFs statFs = new StatFs(file.getPath());
        if (file != null) {
            //获得Sdcard上每个block的size
            long blockSize = statFs.getBlockSize();
            //获取可供程序使用的Block数量
            long blockavailable = statFs.getAvailableBlocks();
            //计算标准大小使用：1024，当然使用1000也可以
            long blockavailableTotal = blockSize * blockavailable;
            return blockavailableTotal;
        } else {
            return -1;
        }
    }

    public static File getFile(Context ctx, String subDir, String filename) {
        File fileDir = getFilesDir(ctx);
//        File fileDir = getExternalStorageDir(ctx, false);
        File subDirFile = new File(fileDir, subDir);
        if (!subDirFile.exists())
            subDirFile.mkdir();
        File file = new File(fileDir, subDir + "/" + filename);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;
    }

    public static <T> T getJsonObj(Context ctx, String filename, Class<T> tcalss) {
        String jsonStr = getJsonString(ctx, filename);
        if (TextUtils.isEmpty(jsonStr))
            return null;
        return GsonUtil.getCommonGson().fromJson(jsonStr, tcalss);
    }

    public static String getJsonString(Context ctx, String filename) {
        BufferedReader reader = null;
        try {
            File file = getFile(ctx, "json", filename + ".json");
            if (!file.exists())
                return null;
            InputStream mInputStream = new FileInputStream(file);

            reader = new BufferedReader(new InputStreamReader(mInputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            return jsonString.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // TODO: handle exception
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void writeFile(Context ctx, String text, String subDir, String filename,
                                 boolean append, boolean autoLine) {
        RandomAccessFile raf = null;
        FileOutputStream out = null;
        try {
            File file = getFile(ctx, subDir, filename);
            if (append) {
                //如果为追加则在原来的基础上继续写文件
                raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(text.getBytes("UTF-8"));
                if (autoLine) {
                    raf.write("\n".getBytes());
                }
            } else {
                //重写文件，覆盖掉原来的数据
                out = new FileOutputStream(file);
                out.write(text.getBytes("UTF-8"));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveJson(Context ctx, String jsonStr, String filename) {
        //Write the file to disk
        Writer writer = null;
        try {
            File file = getFile(ctx, "json", filename + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out);
            writer.write(jsonStr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean isExistedFile(Context ctx, String srcSubDir) {
        File file = new File(getFilesDir(ctx), srcSubDir);
        if (file.exists() && file.length() > 0) {
            return true;
        }
        return false;
    }

    public static void zipFolderBySubDir(Context ctx, String srcSubDir, File zipFile) throws Exception {
        zipFolder(getFilesDir(ctx).getAbsolutePath() + File.separator + srcSubDir, zipFile);
    }

    public static void zipFolder(String srcFilePath, File zipFile) throws Exception {
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        // 创建Zip包
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFile.getAbsolutePath()));
        // 打开要输出的文件
        File file = new File(srcFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 压缩
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);
        // 完成,关闭
        outZip.finish();
        outZip.close();
    }

    private static void zipFiles(String folderPath, String filePath, ZipOutputStream zipOut)
            throws Exception {
        if (zipOut == null) {
            return;
        }
        File file = new File(folderPath + filePath);
        // 判断是不是文件
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
            inputStream.close();
            zipOut.closeEntry();
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();
            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(filePath + File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }
            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath + File.separator + fileList[i], zipOut);
            }
        }
    }

    public static void deleteSubDir(Context ctx, String subDir) {
        deleteDir(getFilesDir(ctx) + File.separator + subDir);
    }

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        boolean isSuccess = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                isSuccess = true;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        return isSuccess;
    }

}


