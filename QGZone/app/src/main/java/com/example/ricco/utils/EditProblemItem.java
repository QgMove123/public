package com.example.ricco.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.ricco.qgzone.R;

/**
 * Created by chenyi on 2016/8/15.
 */
public class EditProblemItem extends LinearLayout {

    private EditText oldAnswer;
    private EditText newAnswer;
    private int oldId;
    private int newId;

    public EditProblemItem(Context context) {
        super(context);

        //加载新控件的布局
        LayoutInflater.from(context).inflate(R.layout.item_edit_problem, this);
        Spinner oldProblem = (Spinner) findViewById(R.id.problem1);
        Spinner newProblem = (Spinner) findViewById(R.id.problem2);
        oldProblem.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg) {
                oldId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                oldId = 0;
            }
        });
        newProblem.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg) {
                newId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                newId = 0;
            }
        });

        oldAnswer = (EditText) findViewById(R.id.new_pass);
        oldAnswer = (EditText) findViewById(R.id.sure_pass);
    }


    public int getOldId() {
        return oldId;
    }

    public int getNewId() {
        return newId;
    }

    public String getOldAnswer() {
        return oldAnswer.getText().toString();
    }
    public String getNewAnswer() {
        return newAnswer.getText().toString();
    }
}
