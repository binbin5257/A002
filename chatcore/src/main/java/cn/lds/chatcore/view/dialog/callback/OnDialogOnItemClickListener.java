package cn.lds.chatcore.view.dialog.callback;

import android.app.Dialog;


@SuppressWarnings("all")
public interface OnDialogOnItemClickListener<D extends Dialog>
{
	public  void onDialogItemClick(D dialog, int position);
}
