package com.ultimustech.cryptowallet.controllers.helpers;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters){
        Log.d(TAG, "Performing long running task in scheduled job");

        //TODO: ADD  THE LONG RUNNING TASK
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters){
        return false;
    }
}
