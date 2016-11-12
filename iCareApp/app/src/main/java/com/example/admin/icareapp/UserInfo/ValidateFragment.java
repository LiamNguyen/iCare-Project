package com.example.admin.icareapp.UserInfo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.icareapp.Controller.Controller;
import com.example.admin.icareapp.Model.DatabaseObserver;
import com.example.admin.icareapp.Model.ModelURL;
import com.example.admin.icareapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ADMIN on 08-Nov-16.
 */

public class ValidateFragment extends Fragment implements View.OnClickListener, DatabaseObserver{
    private Controller aController = Controller.getInstance();
    private TextView noti;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        aController.sendQuery(getActivity(), this, ModelURL.SEND_EMAIL.getUrl(), aController.getUserInfo().getPostEmail());

        View view = inflater.inflate(R.layout.userinfo_validate, container, false);

        ImageButton back = (ImageButton) view.findViewById(R.id.back_button);
        back.setOnClickListener(this);
        AppCompatButton resend = (AppCompatButton) view.findViewById(R.id.ui_resend_email_button);
        resend.setOnClickListener(this);
        AppCompatButton change = (AppCompatButton) view.findViewById(R.id.ui_change_email_button);
        change.setOnClickListener(this);
        noti = (TextView) view.findViewById(R.id.ui_validate_noti);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ui_resend_email_button:
                aController.sendQuery(getActivity(), this, ModelURL.SEND_EMAIL.getUrl(), aController.getUserInfo().getPostEmail());
                break;
            case R.id.ui_change_email_button:
                ((UserInfoActivity) getActivity()).navigateToChangeEmail();
                break;
            case R.id.back_button:
                ((UserInfoActivity) getActivity()).navigateBack();
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Object o) {
        JSONObject status = (JSONObject) o;

        try {
            if (status.has("Send_Email")){
                String result = status.getString("Send_Email");
                if (result.equals("Message has been sent")){
                    noti.setText(R.string.validate_noti_success);
                }else{
                    System.out.println("Message could not be sent");
                    System.out.println(status.getString("ERROR"));
                    noti.setText(R.string.validate_noti_fail);
                }
            }
        } catch (JSONException je){
            System.out.println("Problem with JSON API");
        }
    }
}
