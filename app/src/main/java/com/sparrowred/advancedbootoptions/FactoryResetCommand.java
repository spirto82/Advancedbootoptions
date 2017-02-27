package com.sparrowred.advancedbootoptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.io.IOException;

public class FactoryResetCommand extends AppCompatActivity {
    private int option;
    private String[] commands;
    private String[] messages;
    private String command;
    private String message;
    private boolean isRooted;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String rooted = "isPhoneRooted";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent != null){
            option = intent.getIntExtra("option", -1);
            isRooted = getIntent().getExtras().getBoolean("isRooted");
        }
        Resources res = FactoryResetCommand.this.getResources();
        commands = res.getStringArray(R.array.commands);
        command = commands[option];
        messages = res.getStringArray(R.array.messages);
        message = messages[option];

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog(command, message);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.commit();
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.commit();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.commit();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private void showDialog(String command, String message) throws Resources.NotFoundException {
        new AlertDialog.Builder(this).setTitle(command).setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Runtime.getRuntime().exec(new String[] { "su", "-c","mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system/" });
                            Runtime.getRuntime().exec(new String[] { "su", "-c", "chmod 777 /system/" });
                            Runtime.getRuntime().exec(new String[] { "su", "-c",  "recovery --wipe_data"  });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })*/.show();
    }
}
