package com.jkcq.base.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class ActivityLifecycleController implements Application.ActivityLifecycleCallbacks {
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList());

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.e("activity", "activity=" + activity.getClass().getSimpleName());
        pushActivity(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        popActivity(activity);
    }

    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    public static void finishAllActivity(String str) {
        List<Activity> list = mActivitys;
        if (list == null) {
            return;
        }
        for (Activity activity : list) {
            if (str != null) {
                Log.e("activity", "className=" + str + "activity = " + activity.getClass().getSimpleName());
                if (!str.equals(activity.getClass().getSimpleName())) {
                    activity.finish();
                }
            } else {
                activity.finish();
            }
        }
        mActivitys.clear();
    }

    public static void finishAllActivity(String str, String str2) {
        List<Activity> list = mActivitys;
        if (list == null) {
            return;
        }
        for (Activity activity : list) {
            if (str != null) {
                if (!str.equals(activity.getClass().getSimpleName()) && !str2.equals(activity.getClass().getSimpleName())) {
                    activity.finish();
                }
            } else {
                activity.finish();
            }
        }
        mActivitys.clear();
    }
}
