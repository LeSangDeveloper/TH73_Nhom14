package android.example.demolistviewplan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.SimpleFormatter;

public class CalendarAdapter extends ArrayAdapter<Date> {
    private Context context;
    ArrayList<String> eventDays;
    ArrayList<Date> dates;
    Activity activity;
    int month;
    int year;
    int startPos;
    int endPos;


    public CalendarAdapter(Activity activity, ArrayList<Date> days, ArrayList<String> eventDays, int month, int year, int startPos, int endPos) {
        super(activity, R.layout.item_date, days);
        this.activity = activity;
        this.dates = days;
        this.eventDays = eventDays;
        this.month = month;
        this.year = year;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public Date getItem(int position)
    {
        return dates.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        // inflate item if it does not exist yet
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_date, parent, false);
        }

        TextView view1 = (TextView)view.findViewById(R.id.txtDate);

        // clear styling
        ((TextView)view1).setTypeface(null, Typeface.NORMAL);
        ((TextView)view1).setTextColor(Color.BLACK);

        if (month != this.month || year != this.year) {
            // if this day is outside current month, grey it out
            ((TextView) view1).setTextColor(Color.parseColor("#aaaaaa"));
            ((TextView)view1).setText(String.valueOf(calendar.get(Calendar.DATE)));
        } else if (day == calendarToday.get(Calendar.DATE) && month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR)) {
            // if it is today
            ((TextView)view1).setTextColor(Color.WHITE);
            ((TextView) view1).setGravity(Gravity.CENTER);
            view.setBackgroundResource(R.drawable.today_round_shape);
            // set text
            ((TextView)view1).setText(String.valueOf(calendar.get(Calendar.DATE)));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String dateString = format.format(calendar.getTime());

            for (String event : eventDays)
            {
                if (event.equals(dateString))
                {
                    ((TextView)view1).setTextColor(Color.parseColor("#e65100"));
                }
            }
        }
        else
        {
            // set text
            ((TextView)view1).setText(String.valueOf(calendar.get(Calendar.DATE)));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String dateString = format.format(calendar.getTime());

            for (String event : eventDays)
            {
                if (event.equals(dateString))
                {
                    ((TextView)view1).setTextColor(Color.parseColor("#e65100"));
                }
            }
        }


        return view;
    }


}
