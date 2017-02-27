package com.sparrowred.advancedbootoptions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private boolean isRooted;
    private ImageButton image;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String rooted = "isPhoneRooted";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        Boolean isPhoneRooted = sharedpreferences.getBoolean(rooted,false);
        if(isPhoneRooted){
            startNewActivity(isPhoneRooted);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tollbar_title);
        setSupportActionBar(toolbar);

        image = (ImageButton) findViewById(R.id.imageView2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRooted = checkSu();
                if(isRooted){
                    image.setImageResource(R.drawable.root_true);
                }
                else{
                    image.setImageResource(R.drawable.root_false);
                }
                startNewActivity(isRooted);
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
        setPreferences();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean checkSu(){
        return isPhoneRooted() || hasSuperuserApkfile() || isTestKeyBuild();
    }

    public boolean isPhoneRooted(){
        java.lang.Process process = null;
        try{
            process = Runtime.getRuntime().exec("su");
            return true;
        } catch (Exception e) {
            return false;
        } finally{
            if(process != null){
                try{
                    process.destroy();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean hasSuperuserApkfile(){
        return new File("/system/app/Superuser.apk").exists();
    }

    private boolean isTestKeyBuild(){
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private void startNewActivity(boolean rooted){
        if(rooted) {
            Intent intent = new Intent(getBaseContext(), Options.class);
            intent.putExtra("isRooted",rooted);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Your phone doesn't seem to be rooted. There is no need to use this app. The app will auto close", Toast.LENGTH_LONG).show();
            onDestroy();
        }
    }

    public void setPreferences(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.apply();
    }
}
