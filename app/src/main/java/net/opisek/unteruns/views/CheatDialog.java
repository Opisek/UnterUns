package net.opisek.unteruns.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import net.opisek.unteruns.R;

public class CheatDialog extends AppCompatDialogFragment {

    CheatDialogListener listener;
    public interface CheatDialogListener {
        public void onUserResponse(Boolean result);
    }
    public void setCheatDialogListenerListener(CheatDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_cheat);
        builder.setMessage(R.string.label_cheat);
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton(R.string.button_cheat_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onUserResponse(input.getText().toString().equals("q11unteruns"));
            }
        });
        builder.setNegativeButton(R.string.button_cheat_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onUserResponse(false);
            }
        });
        return builder.create();
    }
}
