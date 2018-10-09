/*
 * Copyright (c) 2018.
 * @author birfincankafein
 *
 */

package com.birfincankafein.permissionutildemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.birfincankafein.permission_util.PermissionUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Initializer{

    private Button mButton_AccessNetworkState;
    private Button mButton_ReadCalendarReadContact;
    private Button mButton_WriteExternalStorage;
    private Button mButton_FragmentExample;
    private Button mButton_AppSettings;
    private TextView mTextView_LatestResult;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews(null);
        initializeVariables();
        initializeViews();
    }

    @Override
    public void findViews(View view) {
        mTextView_LatestResult = findViewById(R.id.textView_latest_result);
        mButton_AccessNetworkState = findViewById(R.id.button_permission_access_network_state);
        mButton_ReadCalendarReadContact = findViewById(R.id.button_permission_read_calendar_read_contacts);
        mButton_WriteExternalStorage = findViewById(R.id.button_permission_write_external_storage);
        mButton_FragmentExample = findViewById(R.id.button_check_fragment);
        mButton_AppSettings = findViewById(R.id.button_app_settings);
    }

    @Override
    public void initializeVariables() {
        final Activity finalActivity = this;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_permission_access_network_state:
                        PermissionUtil.requestPermission(finalActivity, Manifest.permission.ACCESS_NETWORK_STATE, new PermissionUtil.onPermissionResultListener() {
                            @Override
                            public void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
                                mTextView_LatestResult.setText(String.format("%s : %s", Manifest.permission.ACCESS_NETWORK_STATE, isSuccess));
                            }
                        });
                        break;
                    case R.id.button_permission_read_calendar_read_contacts:
                        PermissionUtil.requestPermissions(finalActivity, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS}, new PermissionUtil.onPermissionResultListener() {
                            @Override
                            public void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
                                StringBuilder toSet = new StringBuilder(String.format("%s/%s : %s", Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS, isSuccess));
                                for(String grantedPermission : grantedPermissions){
                                    toSet.append("\nGranted: ").append(grantedPermission);
                                }
                                for(String deniedPermission : deniedPermissions){
                                    toSet.append("\nDenied: ").append(deniedPermission);
                                }
                                mTextView_LatestResult.setText(toSet);
                            }
                        });
                        break;
                    case R.id.button_permission_write_external_storage:
                        PermissionUtil.requestPermission(finalActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.onPermissionResultListener() {
                            @Override
                            public void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
                                mTextView_LatestResult.setText(String.format("%s : %s", Manifest.permission.WRITE_EXTERNAL_STORAGE, isSuccess));
                            }
                        });
                        break;
                    case R.id.button_check_fragment:
                        startFragmentExample();
                        break;
                    case R.id.button_app_settings:
                        openAppSettings();
                        break;
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void initializeViews() {
        mButton_AccessNetworkState.setOnClickListener(onClickListener);
        mButton_ReadCalendarReadContact.setOnClickListener(onClickListener);
        mButton_WriteExternalStorage.setOnClickListener(onClickListener);
        mButton_FragmentExample.setOnClickListener(onClickListener);
        mButton_FragmentExample.setOnClickListener(onClickListener);
        mButton_FragmentExample.setOnClickListener(onClickListener);
        mButton_AppSettings.setOnClickListener(onClickListener);
    }

    private void startFragmentExample(){
        Intent fragmentActivityIntent = new Intent(this, FragmentActivity.class);
        startActivity(fragmentActivityIntent);
    }

    private void openAppSettings(){
        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingsIntent.setData(Uri.fromParts("package" , BuildConfig.APPLICATION_ID, null));
        startActivity(settingsIntent);

    }
}
