package com.birfincankafein.permissionutildemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.birfincankafein.permission_util.PermissionUtil;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentActivityFragment extends Fragment implements Initializer {

    private Button mButton_AccessNetworkState;
    private Button mButton_ReadCalendarReadContact;
    private Button mButton_WriteExternalStorage;
    private Button mButton_AppSettings;
    private TextView mTextView_LatestResult;
    private View.OnClickListener onClickListener;

    public FragmentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initializeVariables();
        initializeViews();
    }

    @Override
    public void findViews(View view) {
        mTextView_LatestResult = view.findViewById(R.id.textView_latest_result);
        mButton_AccessNetworkState = view.findViewById(R.id.button_permission_access_network_state);
        mButton_ReadCalendarReadContact = view.findViewById(R.id.button_permission_read_calendar_read_contacts);
        mButton_WriteExternalStorage = view.findViewById(R.id.button_permission_write_external_storage);
        mButton_AppSettings = view.findViewById(R.id.button_app_settings);
    }

    @Override
    public void initializeVariables() {
        final Fragment finalFragment = this;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_permission_access_network_state:
                        PermissionUtil.requestPermission(finalFragment, Manifest.permission.ACCESS_NETWORK_STATE, new PermissionUtil.onPermissionResultListener() {
                            @Override
                            public void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
                                mTextView_LatestResult.setText(String.format("%s : %s", Manifest.permission.ACCESS_NETWORK_STATE, isSuccess));
                            }
                        });
                        break;
                    case R.id.button_permission_read_calendar_read_contacts:
                        PermissionUtil.requestPermissions(finalFragment, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS}, new PermissionUtil.onPermissionResultListener() {
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
                        PermissionUtil.requestPermission(finalFragment, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.onPermissionResultListener() {
                            @Override
                            public void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
                                mTextView_LatestResult.setText(String.format("%s : %s", Manifest.permission.WRITE_EXTERNAL_STORAGE, isSuccess));
                            }
                        });
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
        mButton_AppSettings.setOnClickListener(onClickListener);
    }

    private void openAppSettings(){
        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingsIntent.setData(Uri.fromParts("package" , BuildConfig.APPLICATION_ID, null));
        startActivity(settingsIntent);

    }
}
