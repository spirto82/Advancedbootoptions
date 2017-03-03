package com.sparrowred.advancedbootoptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;

public class Options extends AppCompatActivity {

    private int mOption;
    private GridView gridview;
    private String[] commands;
    private String command;
    private boolean isRooted;
    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        gridview = (GridView) findViewById(R.id.gridview);
        toolbar.setTitle(R.string.tollbar_title);
        setSupportActionBar(toolbar);
        ImageAdapter myAdapter = new ImageAdapter(this);
        gridview.setAdapter(myAdapter);
        Intent intent = getIntent();

        if(intent != null){
            isRooted = getIntent().getExtras().getBoolean("isRooted");
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mOption = position;
                ViewHolder holder = (ViewHolder) v.getTag();
                final ImageView imageview = holder.image;
                Resources res = Options.this.getResources();
                commands = res.getStringArray(R.array.commands);
                command = commands[mOption];
                //imageview.setImageResource(R.drawable.trans_root_on);
                ((TransitionDrawable)imageview.getDrawable()).startTransition(2000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        advancedBootOptionAction(command);
                        ((TransitionDrawable)imageview.getDrawable()).reverseTransition(2000);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        finishAffinity();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gridview.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
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
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private void advancedBootOptionAction(final String action){
        String command = "";
        String message = "";
        switch(action){
            case "busybox killall system_server":
                command = getResources().getString(R.string.softReboot_command);
                message = getResources().getString(R.string.softReboot_message);
                break;
            case "reboot -p":
                command = getResources().getString(R.string.powerOff_command);
                message = getResources().getString(R.string.powerOff_message);
                break;
            case "reboot":
                command = getResources().getString(R.string.reboot_command);
                message = getResources().getString(R.string.reboot_message);
                break;
            case "reboot recovery":
                command = getResources().getString(R.string.rebootRecovery_command);
                message = getResources().getString(R.string.rebootRecovery_message);
                break;
            case "reboot bootloader":
                command = getResources().getString(R.string.rebootBootloader_command);
                message = getResources().getString(R.string.rebootBootloader_message);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(command);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Runtime.getRuntime().exec(new String[] { "su", "-c","mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system/" });
                    Runtime.getRuntime().exec(new String[] { "su", "-c", "chmod 777 /system/" });
                        Runtime.getRuntime().exec(new String[]{"su", "-c", action});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //resetImages(gridview, mOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}