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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.io.IOException;

public class Options extends AppCompatActivity {

    private int mOption;
    private GridView gridview;
    private String[] commands;
    private String command;
    private boolean isRooted;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String rooted = "isPhoneRooted";

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
        if(!isRooted){
            isRooted = true;
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //final ViewHolder holder = (ViewHolder) v.getTag();
                mOption = position;

                Resources res = Options.this.getResources();
                commands = res.getStringArray(R.array.commands);
                command = commands[mOption];
                advancedBootOptionAction(command);

                //advancedBootOptionAction(mOption);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setPreferences();
        finishAffinity();
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        setPreferences();
        finishAffinity();
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
        setPreferences();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /*private void advancedBootOptionAction(final int action){
        switch(action){
            case 0:
                startNewActivity(action, PowerOffCommand.class);
                break;
            case 1:
                startNewActivity(action, RebootCommand.class);
                break;
            case 2:
                startNewActivity(action, RebootRecoveryCommand.class);
                break;
            case 3:
                startNewActivity(action, RebootBootloaderCommand.class);
                break;
            case 4:
                startNewActivity(action, FactoryResetCommand.class);
                break;
        }
    }

    private void startNewActivity(int option, Class<?> cls) {
        Intent intent = new Intent(getBaseContext(), cls);
        intent.putExtra("option",option);
        startActivity(intent);
    }*/

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
            case "recovery wipe cache":
                command = getResources().getString(R.string.factoryReset_command);
                message = getResources().getString(R.string.factoryReset_message);
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
                    /*if(action == "recovery wipe cache") {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot recovery && twrp wipe cache && twrp wipe dalvik && reboot"});
                    }
                    else{
                        Runtime.getRuntime().exec(new String[]{"su", "-c", action});
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setPreferences(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.apply();
    }
}
