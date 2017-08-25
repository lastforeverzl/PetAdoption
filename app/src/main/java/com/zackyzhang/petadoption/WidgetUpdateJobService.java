package com.zackyzhang.petadoption;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import timber.log.Timber;

/**
 * Created by lei on 8/18/17.
 */

public class WidgetUpdateJobService extends JobService {
    public static final String ACTION_UPDATE_FINISHED = "actionUpdateFinished";

    private JobParameters mJobParameters;

    private BroadcastReceiver updateFinishedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.tag("FirebaseJob").d("broadcast received");
            LocalBroadcastManager.getInstance(context).unregisterReceiver(updateFinishedReceiver);
            onJobFinished();
        }
    };

    private void onJobFinished() {
        jobFinished(mJobParameters, false);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        mJobParameters = job;
        IntentFilter filter = new IntentFilter(ACTION_UPDATE_FINISHED);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateFinishedReceiver , filter);
        FavoritePetService.startActionUpdatePetStatus(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
