package com.education.shengnongcollege.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.education.shengnongcollege.R;


/**
 * @author JimmyZhang
 * @version V 1.0 修改历史： 修改者，2013-7-10　下午5:13:37，　修改内容
 * @Title DialogUtil.java
 * @Description 提示用户操作的对话框和吐丝
 * @date 2013-7-10
 */
public class DialogUtil {

	private static Dialog loadingDialog;

	public static DialogUtil dialogUtil;

	private ProgressDialog progressDialog = null;

//	private int mLayoutResId = R.layout.view_progress_dialog;

	public static int mAlertDialogIconId = android.R.drawable.ic_dialog_info;

	private DialogInterface.OnCancelListener mOnCancelListener;
	private DialogInterface.OnShowListener mOnShowlListener;

//	private GifImageView bigGifView;
//	private GifImageView smallGifView;
	private long lastClickTime = 0;

	public static DialogUtil getInstance() {
		if (null == dialogUtil)
			dialogUtil = new DialogUtil();
		return dialogUtil;
	}

	/**
	 * 设置自定义弹出框的显示图标
	 *
	 * @param drawableResId
	 */
	public static void setAlertDialogIcon(int drawableResId) {
		mAlertDialogIconId = drawableResId;
	}

	/**
	 * 设置进度悬浮框的布局资源
	 *
	 */
	public DialogUtil setContentView(int layoutResId) {
//		this.mLayoutResId = layoutResId;
		return this;
	}

	/**
	 * 设置进度悬浮框的布局资源
	 *
	 */
	public DialogUtil setContentView(View view) {
//		this.mLayoutResId = view.getId();
		return this;
	}

	/**
	 * 根据Context显示默认加载框
	 *
	 * @param context 上下文
	 */
	public void showProgressDialog(final Context context) {
		if(context != null){
			try {
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(context, context.getString(R.string.util_dialog_loading_please_waiting));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isDialogShowing(){
		if(progressDialog != null && progressDialog.isShowing()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 根据Context和String提示信息显示加载框
	 *
	 * @param context 上下文
	 * @param message 显示信息
	 */
	public void showProgressDialog(final Context context, final String message) {
		if(context != null){
			try {
				lastClickTime = 0;
				if(progressDialog == null) {
					progressDialog = new ProgressDialog(context, R.style.Dialog);
				}
				progressDialog.setMessage(message);
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				if (mOnCancelListener != null) {
					progressDialog.setOnCancelListener(mOnCancelListener);
				}
				if (mOnShowlListener != null) {
					progressDialog.setOnShowListener(mOnShowlListener);
				}
//				progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//					@Override
//					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
//					{
//						if (keyCode == KeyEvent.KEYCODE_SEARCH||keyCode == KeyEvent.KEYCODE_BACK)
//						{
//							try {
//								ICHttpClient.getInstance().cancelAllRequest();
//								cancelProgressDialog();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						return true;
//					}
//				});
				progressDialog.show();
//				if (mLayoutResId != -1) {
//					//progressDialog.setContentView(mLayoutResId);
//					View view = LayoutInflater.from(context).inflate(mLayoutResId, null);
//					progressDialog.setContentView(view);
//					//((TextView) view.findViewById(R.id.oaprogresstitle)).setText(message);
//					bigGifView = (GifImageView) view.findViewById(R.id.bigGifView);
//					bigGifView.setImageResource(R.drawable.pop_loading_img);
//					bigGifView.destroyDrawingCache();
//					smallGifView = (GifImageView) view.findViewById(R.id.smallGifView);
//					smallGifView.setImageResource(R.drawable.pop_loading_animate);
//					smallGifView.destroyDrawingCache();
//					view.setOnClickListener(new View.OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							if ((System.currentTimeMillis() - lastClickTime) < 500) {
//								try {
//									ICHttpClient.getInstance().cancelAllRequest();
//									cancelProgressDialog();
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								return;
//							}
//							lastClickTime = System.currentTimeMillis();
//						}
//					});
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public DialogUtil setOnCancelListener(
			DialogInterface.OnCancelListener listener) {
		mOnCancelListener = listener;
		return this;
	}

	public DialogUtil setOnShowlListener(DialogInterface.OnShowListener listener) {
		this.mOnShowlListener = listener;
		return this;
	}
	/**
	 * 关闭当前加载进度框
	 */
	public void cancelProgressDialog() {
		this.mOnShowlListener = null;
//		try {
//			if(bigGifView != null) {
//				bigGifView.destroyDrawingCache();
//				bigGifView = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			if(smallGifView != null) {
//				smallGifView.destroyDrawingCache();
//				smallGifView = null;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
            }
			progressDialog = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDialogDissmissListen(){
		if(progressDialog != null && progressDialog.isShowing()){

		}
	}

	/**
	 * 显示吐丝
	 *
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 此方法描述的是：
	 *
	 * @param context
	 * @param message
	 * @param gravity 显示位置
	 * @return void
	 * @author:JimmyZhang
	 * @since: 2013-11-19 下午5:36:03
	 */
	public static void showCustomToast(Context context, String message,
									   int gravity) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	/**
	 * 显示自定义对话框
	 *
	 * @param context
	 * @param title
	 * @param message
	 * @param onClickListener
	 */
	public static Dialog showCustomDialog(Context context, String title,
										  String message, View contentView, String positiveButtonTitle,
										  String negativeButtonTitle,
										  final OnCustomClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(mAlertDialogIconId);
		builder.setTitle(title);
		if (message != null) {
			builder.setMessage(message);
		} else if (contentView != null) {
			builder.setView(contentView);
		}
		if (null != positiveButtonTitle && !"".equals(positiveButtonTitle)) {
			builder.setPositiveButton(positiveButtonTitle,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (onClickListener != null) {
								onClickListener.PositiveButtonOnClick(dialog,
										which);
							}

						}
					});
		}

		if (null != negativeButtonTitle && !"".equals(negativeButtonTitle)) {
			builder.setNegativeButton(negativeButtonTitle,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (onClickListener != null) {
								onClickListener.NegativeButtonOnClick(dialog,
										which);
							}
						}
					});
		}

		loadingDialog = builder.create();
		return loadingDialog;
	}

	/**
	 * Interface used to allow the creator of a dialog to run some code when an
	 * item on the dialog is clicked..
	 */
	public interface OnCustomClickListener {
		public void PositiveButtonOnClick(DialogInterface dialog, int which);
		public void NegativeButtonOnClick(DialogInterface dialog, int which);
	}

}
