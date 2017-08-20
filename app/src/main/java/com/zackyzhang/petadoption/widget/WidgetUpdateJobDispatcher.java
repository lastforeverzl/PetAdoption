package com.zackyzhang.petadoption.widget;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.zackyzhang.petadoption.WidgetUpdateJobService;

import java.util.concurrent.TimeUnit;

/**
 * Created by lei on 8/19/17.
 */

public class WidgetUpdateJobDispatcher {

    private static final int UPDATE_INTERVAL_HOURS = 24;
    private static final int UPDATE_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(UPDATE_INTERVAL_HOURS);
    private static final int UPDATE_FLEXTIME_SECONDS = (int) TimeUnit.HOURS.toSeconds(1);

    private static final String WIDGET_UPDATE_TAG = "update_favorite_adoption_info";

    synchronized public static void scheduleFirebaseJobDispatcher(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job updateAdoptionInfoJob = dispatcher.newJobBuilder()
                .setService(WidgetUpdateJobService.class)
                .setTag(WIDGET_UPDATE_TAG)
                .setRecurring(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(
                        UPDATE_INTERVAL_SECONDS,
                        UPDATE_INTERVAL_SECONDS + UPDATE_FLEXTIME_SECONDS))
//                .setTrigger(Trigger.executionWindow(0, 60)) // For test purpose.
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(updateAdoptionInfoJob);
    }
}
