package com.example.admin.icareapp.UserInfo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.admin.icareapp.Controller.Controller;
import com.example.admin.icareapp.R;

/**
 * Created by ADMIN on 22-Oct-16.
 */

public class DOBvsGenderFragment extends Fragment implements DatePicker.OnDateChangedListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    private Controller aController;
    private TextView dob_noti;
    private TextView gender_noti;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userinfo_dobvsgender, container, false);

        DatePicker dPicker = (DatePicker) view.findViewById(R.id.datePicker);
        dPicker.init(1998, 0, 1, this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.ui_radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.ui_next_button_p2) ;
        button.setOnClickListener(this);
        ImageButton back = (ImageButton) view.findViewById(R.id.back_button);
        back.setOnClickListener(this);
        dob_noti = (TextView) view.findViewById(R.id.ui_dob_noti);
        gender_noti = (TextView) view.findViewById(R.id.ui_gender_noti);

        aController = Controller.getInstance();

        return view;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dob_noti.setVisibility(View.INVISIBLE);
        aController.getUserInfo().addInfo("dob", Integer.toString(year) + "-" + Integer.toString(monthOfYear) + "-" + Integer.toString(dayOfMonth));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        gender_noti.setVisibility(View.INVISIBLE);
        switch (checkedId){
            case R.id.ui_male:
                aController.getUserInfo().addInfo("gender", "Male");
                break;
            case R.id.ui_female:
                aController.getUserInfo().addInfo("gender", "Female");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ui_next_button_p2:
                if (aController.getUserInfo().isInfoExist("dob") && aController.getUserInfo().isInfoExist("gender")){
                    ((UserInfoActivity) getActivity()).navigateToContact();
                }else {
                    if (!aController.getUserInfo().isInfoExist("dob")){
                        dob_noti.setVisibility(View.VISIBLE);
                    }
                    if (!aController.getUserInfo().isInfoExist("gender")){
                        gender_noti.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.back_button:
                ((UserInfoActivity) getActivity()).navigateBack();
                break;
            default:
                break;
        }
    }
}
