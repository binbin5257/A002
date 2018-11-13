package cn.lds.chatcore.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import cn.lds.chatcore.R;

/**
 * Yes / No dialog builder.
 * 
 */
public class ConfirmDialogBuilder {

	public ConfirmDialogBuilder(final Context context, final ConfirmDialogListener listener, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle(title);
		builder.setPositiveButton(R.string.common_confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onAccept();
			}
		});

		builder.setNegativeButton(R.string.common_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onCancel();
			}
		});

		builder.create().show();
	}
}
