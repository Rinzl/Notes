package com.thd.notes.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.thd.notes.R;
import com.thd.notes.model.Note;

/**
 * Created by Tran Hai Dang on 11/3/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class ColorDialogFragment extends DialogFragment implements View.OnClickListener {
    private View whiteColor;
    private View orangeColor;
    private View greenColor;
    private View pinkColor;
    private OnColorPicked onColorPicked;
    public interface OnColorPicked {
        void onCLick(String color);
    }

    public void setOnColorPicked(OnColorPicked onColorPicked) {
        this.onColorPicked = onColorPicked;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_pick,null);
        builder.setView(view).setTitle("Choose color");
        whiteColor = view.findViewById(R.id.whiteColor);
        orangeColor = view.findViewById(R.id.orangeColor);
        greenColor = view.findViewById(R.id.greenColor);
        pinkColor = view.findViewById(R.id.pinkColor);
        clickView();
        return builder.create();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whiteColor :
                onColorPicked.onCLick(Note.COLOR_WHITE);
                this.dismiss();
                break;
            case R.id.orangeColor :
                onColorPicked.onCLick(Note.COLOR_ORANGE);
                this.dismiss();
                break;
            case R.id.greenColor :
                onColorPicked.onCLick(Note.COLOR_GREEN);
                this.dismiss();
                break;
            case R.id.pinkColor :
                onColorPicked.onCLick(Note.COLOR_PINK);
                this.dismiss();
                break;
        }
    }
    public void clickView() {
        whiteColor.setOnClickListener(this);
        orangeColor.setOnClickListener(this);
        pinkColor.setOnClickListener(this);
        greenColor.setOnClickListener(this);
    }
}
