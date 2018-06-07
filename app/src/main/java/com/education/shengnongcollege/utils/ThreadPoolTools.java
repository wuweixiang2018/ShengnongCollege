package com.education.shengnongcollege.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * Created by wuweixiang on 16/8/31.
 * 线程管理工具类
 */
public class ThreadPoolTools {
    private static ThreadPoolTools instance;

    public synchronized static ThreadPoolTools getInstance() {
        if (instance == null)
            instance = new ThreadPoolTools();
        return instance;
    }

    private ThreadPoolTools() {
        sWorkerThread = new HandlerThread("sc");
        sWorkerThread.start();
        //必须先start，才能拿到Looper
        sWorker = new Handler(sWorkerThread.getLooper());
    }

    /**
     * 现阶段使用单独的线程处理配置信息的解析和保存
     */
    private HandlerThread sWorkerThread;

    private Handler sWorker;

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 在工作线程中运行
     *
     * @param runnable
     */
    public void postWorkerTask(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        if (sWorkerThread.getThreadId() == android.os.Process.myTid()) {
            runnable.run();
        } else {
            // If we are not on the worker thread, then post to the worker handler
            sWorker.post(runnable);
        }
    }

    /**
     * 在工作线程中运行,推迟delayMillis
     *
     * @param runnable
     * @param delayMillis
     */
    public void postWorkerTask(Runnable runnable, long delayMillis) {
        if (runnable == null) {
            return;
        }

        sWorker.postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程中运行,
     *
     * @param runnable
     */
    public void postMainTask(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        mMainHandler.post(runnable);
    }

    /**
     * 在主线程中运行,推迟delayMillis
     *
     * @param runnable
     * @param delayMillis
     */
    public void postMainTask(Runnable runnable, long delayMillis) {
        if (runnable == null) {
            return;
        }

        mMainHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 移除UI线程的回调，防止内存泄漏
     *
     * @param runnable
     */
    public void removeMainCallbacks(Runnable runnable) {
        mMainHandler.removeCallbacks(runnable);
    }

    /**
     * 移除UI线程的回调，防止内存泄漏
     *
     * @param runnableList
     */
    public void removeMainCallbacks(List<Runnable> runnableList) {
        for (Runnable runnable : runnableList)
            mMainHandler.removeCallbacks(runnable);
    }

    /**
     * 移除工作线程的回调，防止内存泄漏
     *
     * @param runnable
     */
    public void removeWorkerCallbacks(Runnable runnable) {
        sWorker.removeCallbacks(runnable);
    }

    /**
     * 移除工作线程的回调，防止内存泄漏
     *
     * @param runnableList
     */
    public void removeWorkerCallbacks(List<Runnable> runnableList) {
        for (Runnable runnable : runnableList)
            sWorker.removeCallbacks(runnable);
    }

}
