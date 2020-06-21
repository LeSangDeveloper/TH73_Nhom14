package android.example.demolistviewplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateActivity extends AppCompatActivity {

    ConstraintLayout ActivityView;
    ArrayList<String> eventDays;
    ConstraintLayout constraintLayout;

    LinearLayout header;
    Button btnToday;
    ImageView btnPrev;
    ImageView btnNext;
    TextView txtDisplayMonth;
    TextView txtDateYear;
    GridView gridView;
    ArrayList<Date> cells;
    CalendarAdapter calendarAdapter;

    String dateIntent, monthIntent, yearIntent;

    int DAYS_COUNT = 7 * 4;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        eventDays = new ArrayList<String>();

        eventDays = MainActivity.eventDays;

        constraintLayout = (ConstraintLayout)findViewById(R.id.constraint);
        assignUiElements();
        cells = new ArrayList<>();

        Intent intent = getIntent();

        updateCalendar(eventDays, Integer.parseInt(intent.getStringExtra("year")), Integer.parseInt(intent.getStringExtra("month")) - 1,  Integer.parseInt(intent.getStringExtra("date")));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(txtDateYear.getText().toString());
                String monthString = txtDisplayMonth.getText().toString();
                int month = convertToMonth(monthString);

                if (month == 12)
                {
                    year += 1;
                    month = 1;
                }
                else {
                    month += 1;
                }
                updateCalendar(eventDays, year, month - 1,  13);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(txtDateYear.getText().toString());
                String monthString = txtDisplayMonth.getText().toString();
                int month = convertToMonth(monthString);

                if (month == 1)
                {
                    year -= 1;
                    month = 12;
                }
                else {
                    month -= 1;
                }
                updateCalendar(eventDays, year, month - 1,  13);
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateToday = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd");
                dateIntent = format.format(dateToday);
                format = new SimpleDateFormat("MM");
                int monthTemp = Integer.parseInt(format.format(dateToday)) + 1;
                monthIntent = String.valueOf(monthTemp);
                format = new SimpleDateFormat("yyyy");
                yearIntent = format.format(dateToday);
                finish();
            }
        });

    }

    public void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDateYear = findViewById(R.id.date_display_year);
        txtDisplayMonth = findViewById(R.id.date_display_month);
        btnToday = findViewById(R.id.date_display_today);
        gridView = (GridView)findViewById(R.id.calendar_grid);
    }

    public void updateCalendar(ArrayList<String> events, int year, int month, int date)
    {
        int startPos = 0, endPos = 0;
        cells.clear();
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(year, month, date);
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int monthBeginningCell = 0;
        int dayOfWeekBegin = calendar.get(Calendar.DAY_OF_WEEK);
        int currentMonth = calendar.get(Calendar.MONTH);

        monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeekBegin == 1 || (dayOfWeekBegin == 7 && (currentMonth == 3 || currentMonth == 5 || currentMonth == 8 || currentMonth == 10))) {
            DAYS_COUNT = 7 * 6;
        }
        else
            DAYS_COUNT = 7 * 5;

        // move calendar backwards to the beginning of the week
        if (dayOfWeekBegin == 1)
        {
            calendar.add(Calendar.DAY_OF_MONTH, -6);
        }
        else {
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        }
        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int monthInt = calendar.get(Calendar.MONTH);

            if (currentMonth < monthInt)
            {
                endPos = cells.size() - 1;
                currentMonth++;
            }
        }


        // update grid
        calendarAdapter = new CalendarAdapter(this, cells, events, month, year, startPos, endPos);
        gridView.setAdapter(calendarAdapter);

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MM,yyyy");
        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
        txtDisplayMonth.setText(dateToday[1]);
        txtDateYear.setText(dateToday[2]);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= calendarAdapter.startPos && position <= calendarAdapter.endPos) {
                    Date date = calendarAdapter.getItem(position);

                    SimpleDateFormat format = new SimpleDateFormat("dd");
                    dateIntent = format.format(date);

                    int month = convertToMonth(txtDisplayMonth.getText().toString());

                    monthIntent = String.format("%d", month + 1);
                    yearIntent = String.format(txtDateYear.getText().toString());
                    finish();
                }
            }
        });

    }

    private int convertToMonth(String monthString)
    {
        int month = 0;
        switch (monthString)
        {
            case "01":
                month = 1;
                break;
            case "02":
                month = 2;
                break;
            case "03":
                month = 3;
                break;
            case "04":
                month = 4;
                break;
            case "05":
                month = 5;
                break;
            case "06":
                month = 6;
                break;
            case "07":
                month = 7;
                break;
            case "08":
                month = 8;
                break;
            case "09":
                month = 9;
                break;
            case "10":
                month = 10;
                break;
            case "11":
                month = 11;
                break;
            case "12":
                month = 12;
                break;
        }

        return month;
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("date", dateIntent);
        data.putExtra("month", monthIntent);
        data.putExtra("year", yearIntent);
        setResult(RESULT_OK, data);
        super.finish();
    }

}