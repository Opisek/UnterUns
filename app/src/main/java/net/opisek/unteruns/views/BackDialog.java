package net.opisek.unteruns.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import net.opisek.unteruns.R;

public class BackDialog extends AppCompatDialogFragment {

    BackListener listener;
    public interface BackListener {
        public void onUserResponse(Boolean result);
    }
    public void setBackListener(BackListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_back);
        builder.setMessage(R.string.label_back);
        builder.setPositiveButton(R.string.button_back_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onUserResponse(true);
            }
        });
        builder.setNegativeButton(R.string.button_back_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onUserResponse(false);
            }
        });
        return builder.create();
    }
}
