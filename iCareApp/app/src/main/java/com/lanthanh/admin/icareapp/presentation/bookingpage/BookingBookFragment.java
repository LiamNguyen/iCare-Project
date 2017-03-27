package com.lanthanh.admin.icareapp.presentation.bookingpage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.lanthanh.admin.icareapp.presentation.application.ApplicationProvider;
import com.lanthanh.admin.icareapp.presentation.base.BaseFragment;
import com.lanthanh.admin.icareapp.presentation.converter.TimeComparator;
import com.lanthanh.admin.icareapp.R;
import com.lanthanh.admin.icareapp.presentation.model.dto.DTOMachine;
import com.lanthanh.admin.icareapp.presentation.adapter.CustomSpinnerAdapter;
import com.lanthanh.admin.icareapp.presentation.adapter.ExpandableListViewAdapter;
import com.lanthanh.admin.icareapp.utils.GraphicUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ADMIN on 13-Nov-16.
 */

public class BookingBookFragment extends BaseFragment<BookingActivityPresenterImpl> implements AdapterView.OnItemSelectedListener{
    @BindView(R.id.booking_finish_button) AppCompatButton finishButton;
    @BindView(R.id.spinner_machine) Spinner machineSpinner;
    @BindView(R.id.expListView) ExpandableListView expandableListView;

    private ExpandableListViewAdapter adapter;
    private TimeComparator timeComparator;
    private CustomSpinnerAdapter machineAdapter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_book, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();

        timeComparator = new TimeComparator();
        getMainPresenter().getMachinesByLocationId(getProvider().getCurrentAppointment().getLocation().getLocationId());
        getMainPresenter().getWeekDays();
        getMainPresenter().getAllTime();

        return view;
    }

    @Override
    public void initViews() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GraphicUtils.FONT_LIGHT);//Custom font
        finishButton.setTypeface(font);

        finishButton.setOnClickListener(
            view ->{
                if (this.getProvider().getCurrentAppointment().getAppointmentScheduleList().size() <= 0){
                    showToast(getString(R.string.min_item));
                }else {
                    //getMainPresenter()
                    //bookingActivityPresenter.insertAppointment();
                }
        });
        finishButton.setEnabled(false);

        /*========================= MACHINE SPINNER =========================*/
        machineAdapter = new CustomSpinnerAdapter<>(getActivity(), R.layout.bookingselect_spinner_item, getProvider().getMachines(), getString(R.string.booking_machine_hint));
        machineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machineSpinner.setAdapter(machineAdapter);
        machineSpinner.setSelection(0, false);
        machineSpinner.setOnItemSelectedListener(this);

        /*========================= EXPANDABLE LIST =========================*/
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnChildClickListener(
                (ExpandableListView expandableListView, View view,  int groupPosition, int childPosition, long childId) -> {
                if (getProvider().getCurrentAppointment().getAppointmentScheduleList().size() == 3) {
                    showToast(getString(R.string.max_item));
                    return false;
                }

                getMainPresenter().bookTime(adapter.getGroup(groupPosition), adapter.getChild(groupPosition, childPosition));

                expandableListView.collapseGroup(groupPosition);

                return true;
            }
        );
        expandableListView.setOnGroupClickListener(
            (ExpandableListView expandableListView, View view, int groupPosition, long groupId) -> {
                //Check whether machine has been selected. If not selected
                if (!getProvider().getCurrentAppointment().isMachineFilled()){
                    showToast(getString(R.string.machine_alert));
                    return true;
                }
                //If selected (currently the selected item of group is closing)
                if (!expandableListView.isGroupExpanded(groupPosition)) {
                    expandGroup(groupPosition, false);
                }

                return false;
            }
        );
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            //This method will close opening group to expand another group (only one group expand at a time)
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expandableListView.collapseGroup(previousItem );
                previousItem = groupPosition;
                expandableListView.setSelectedGroup(groupPosition);
            }
        });
        //Initialize list adapter
        adapter = new ExpandableListViewAdapter(getActivity(), getProvider().getWeekDays(), getProvider().getAllTime());
        expandableListView.setAdapter(adapter);

    }

    @Override
    public void resetViews() {}

    @Override
    public BookingActivityPresenterImpl getMainPresenter() {
        return ((BookingActivity) getActivity()).getMainPresenter();
    }

    @Override
    public ApplicationProvider getProvider() {
        return ((BookingActivity) getActivity()).getProvider();
    }

//    public void setAvailableDay(List<String> list) {
//        adapter.updateGroupList(list);
//    }
//
//    public void setAvailableTime(List<String> list) {
//        Collections.sort(list, timeComparator);
//        adapter.updateChildList(list);
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spinner_machine:
                getProvider().getCurrentAppointment().getCurrentSchedule().setBookedMachine((DTOMachine) machineSpinner.getSelectedItem());
                collapseAllGroups();
                expandGroup(0, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void expandGroup(int groupPosition, boolean isAuto) {
        getMainPresenter().getSelectedTime(
            adapter.getGroup(groupPosition).getDayId(),
            getProvider().getCurrentAppointment().getLocation().getLocationId(),
            getProvider().getCurrentAppointment().getCurrentSchedule().getBookedMachine().getMachineId()
        );
        if (isAuto) {
            expandableListView.expandGroup(groupPosition);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && isVisible()) {
           //bookingBookPresenter.resume();
        }
        else
            collapseAllGroups();
    }

    public void collapseAllGroups(){
        int count =  adapter.getGroupCount();
        for (int i = 0; i <count ; i++)
            expandableListView.collapseGroup(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
