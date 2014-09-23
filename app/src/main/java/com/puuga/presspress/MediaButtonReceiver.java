package com.puuga.presspress;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by siwaweswongcharoen on 9/10/14 AD.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

    private static final int FM_NOTIFICATION_ID = 0;

    DevicePolicyManager deviceManger;
    ComponentName compName;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Log.i("event", intent.getAction().toString());

        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        int keycode = keyEvent.getKeyCode();
        int action = keyEvent.getAction();
        //Log.i("keycode", String.valueOf(keycode));
        //Log.i("action", String.valueOf(action));

        if (keycode==79 && action ==0) {
            deviceManger = (DevicePolicyManager) context.getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            compName = new ComponentName(context, MyAdmin.class);

            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

                boolean isScreenOn;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    isScreenOn = pm.isInteractive();
                } else {
                    isScreenOn = pm.isScreenOn();
                }

                if (isScreenOn) {
                    Log.i("deviceManger", "now ScreenOn: lock screen");
                    deviceManger.lockNow();
                } else {
                    Log.i("deviceManger", "now ScreenOff: wake screen");
//                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");
//                    wl.acquire();
//                    wl.release();

                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "tag");
                    wl.acquire();
                    wl.release();

                }

            }


        }
    }
}
