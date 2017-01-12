package com.lanthanh.admin.icareapp.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lanthanh.admin.icareapp.Controller.NetworkController;
import com.lanthanh.admin.icareapp.R;
import com.lanthanh.admin.icareapp.api.impl.iCareApiImpl;
import com.lanthanh.admin.icareapp.data.manager.impl.CustomerManagerImpl;
import com.lanthanh.admin.icareapp.domain.executor.impl.ThreadExecutor;
import com.lanthanh.admin.icareapp.presentation.presenter.RegisterActivityPresenter;
import com.lanthanh.admin.icareapp.presentation.presenter.UserInfoActivityPresenter;
import com.lanthanh.admin.icareapp.presentation.presenter.impl.UserInfoActivityPresenterImpl;
import com.lanthanh.admin.icareapp.presentation.view.activity.RegisterActivity;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userinfo.ChangeEmailFragment;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userinfo.ContactFragment;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userinfo.DOBvsGenderFragment;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userinfo.NameAndAddressFragment;
import com.lanthanh.admin.icareapp.presentation.view.fragment.userinfo.ValidateFragment;
import com.lanthanh.admin.icareapp.threading.impl.MainThreadImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 22-Oct-16.
 */

public class UserInfoActivity extends AppCompatActivity implements UserInfoActivityPresenter.View{
    public final static int NAME_LOCATION = 0;
    public final static int DOB_GENDER = 1;
    public final static int CONTACT = 2;
    public final static int VALIDATE = 3;
    public final static int CHANGEEMAIL = 4;

    private Toolbar toolBar;
    private NetworkController networkController;
    private UserInfoActivityPresenter userInfoActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        init();

        //Init toolbar
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_48dp);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) toolBar.findViewById(R.id.toolbar_title);
        title.setVisibility(View.GONE);

        userInfoActivityPresenter.navigateFragment(NAME_LOCATION);
    }

    public void init(){
        //Init controllers
        networkController = new NetworkController(this);
        //Init presenter
        userInfoActivityPresenter = new UserInfoActivityPresenterImpl(getSharedPreferences("content", Context.MODE_PRIVATE), ThreadExecutor.getInstance(), MainThreadImpl.getInstance(), this,
                getSupportFragmentManager(), new CustomerManagerImpl(iCareApiImpl.getAPI()));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null){
            if (b.containsKey("fromRegisterActivity"))
                userInfoActivityPresenter.setUsername(b.getString("fromRegisterActivity", ""));
        }
//        SharedPreferences sharedPref = this.getSharedPreferences("content", Context.MODE_PRIVATE);
//        if (!sharedPref.getString("step", "0").equals("0"))
//            navigateToValidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkController.registerNetworkReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkController.unregisterNetworkReceiver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                userInfoActivityPresenter.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public UserInfoActivityPresenter getMainPresenter() {
        return userInfoActivityPresenter;
    }

    @Override
    public void showFragment(FragmentManager fm, Fragment f, List<Fragment> visibleFrags) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                                /*.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                                        R.anim.slide_in_left, R.anim.slide_out_right);*/
        //Hide all current visible fragment
        hideFragments(fragmentTransaction, visibleFrags);

        if (!f.isAdded()){
            fragmentTransaction.add(R.id.ui_fragment_container, f, f.getClass().getName());
        }else{
            fragmentTransaction.show(f);
        }

        fragmentTransaction.addToBackStack(null).commit();
        hideSoftKeyboard();
    }

    @Override
    public void hideFragments(FragmentTransaction ft, List<Fragment> visibleFrags) {
        for (Fragment fragment : visibleFrags) {
            ft.hide(fragment);
        }
    }

    @Override
    public void navigateActivity(Class activityClass) {
        Intent toActivity = new Intent(this, activityClass);
        if (activityClass == MainActivity.class)
            toActivity.putExtra("isSignedIn", 1);
        startActivity(toActivity);
        finish();
        hideSoftKeyboard();
    }

    @Override
    public String getStringResource(int id) {
        return getString(id);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 110);
        toast.show();
    }

    //Hide SoftKeyBoard when needed
    public void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Hide/show tool bar
    public void isToolBarHidden(boolean hidden){
        if (hidden)
            toolBar.setVisibility(View.GONE);
        else
            toolBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        userInfoActivityPresenter.onBackPressed();
    }
}