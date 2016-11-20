package com.example.admin.icareapp.BookingTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.icareapp.Controller.Controller;
import com.example.admin.icareapp.MainActivity;
import com.example.admin.icareapp.Model.DatabaseObserver;
import com.example.admin.icareapp.Model.ModelURL;
import com.example.admin.icareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ADMIN on 13-Nov-16.
 */

public class BookingBookFragment extends Fragment implements DatabaseObserver, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener{
    private Controller aController;
    private List<String> timeList, daysList, availableTime;
    private ExpandableListView list;
    private ExpandableListViewAdapter adapter;
    private TimeComparator timeComparator;
    private int day_id, time_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_book, container, false);

        //Get controller
        aController = Controller.getInstance();
        //Create time comparator
        timeComparator = new TimeComparator();
        //Get expandable list
        list = (ExpandableListView) view.findViewById(R.id.expListView);
        //Initialize list
        timeList = new ArrayList<>();
        availableTime = new ArrayList<>();
        daysList = new ArrayList<>();
        list.setOnChildClickListener(this);
        list.setOnGroupClickListener(this);
        //Initialize list adapter
        adapter = new ExpandableListViewAdapter(getActivity(), daysList, availableTime);
        list.setAdapter(adapter);

        //Get days of week and all time in day
        aController.setRequestData(getActivity(), this, ModelURL.SELECT_DAYSOFWEEK.getUrl(), "");
        aController.setRequestData(getActivity(), this, ModelURL.SELECT_ALLTIMEINADAY.getUrl(), "");

        return view;
    }

    @Override
    public void update(Object o) {
        JSONObject status = (JSONObject) o;

        if (status == null) {
            System.out.println("ERROR IN PHP FILE");
            return;
        }

        try {
            if (status.has("Select_DaysOfWeek")) {
                //Receive response from Select_DaysOfWeek.php
                JSONArray days = status.getJSONArray("Select_DaysOfWeek");//Get the array of days' JSONObject
                for (int i = 0; i < days.length(); i++) {
                    JSONObject jOb = (JSONObject) days.get(i);
                    daysList.add(jOb.getString("DAY"));
                }
                adapter.updateGroupList(daysList);
            }else if (status.has("Select_AllTime")) {
                //Receive response from Select_AllTime.php
                JSONArray time = status.getJSONArray("Select_AllTime");//Get the array of time
                for (int i = 0; i < time.length(); i++) {
                    JSONObject jOb = (JSONObject) time.get(i);
                    timeList.add(jOb.getString("TIME"));
                }
            }else if (status.has("Select_SelectedTime")){
                availableTime.clear();
                availableTime.addAll(timeList);
                JSONArray selected = status.getJSONArray("Select_SelectedTime");//Get the array of time
                System.out.println("array " + selected);
                for (int i = 0; i < selected.length(); i++) {
                    JSONObject jOb = (JSONObject) selected.get(i);
                    if (availableTime.contains(jOb.getString("TIME")))
                        availableTime.remove(jOb.getString("TIME"));
                }
                adapter.updateChildList(availableTime);
            }else if (status.has("Select_CheckTimeExistence")){
                if (status.getString("Select_CheckTimeExistence").equals("Exist"))
                    aController.setRequestData(getActivity(), this, ModelURL.UPDATE_CHOSENTIME.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                else
                    aController.setRequestData(getActivity(), this, ModelURL.INSERT_NEWTEMPTIME.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view,  int groupPosition, int childPosition, long childId) {
        if (((MainActivity) getActivity()).numberOfCartItems() == 3) {
            Toast.makeText(getActivity(), getString(R.string.max_item), Toast.LENGTH_LONG).show();
            return false;
        }

        TextView tv = (TextView) view.findViewById(R.id.time_of_day);
        switch (groupPosition){
            case 0://Monday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(0) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 1; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 1://Tuesday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(1) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 2; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 2://Wednesday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(2) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 3; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 3://Thursday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(3) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 4; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 4://Friday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(4) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 5; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 5://Saturday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(5) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 6; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            case 6://Sunday
                ((MainActivity) getActivity()).addSelectedItemToCart(daysList.get(6) + " - " + tv.getText().toString());
                availableTime.remove(childPosition);
                adapter.notifyDataSetChanged();
                day_id = 7; time_id = timeList.indexOf(tv.getText().toString()) + 1;
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_CHECKTIMEEXISTENCE.getUrl(), "day_id=" + day_id + "&time_id=" + time_id);
                break;
            default:
                break;
        }

         return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long groupId) {
         switch (groupPosition){
            case 0://Monday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=1");
                break;
            case 1://Tuesday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=2");
                break;
            case 2://Wednesday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=3");
                break;
            case 3://Thursday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=4");
                break;
            case 4://Friday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=5");
                break;
            case 5://Saturday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=6");
                break;
            case 6://Sunday
                aController.setRequestData(getActivity(), this, ModelURL.SELECT_SELECTEDTIME.getUrl(), "day_id=7");
                break;
            default:
                break;
        }

        return false;
    }

    public void refreshTimeList(String day, String time){
        availableTime.add(time);
        Collections.sort(availableTime, timeComparator);
        adapter.notifyDataSetChanged();
        aController.setRequestData(getActivity(), this, ModelURL.UPDATE_UNCHOSENTIME.getUrl(), "day_id=" + (daysList.indexOf(day) + 1) + "&time_id=" + (timeList.indexOf(time) + 1));
    }
}
