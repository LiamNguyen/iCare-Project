package com.lanthanh.admin.icareapp.presentation.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lanthanh.admin.icareapp.presentation.homepage.MainActivity;

/**
 * @author longv
 * Created on 19-Mar-17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Toast toast;

    public abstract void refreshAfterLosingNetwork();

    public void navigateActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        this.startActivity(intent);
        if (!(this instanceof MainActivity))
            this.finish();
    }

    public void navigateActivity(Class<? extends Activity> activityClass, Bundle b) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(this.getClass().getName(), b); //TODO check this put extra
        this.startActivity(intent);
        if (!(this instanceof MainActivity))
            this.finish();
    }

    public void navigateActivity(Class<? extends Activity> activityClass, String key, Bundle b) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(key, b); //TODO check this put extra
        this.startActivity(intent);
        if (!(this instanceof MainActivity))
            this.finish();
    }

    /**
     * This method is used for hiding soft keyboard if it is visible
     */
    public void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This method is used for showing soft keyboard when needed
     */
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showToast(String msg){
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
