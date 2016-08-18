package com.example.ricco.others;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mr_Do on 2016/8/15.
 */
public class RespondViewHolder {
    private ImageButton imageButton;
    private EditText editText;
    private TextView textView;

    public EditText getEditText() {
        return editText;
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }
}
