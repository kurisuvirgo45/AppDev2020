package com.example.activity5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public abstract class DialogWith2Buttons {
    private String title, message, positiveButton, negativeButton;
    private Activity activity;
    public DialogWith2Buttons(String title, String positiveButton, String negativeButton, Activity activity){
        this.title = title;
        this.message = "";
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.activity = activity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void openDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setPositiveButton(positiveButton, (dialog, which) -> {
            onPostiveClick();
        }).setNegativeButton(negativeButton,  (dialog, which) -> {
            onNegativeClick();
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public abstract void onPostiveClick();
    public abstract void onNegativeClick();
}
