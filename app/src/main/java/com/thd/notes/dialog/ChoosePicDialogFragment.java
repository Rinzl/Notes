package com.thd.notes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.thd.notes.R;

/**
 * Created by Tran Hai Dang on 11/4/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class ChoosePicDialogFragment extends DialogFragment implements View.OnClickListener {
    private OnActionClicked onActionClicked;
    public interface OnActionClicked {
        void onClick(View v);
    }

    public void setOnActionClicked(OnActionClicked onActionClicked) {
        this.onActionClicked = onActionClicked;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_take_picture,null);
        builder.setView(view).setTitle("Take picture ");
        Button btnCam = (Button) view.findViewById(R.id.btnCamera);
        Button btnGallery = (Button) view.findViewById(R.id.btnGallery);
        btnCam.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        onActionClicked.onClick(v);
        this.dismiss();
    }
}
