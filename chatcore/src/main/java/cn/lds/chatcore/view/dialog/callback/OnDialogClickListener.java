package cn.lds.chatcore.view.dialog.callback;

import android.app.Dialog;

import cn.lds.chatcore.view.dialog.annotation.ClickPosition;


@SuppressWarnings("all")
public interface OnDialogClickListener<D extends Dialog>
{
	public  void onDialogClick(D dialog, @ClickPosition String clickPosition);
}
