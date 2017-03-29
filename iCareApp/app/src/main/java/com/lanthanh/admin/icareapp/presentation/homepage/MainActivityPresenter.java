package com.lanthanh.admin.icareapp.presentation.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.lanthanh.admin.icareapp.R;
import com.lanthanh.admin.icareapp.data.repository.UserRepositoryImpl;
import com.lanthanh.admin.icareapp.domain.interactor.Interactor;
import com.lanthanh.admin.icareapp.domain.repository.UserRepository;
import com.lanthanh.admin.icareapp.presentation.Function;
import com.lanthanh.admin.icareapp.presentation.homepage.appointmenttab.AppointmentFragment;
import com.lanthanh.admin.icareapp.presentation.homepage.appointmenttab.DefaultAppointmentFragment;
import com.lanthanh.admin.icareapp.presentation.homepage.usertab.UserFragment;
import com.lanthanh.admin.icareapp.presentation.base.BasePresenter;
import com.lanthanh.admin.icareapp.presentation.welcomepage.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 31-Dec-16.
 */

public class MainActivityPresenter extends BasePresenter {
    public static final String TAG = MainActivityPresenter.class.getSimpleName();

    private MainActivity activity;
    private AppointmentFragment appointmentFragment;
    private DefaultAppointmentFragment defaultAppointmentFragment;
    private UserFragment userFragment;

    private UserRepository userRepository;
    private Interactor interactor;

    public MainActivityPresenter(MainActivity activity){
        this.activity = activity;
        init();
    }

    public void init(){
        defaultAppointmentFragment = new DefaultAppointmentFragment();
        appointmentFragment = new AppointmentFragment();
        userFragment = new UserFragment();

        userRepository = new UserRepositoryImpl(this.activity);
        interactor = new Interactor();
    }

    @Override
    public void resume() {

    }

    public void navigateFragment(Class<? extends Fragment> fragmentClass) {
        if (fragmentClass == AppointmentFragment.class || fragmentClass == DefaultAppointmentFragment.class)
            showFragment(defaultAppointmentFragment);
        else if (fragmentClass == UserFragment.class)
            showFragment(userFragment);
    }


    public List<Fragment> getVisibleFragments() {
        // We have 3 fragments, so initialize the arrayList to 3 to optimize memory
        List<Fragment> result = new ArrayList<>(3);

        // Add each visible fragment to the result IF VISIBLE
        if (defaultAppointmentFragment.isVisible()) {
            result.add(defaultAppointmentFragment);
        }
        if (appointmentFragment.isVisible()) {
            result.add(appointmentFragment);
        }
        if (userFragment.isVisible()) {
            result.add(userFragment);
        }

        return result;
    }

    public void hideFragments(FragmentTransaction ft, List<Fragment> visibleFrags) {
        for (Fragment fragment : visibleFrags) {
            ft.hide(fragment);
        }
    }

    public void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                                /*.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                                        R.anim.slide_in_left, R.anim.slide_out_right);*/
        //Hide all current visible fragment
        hideFragments(fragmentTransaction, getVisibleFragments());

        if (!f.isAdded()){
            fragmentTransaction.add(R.id.fragment_container, f, f.getClass().getName());
        }else{
            fragmentTransaction.show(f);
        }

        fragmentTransaction.addToBackStack(null).commit();
    }

    public void navigateActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this.activity, activityClass);
        this.activity.startActivity(intent);
        this.activity.finish();
    }

    public void navigateActivity(Class<? extends Activity> activityClass, Bundle b) {
        Intent intent = new Intent(this.activity, activityClass);
        intent.putExtra(this.getClass().getName(), b); //TODO check this put extra
        this.activity.startActivity(intent);
        this.activity.finish();
    }

    public void populateUserTabOptions(Function.VoidParam notify, List<String> list) {
        interactor.execute(
            () -> userRepository.getUserInformation(),
            user -> {
                if (user != null && user.getName() != null) {
                    list.add(user.getName());
                } else {
                    list.add(this.activity.getString(R.string.user_name));
                }
                list.add(this.activity.getString(R.string.user_option_logout));
                notify.apply();
            },
            error -> {}
        );
    }

    public void logout() {
        interactor.execute(
            () -> userRepository.logout(),
            success -> {
                if (success) {
                    navigateActivity(WelcomeActivity.class);
                }
            },
            error -> {}
        );
    }

    @Override
    public void destroy() {
        super.destroy();
        interactor.dispose();
    }

    //    @Override
//    public void updateAppointmentList() {
//        //Get local appointment list
//        List<DTOAppointment> dtoAppointmentsList = appointmentManager.getLocalAppointmentsFromPref(sharedPreferences, mUser.getID());
//
//        if ( dtoAppointmentsList != null) {
//            appointmentFragment.updateList(dtoAppointmentsList);
//        }else{
//            mView.showFragment(fragmentManager, defaultAppointmentFragment, getVisibleFragments());
//        }
//    }
//
//    @Override
//    public void cancelAppointment(int appointmentId) {
//        mView.showProgress();
//        CancelAppointmentInteractor cancelAppointmentInteractor = new CancelAppointmentInteractorImpl(mExecutor, mMainThread, this, appointmentManager, appointmentId);
//        cancelAppointmentInteractor.execute();

//
//    public void onCancelAppointmentFail() {
//        try {
//            mView.hideProgress();
//            Log.e(TAG, "Cancel appointment fail");
//        }catch (Exception e){
//            Log.w(TAG, e.toString());
//        }
//    }
//
//    public void onCancelAppointmentSuccess(int appointmentId) {
//        try {
//            mView.hideProgress();
//            //Get appointment from local shared pref
//            List<DTOAppointment> appointmentsList = appointmentManager.getLocalAppointmentsFromPref(sharedPreferences, mUser.getID());
//            if (appointmentsList == null)
//                return;
//            //Remove appointment
//            int index = -1;
//            for (int i = 0; i < appointmentsList.size(); i++){
//                if (appointmentsList.get(i).getAppointmentId() == appointmentId){
//                    index = i;
//                    break;
//                }
//            }
//            if (index != -1)
//                appointmentsList.remove(index);
//            //Put to shared pref
//            appointmentManager.saveLocalAppointmentsToPref(sharedPreferences, appointmentsList, mUser.getID());
//            //Update list
//            //updateAppointmentList();
//        }catch (Exception e){
//            Log.w(TAG, e.toString());
//        }
//    }
}
