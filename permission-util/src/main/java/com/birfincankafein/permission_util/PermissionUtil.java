/*
 * Developed by metehantoksoy.
 * https://github.com/birfincankafein/permission-util
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.birfincankafein.permission_util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Permission-Util is a simple utility that simplifies Android runtime permissions.
 *  Created by metehantoksoy on 9.04.2018.
 */

public class PermissionUtil {
    /**
     * Callback array indexed requestCodeCounter
     */
    private static final SparseArray<onPermissionResultListener> callbacks = new SparseArray<>();

    /**
     * The request code counter that will be use in requesting permissions. Randomize might be dangerous.
     */
    private static int requestCodeCounter = 1000;

    /**
     * Checks the permission and request the permission over fragment if not granted.
     * @param fragment permissions will be checked and requested on this.
     * @param permission permission to check. Must be {@link android.Manifest.permission}
     * @param onPermissionResultListener listener belongs to the this permission request.
     * @see onPermissionResultListener
     * @see android.Manifest.permission
     */
    public static void requestPermission(Fragment fragment, String permission, onPermissionResultListener onPermissionResultListener){
        int requestCode = generateRequestCode();
        callbacks.put(requestCode, onPermissionResultListener);

        if( ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionsResult(requestCode, new String[]{permission}, new int[]{PackageManager.PERMISSION_GRANTED});
        }
        else {
            fragment.requestPermissions(new String[]{permission}, requestCode);
        }
    }

    /**
     * Checks the permissions and request the permissions over fragment if not granted.
     * @param fragment permissions will be checked and requested on this.
     * @param permissions permissions to check. Must be {@link android.Manifest.permission}
     * @param onPermissionResultListener listener belongs to the this permissions request.
     * @see onPermissionResultListener
     * @see android.Manifest.permission
     */
    public static void requestPermissions(Fragment fragment, String[] permissions, onPermissionResultListener onPermissionResultListener){
        int requestCode = generateRequestCode();
        callbacks.put(requestCode, onPermissionResultListener);

        if(isAllPermissionGrantedBefore(fragment.getActivity(), permissions)){
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else{
            fragment.requestPermissions(permissions, requestCode);
        }
    }

    /**
     * Checks the permission and request the permission over fragment if not granted.
     * @param activity permission will be checked and requested on this.
     * @param permission permission to check. Must be {@link android.Manifest.permission}
     * @param onPermissionResultListener listener belongs to the this permissions request.
     * @see onPermissionResultListener
     * @see android.Manifest.permission
     */
    public static void requestPermission(Activity activity, String permission, onPermissionResultListener onPermissionResultListener){
        int requestCode = generateRequestCode();
        callbacks.put(requestCode, onPermissionResultListener);

        if( ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
        else {
            onRequestPermissionsResult(requestCode, new String[]{permission}, new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    /**
     * Checks the permissions and request the permissions over fragment if not granted.
     * @param activity permissions will be checked and requested on this.
     * @param permissions permissions to check. Must be {@link android.Manifest.permission}
     * @param onPermissionResultListener listener belongs to the this permissions request.
     * @see onPermissionResultListener
     * @see android.Manifest.permission
     */
    public static void requestPermissions(Activity activity, String[] permissions, onPermissionResultListener onPermissionResultListener){
        int requestCode = generateRequestCode();
        callbacks.put(requestCode, onPermissionResultListener);

        if(isAllPermissionGrantedBefore(activity, permissions)){
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    /**
     * This function must be triggered from your Activity's {@link Activity#onRequestPermissionsResult(int, String[], int[])} or your Fragment's {@link Fragment#onRequestPermissionsResult(int, String[], int[])}
     *
     * @param requestCode should be passed from your Activity's or Fragment's onRequestPermissionsResult callback.
     * @param permissions should be passed from your Activity's or Fragment's onRequestPermissionsResult callback.
     * @param grantResults should be passed from your Activity's or Fragment's onRequestPermissionsResult callback.
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        onPermissionResultListener listener = callbacks.get(requestCode);
        if(listener != null){
            callbacks.remove(requestCode);
            ArrayList<String> grantedPermissions = new ArrayList<>();
            ArrayList<String> deniedPermissions = new ArrayList<>();
            splitGrantedDeniedPermissions(permissions, grantResults, grantedPermissions, deniedPermissions);
            listener.onPermissionResult(isAllEqual(grantResults, PackageManager.PERMISSION_GRANTED), requestCode, grantedPermissions, deniedPermissions);
        }
    }

    /**
     * Checks for all permissions are already granted.
     * @param activity to checks permissions on this.
     * @param permissions permissions to check grant status
     * @return true if all permissions granted, false otherwise
     */
    private static boolean isAllPermissionGrantedBefore(Activity activity, String[] permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * Simple utility function that checks whole int arrays value equals to something.
     * @param collection array to check
     * @param value value to compare
     * @return true if all array elements equals the value, false otherwise
     */
    private static boolean isAllEqual(int[] collection, int value){
        for(int tmp : collection){
            if(tmp != value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Split all request permissions into granted and denied ones.
     * @param permissions all permissions to split. Comes from Activity's or Fragment's onRequestPermissionsResult callback
     * @param grantResults all grant statuses to split. Comes from Activity's or Fragment's onRequestPermissionsResult callback
     * @param grantedPermissions list to put granted permissions
     * @param deniedPermissions list to put denied permissions
     */
    private static void splitGrantedDeniedPermissions(String[] permissions, int[] grantResults, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions) {
        if(permissions != null && permissions.length == grantResults.length){
            for(int i=0 ; i<permissions.length ; i++ ){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permissions[i]);
                }
                else {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
    }

    /**
     * Generated request code for permission request. Can be randomize but it is not safe.
     * @return next request code
     */
    private static int generateRequestCode(){
        // return new Random().nextInt()/1000 + 1000;
        return ++requestCodeCounter;
    }

    /**
     * Interface for permission result callback.
     */
    public interface onPermissionResultListener {
        void onPermissionResult(boolean isSuccess, int requestCode, ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions);
    }
}
