package com.lanthanh.admin.icareapp.presentation.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanthanh.admin.icareapp.domain.model.DTOAppointment;
import com.lanthanh.admin.icareapp.domain.model.DTOAppointmentSchedule;
import com.lanthanh.admin.icareapp.presentation.converter.ConverterForDisplay;
import com.lanthanh.admin.icareapp.presentation.view.activity.ConfirmBookingActivity;
import com.lanthanh.admin.icareapp.R;
import com.lanthanh.admin.icareapp.presentation.view.fragment.bookingdetails.BookingDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 21-Nov-16.
 */

public class BookingCVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DTOAppointment> list;
    private Activity ctx;
    private Typeface font;
    private FragmentManager fm;

    public BookingCVAdapter(Activity context, List<DTOAppointment> list){
        ctx = context;
        this.list = list;
        font = Typeface.createFromAsset(ctx.getAssets(), "fonts/OpenSans-Light.ttf");//Custom font
        fm = ((AppCompatActivity) context).getSupportFragmentManager();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_booking_details_cardview, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).setName(list.get(position).getCustomerName());
        ((ViewHolder) holder).setVoucher(list.get(position).getVoucherName());
        ((ViewHolder) holder).setStartDate(ConverterForDisplay.convertDateToDisplay(list.get(position).getStartDate()));
        ((ViewHolder) holder).setEndDate(ConverterForDisplay.convertDateToDisplay(list.get(position).getExpireDate()));
        ((ViewHolder) holder).setStatus(list.get(position).getStatus());
        ((ViewHolder) holder).getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt = (TextView) view.findViewById(R.id.status);
                if (txt.getText().toString().equals(ctx.getString(R.string.not_confirm_yet))){
                    Intent toConfirm = new Intent(ctx, ConfirmBookingActivity.class);
                    ctx.startActivity(toConfirm);
                    ctx.finish();
                }else if (txt.getText().toString().equals(ctx.getString(R.string.confirmed))){
                    BookingDetailsFragment frag = new BookingDetailsFragment();

                    //Set information into bundle so that fragment can display
                    Bundle args = new Bundle();

                    args.putString("title", ctx.getString(R.string.bill));
                    args.putString("name", list.get(position).getCustomerName());
                    args.putString("address", list.get(position).getLocationName() + ", " + list.get(position).getDistrictName() + ", " + list.get(position).getCityName() + ", " + list.get(position).getCountryName());
                    args.putString("voucher", list.get(position).getVoucherName());
                    args.putString("type", list.get(position).getTypeName());
                    args.putString("start_date", ConverterForDisplay.convertDateToDisplay(list.get(position).getStartDate()));
                    args.putString("end_date", ConverterForDisplay.convertDateToDisplay(list.get(position).getExpireDate()));
                    args.putString("code", list.get(position).getVerficationCode());
                    ArrayList<String> appointmentScheduleString = new ArrayList<>();
                    for (DTOAppointmentSchedule dtoAppointmentSchedule: list.get(position).getAppointmentScheduleList()){
                        appointmentScheduleString.add(dtoAppointmentSchedule.toString());
                    }
                    args.putStringArrayList("schedules", appointmentScheduleString);

                    //Attach bundle to fragment
                    frag.setArguments(args);

                    //Show fragment
                    frag.show(fm, frag.getClass().getName());
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt1, txt2, txt3, txt4, txt5, txt3title, txt4title;
        private View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //Name
            txt1 = (TextView) itemView.findViewById(R.id.name);
            txt1.setTypeface(font);
            //Voucher
            txt2 = (TextView) itemView.findViewById(R.id.voucher);
            txt2.setTypeface(font);
            //Start date
            txt3title = (TextView) itemView.findViewById(R.id.startdatetitle);
            txt3 = (TextView) itemView.findViewById(R.id.startdate);
            txt3.setTypeface(font);
            //Expire date
            txt4title = (TextView) itemView.findViewById(R.id.expiredatetitle);
            txt4 = (TextView) itemView.findViewById(R.id.expiredate);
            txt4.setTypeface(font);
            //Status
            txt5 = (TextView) itemView.findViewById(R.id.status);
            txt5.setTypeface(font);
        }

        public void setName(String s) {
            txt1.setText(s);
        }

        public void setVoucher(String s) {
            txt2.setText(s);
        }

        public void setStartDate(String s) {
            if (s != null) {
                if (!s.equals("11/11/1111"))
                    txt3.setText(s);
                else{
                    txt3title.setVisibility(View.GONE);
                    txt3.setVisibility(View.GONE);
                }
            }
        }

        public void setEndDate(String s) {
            if (s != null) {
                txt4.setText(s);
                if (txt3.getVisibility() == View.GONE){
                    //If start date = null => one day booking => while hiding start date, move expire date to the right
                    //rename to Ngay thuc hien
                    txt4title.setText(ctx.getString(R.string.booking_do_date));

                    //move to right
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txt4.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    RelativeLayout.LayoutParams params_title = (RelativeLayout.LayoutParams) txt4title.getLayoutParams();
                    params_title.addRule(RelativeLayout.ALIGN_PARENT_START);
                    txt4.setLayoutParams(params);
                    txt4title.setLayoutParams(params_title);
                }
            }
            else {
                System.out.println("Severe: No expire date from BookingCVAdapter class");
            }
        }

        public void setStatus(boolean isConfirmed) {
            if (isConfirmed)
                txt5.setText(ctx.getString(R.string.confirmed));
            else
                txt5.setText(ctx.getString(R.string.not_confirm_yet));
        }

        public View getView(){
            return view;
        }
    }

    public void updateList(List<DTOAppointment> list){
        this.list = list;
        this.notifyDataSetChanged();
    }
}
