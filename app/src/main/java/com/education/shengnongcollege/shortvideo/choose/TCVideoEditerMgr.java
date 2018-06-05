package com.education.shengnongcollege.shortvideo.choose;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.tencent.liteav.basic.log.TXCLog;

import java.io.File;
import java.util.ArrayList;

public class TCVideoEditerMgr {

    private static final String TAG = "TCVideoEditerMgr";

    public static ArrayList<TCVideoFileInfo> getAllVideo(Context context) {
        ArrayList<TCVideoFileInfo> videos = new ArrayList<TCVideoFileInfo>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor == null) return videos;

        if (cursor.moveToFirst()) {
            do {
                TCVideoFileInfo fileItem = new TCVideoFileInfo();
                fileItem.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                File file = new File(fileItem.getFilePath());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                if (duration < 0)
                    duration = 0;
                fileItem.setDuration(duration);

                if (fileItem.getFileName() != null && fileItem.getFileName().endsWith(".mp4")) {
                    videos.add(fileItem);
                }
                TXCLog.d(TAG, "fileItem = " + fileItem.toString());
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }
}