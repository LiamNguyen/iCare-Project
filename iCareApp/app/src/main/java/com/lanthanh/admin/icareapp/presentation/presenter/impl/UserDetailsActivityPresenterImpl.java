package com.lanthanh.admin.icareapp.presentation.presenter.impl;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.lanthanh.admin.icareapp.R;
import com.lanthanh.admin.icareapp.data.converter.ConverterJson;
import com.lanthanh.admin.icareapp.data.manager.CustomerManager;
import com.lanthanh.admin.icareapp.domain.executor.Executor;
import com.lanthanh.admin.icareapp.domain.interactor.UpdateCustomerInteractor;
import com.lanthanh.admin.icareapp.domain.interactor.impl.UpdateCustomerInteractorImpl;
import com.lanthanh.admin.icareapp.presentation.converter.ConverterForDisplay;
import com.lanthanh.admin.icareapp.presentation.model.ModelUser;
import com.lanthanh.admin.icareapp.presentation.presenter.UserDetailsActivityPresenter;
import com.lanthanh.admin.icareapp.presentation.presenter.base.AbstractPresenter;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userdetails.DobFragment;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userdetails.GenderFragment;
import com.lanthanh.admin.icareapp.threading.MainThread;

/**
 * Created by ADMIN on 11-Jan-17.
 */

public class UserDetailsActivityPresenterImpl extends AbstractPresenter implements UserDetailsActivityPresenter,
             UpdateCustomerInteractor.Callback {
    private UserDetailsActivityPresenter.View mView;
    private FragmentManager fragmentManager;
    private CustomerManager customerManager;
    private SharedPreferences sharedPreferences;
    private ModelUser mUser;
    public UserDetailsActivityPresenterImpl(SharedPreferences sharedPreferences, Executor executor, MainThread mainThread, View view,
                                            FragmentManager fragmentManager, CustomerManager customerManager) {
        super(executor, mainThread);
        mView = view;
        this.sharedPreferences = sharedPreferences;
        this.fragmentManager = fragmentManager;
        this.customerManager = customerManager;
        init();
    }

    public void init(){
        Gson gson = new Gson();
        mUser = gson.fromJson(sharedPreferences.getString("user", ""), ModelUser.class);
    }

    @Override
    public void navigateToMainActivity() {
        mView.navigateToMainActivity();
    }

    @Override
    public void showDobDialogFragment() {
        DobFragment dobFragment = new DobFragment();
        dobFragment.show(fragmentManager, dobFragment.getClass().getName());
    }

    @Override
    public void showGenderDialogFragment() {
        GenderFragment genderFragment = new GenderFragment();
        genderFragment.show(fragmentManager, genderFragment.getClass().getName());
    }

    @Override
    public void resume() {

    }

    @Override
    public void setName(String name) {
        mUser.setName(name);
    }

    @Override
    public void setAddress(String address) {
        mUser.setAddress(address);
    }

    @Override
    public void setDob(String dob) {
        mUser.setDob(dob);
    }

    @Override
    public void setGender(String gender) {
        mUser.setGender(gender);
    }

    @Override
    public void setEmail(String email) {
        mUser.setEmail(email);
    }

    @Override
    public void setPhone(String phone) {
        mUser.setPhone(phone);
    }

    @Override
    public String getName() {
        return mUser.getName();
    }

    @Override
    public String getAddress() {
        return mUser.getAddress();
    }

    @Override
    public String getDob() {
        return ConverterForDisplay.convertStringDateFromDBToDisplay(mUser.getDOB());
    }

    @Override
    public String getGender() {
        return ConverterForDisplay.convertStringGenderFromDBToDisplay(mUser.getGender(), mView.getStringResource(R.string.male), mView.getStringResource(R.string.female));
    }

    @Override
    public String getEmail() {
        return mUser.getEmail();
    }

    @Override
    public String getPhone() {
        return mUser.getPhone();
    }

    @Override
    public void updateCustomer() {
        UpdateCustomerInteractor updateCustomerInteractor = new UpdateCustomerInteractorImpl(mExecutor, mMainThread, this, customerManager, mUser);
        updateCustomerInteractor.execute();
    }

    @Override
    public void onUpdateCustomerFail() {
        onError("Update customer fail");
    }

    @Override
    public void onUpdateCustomerSuccess() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", ConverterJson.convertToJson(mUser));
        editor.apply();
        editor.commit();
        mView.refreshViews();
    }
}