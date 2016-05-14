package com.example.whatsemo.classmates.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by WhatsEmo on 5/13/2016.
 */
public class DeleteItemDialog extends DialogFragment {
    private Context mContext;
    private int dialogType;
    private String dialogTitle;
    private String dialogMessage;
    private AlertDialog.Builder mAlertDialogBuilder;
    private static final int COURSES = 0;
    private static final int INTERESTS = 1;
    private static final int NOT_REMOVED = 0;
    private static final int REMOVED = 1;

    public DeleteItemDialog createCustomDialog(AlertDialog.Builder alertDialogBuilder){
        mAlertDialogBuilder = alertDialogBuilder;
        return this;
    }
    /*
    public DeleteItemDialog createCustomDialog(Context context, int type, Interest interest){
        mContext = context;
        dialogType = type;
        if(INTERESTS == dialogType){
            dialogTitle = "Remove Interest:";
            dialogMessage = String.format("%s", interest.getName());
        }else{
            dialogTitle = "Remove ?:";
        }
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mAlertDialogBuilder.create();
    }
}
