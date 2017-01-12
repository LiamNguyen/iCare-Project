package com.lanthanh.admin.icareapp.domain.interactor.impl;

import com.lanthanh.admin.icareapp.data.manager.TimeManager;
import com.lanthanh.admin.icareapp.domain.executor.Executor;
import com.lanthanh.admin.icareapp.domain.interactor.GetAllSelectedTimeInteractor;
import com.lanthanh.admin.icareapp.domain.interactor.base.AbstractInteractor;
import com.lanthanh.admin.icareapp.domain.model.DTOTime;
import com.lanthanh.admin.icareapp.threading.MainThread;

import java.util.List;

/**
 * Created by ADMIN on 07-Jan-17.
 */

public class GetAllSelectedTimeInteractorImpl extends AbstractInteractor implements GetAllSelectedTimeInteractor {
    private GetAllSelectedTimeInteractor.Callback mCallback;
    private TimeManager mTimeManager;
    private int dayId;

    public GetAllSelectedTimeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, TimeManager timeManager, int dayId){
        super(executor, mainThread);
        mCallback = callback;
        mTimeManager = timeManager;
        this.dayId = dayId;
    }

    @Override
    public void run() {
        final List<DTOTime> selectedTimeList = mTimeManager.getAllSelectedTime(dayId);
        if (selectedTimeList == null){
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onNoSelectedTimeFound();
                }
            });
        }else{
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSelectedTimeReceive(selectedTimeList);
                }
            });
        }
    }
}