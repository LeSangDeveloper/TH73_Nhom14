package android.example.demolistviewplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Work> works;
    private List<Work> workAdapter;

    private RecyclerView listWorks;
    private RecyclerViewWorks adapter;

    private WorksDataSource source;
    private TextView txtDate;

    private NotificationManager mNotificationManager;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    public static ArrayList<String> eventDays;

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignAndConstruct();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getBoolean(
                IntroductionActivity.COMPLETED_ONBOARDING_PREF_NAME, false))
        {
            Intent intent = new Intent(this, IntroductionActivity.class);
            startActivity(intent);
        }

        setTodayText(savedInstanceState);

        eventDays = new ArrayList<String>();

        source.open();
        works = source.getAllWork();

        Collections.sort(works, Work.workSort);

        setWorksToDay();

        setEventsDay();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listWorks.setLayoutManager(layoutManager);
        listWorks.setHasFixedSize(true);
        adapter = new RecyclerViewWorks(this, workAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listWorks);
        listWorks.setAdapter(adapter);

        gestureDetector = new GestureDetector(this, new DateGestureDetector());

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.instruction:
                Intent intent = new Intent(this, InstructionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setEventsDay()
    {
        eventDays.clear();
        for (Work work: works)
        {
            if (!eventDays.contains(work.getDate()))
            {
                eventDays.add(work.getDate());
            }
        }
    }

    private void assignAndConstruct()
    {
        txtDate = (TextView)findViewById(R.id.txtDate);
        listWorks = (RecyclerView)findViewById(R.id.listWorks);
        workAdapter = new ArrayList<Work>();
        source = new WorksDataSource(this);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            getString(R.string.channelTitleDes),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (getString(R.string.channelTitleDes));
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public String getToday()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    private void setTodayText(Bundle savedInstanceState)
    {
        String dateMessage = getToday();
        txtDate.setText(dateMessage);

        if (savedInstanceState != null)
        {
            txtDate.setText(savedInstanceState.getString("txtDate"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("txtDate", txtDate.getText().toString());
    }

    public void setWorksToDay()
    {
        workAdapter.clear();
        for (Work work:works)
        {
            if (work.getDate().equals(txtDate.getText().toString()))
            {
                workAdapter.add(work);
            }
        }
    }

    // gán cho floating button
    public void addWork(View view)
    {
        Intent intent = new Intent(this, AddWorkActivity.class);

        int REQUEST_CODE = 9;
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setAlarm(int id,String title ,String time, String date, int before) throws ParseException {
        long countDate = 0, countHour = 0, countMinute = 0;
        String today = getToday();

        String dateString[] = date.split("/");
        int dateEnd = Integer.valueOf(dateString[0]);
        int monthEnd = Integer.valueOf(dateString[1]);
        int yearEnd = Integer.valueOf(dateString[2]);

        String timeString[] = time.split(":");

        int hour = Integer.valueOf(timeString[0]);
        int minute = Integer.valueOf(timeString[1]);

        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.set(yearEnd, monthEnd - 1, dateEnd, hour, minute);
        long count = endTime.getTimeInMillis() - startTime.getTimeInMillis() - before * 60 * 1000;

        Intent notifyIntent = new Intent(this, WorkAlarmReceiver.class);
        notifyIntent.putExtra("id", String.valueOf(id));
        notifyIntent.putExtra("time", time);
        notifyIntent.putExtra("title", title);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + count, notifyPendingIntent);
    }

    private void cancelAlarm(int id)
    {
        Intent notifyIntent = new Intent(this, WorkAlarmReceiver.class);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(notifyPendingIntent);
    }

    private boolean checkWorkTime(Work work)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateString = formatter.format(date);
        formatter = new SimpleDateFormat("HH:mm");
        Date time = new Date();
        return (RecyclerViewWorks.compareDateWork(dateString, formatter.format(time), work));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String hour = null;
        String title = null;
        String location = null;
        String notification = null;
        int before = 0;

        if (resultCode == RESULT_OK && requestCode == 9) {

            hour = data.getExtras().getString("hour");
            title = data.getExtras().getString("work");
            location = data.getExtras().getString("location");
            notification = data.getExtras().getString("notification");

            if (data.hasExtra("hour")) {
                if (data.getExtras().getString("activity").equalsIgnoreCase("add")) {
                    if (!(hour.isEmpty() || title.isEmpty() || location.isEmpty())){

                        if (!notification.equalsIgnoreCase("none")) {
                            before = Integer.valueOf(notification);
                            notification = "before " + notification + " minute";
                        }

                        Work temp = new Work(title, txtDate.getText().toString(), hour, location, notification);

                        if (checkWorkTime(temp)) {
                            temp = source.createWork(title, txtDate.getText().toString(), hour, location, notification);
                            int n = workAdapter.size();

                            works.add(temp);

                            workAdapter.add(n, temp);
                            adapter.notifyItemInserted(n);

                            if (!notification.contains("none")) {
                                try {
                                    setAlarm((int) temp.getId(), title, hour, txtDate.getText().toString(), before);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Đừng thay đổi những gì đã qua!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    if (!(hour.isEmpty() || title.isEmpty() || location.isEmpty())) {
                        String indexOfWorks = data.getStringExtra("indexOfWorks");
                        String indexOfWorkAdapter = data.getStringExtra("indexOfWorkAdapter");

                        if (!notification.equalsIgnoreCase("none")) {
                            notification = "before " + notification + " minute";
                        }

                        String id = data.getStringExtra("id");

                        Log.e("id ", id);

                        Work temp = new Work(title, txtDate.getText().toString(), hour, location, notification);

                        temp.setId(Long.parseLong(id));

                        if (checkWorkTime(temp))
                        {
                            source.saveWork(temp);
                            works.remove(Integer.parseInt(indexOfWorks));
                            works.add(Integer.parseInt(indexOfWorks), temp);
                            workAdapter.remove(Integer.parseInt(indexOfWorkAdapter));
                            adapter.notifyItemRemoved(Integer.parseInt(indexOfWorkAdapter));
                            workAdapter.add(Integer.parseInt(indexOfWorkAdapter), temp);
                            adapter.notifyItemChanged(Integer.parseInt(indexOfWorkAdapter));

                            if (!notification.contains("none")) {
                                try {
                                    setAlarm((int) temp.getId(), title, hour, txtDate.getText().toString(), before);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Đừng thay đổi những gì đã qua!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                Collections.sort(works, Work.workSort);
                Collections.sort(workAdapter, Work.workSort);
                adapter.notifyDataSetChanged();
                setEventsDay();
            }
        }

            if (resultCode == RESULT_OK && requestCode == 10) {

                String date, month, year;
                date = data.getStringExtra("date");
                month = data.getStringExtra("month");
                year = data.getStringExtra("year");

                if (date != null && month != null && year != null) {
                    processDatePickerResult(date + "/" + month + "/" + year);
                    Collections.sort(works, Work.workSort);
                    Collections.sort(workAdapter, Work.workSort);
                    adapter.notifyDataSetChanged();
                    setWorksToDay();
                }
            }
    }

    final ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

                private boolean deleteButtonVisible = false;
                private boolean editButtonVisible = false;
                private int posSwiped = -1;
                private int lastSwipe = -1;
                private boolean moving = false;
                private float buttonEditLeft = -1;
                private float buttonEditRight = -1;
                private float buttonDeleteLeft= -1;
                private float buttonDeleteRight = -1;

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    moving = true;
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    moving = false;
                    int position = viewHolder.getLayoutPosition();

                    // da dong item wipe truoc
                    if (lastSwipe != -1 && lastSwipe != position)
                        adapter.notifyItemChanged(lastSwipe);

                    lastSwipe = position;
                    deleteButtonVisible = true;
                    editButtonVisible = true;
                }

                @Override
                public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    posSwiped = viewHolder.getAdapterPosition();

                    View view = viewHolder.itemView;

                    // Tao doi tuong paint de ve
                    Paint paint = new Paint();
                    paint.setColor(getResources().getColor(R.color.colorAccent));
                    paint.setTextSize(70f);
                    paint.setAntiAlias(true);

                    //fix position cho button Delete
                    float deleteButtonLeft = view.getRight() - (view.getRight() / 3f);
                    float deleteButtonTop = view.getTop();
                    float deleteButtonRight = view.getRight() - view.getPaddingRight();
                    float deleteButtonBottom = view.getBottom();

                    buttonDeleteLeft = deleteButtonLeft;
                    buttonDeleteRight = deleteButtonRight;

                    //fix position cho button Edit
                    float editButtonLeft = deleteButtonLeft - (deleteButtonRight - deleteButtonLeft);
                    float editButtonTop = view.getTop();
                    float editButtonRight = deleteButtonLeft - 3f;
                    float editButtonBottom = view.getBottom();

                    buttonEditLeft = editButtonLeft;
                    buttonEditRight = editButtonRight;

                    // Ve button Delete
                    float radius = 15f;
                    RectF deleteButtonDelete = new RectF(deleteButtonLeft, deleteButtonTop, deleteButtonRight, deleteButtonBottom);
                    c.drawRoundRect(deleteButtonDelete, radius, radius, paint);

                    //Ve button Edit
                    RectF editButtonEdit = new RectF(editButtonLeft, editButtonTop, editButtonRight, editButtonBottom);
                    c.drawRoundRect(editButtonEdit, radius, radius, paint);


                    // chinh color cho text
                    paint.setColor(getResources().getColor(R.color.colorWhite));

                    //ching noi dung text
                    String textButtonDelete = "Delete";
                    String textButtonEdit = "Edit";

                    // Lay width, height
                    Rect rect = new Rect();
                    paint.getTextBounds(textButtonDelete, 0, textButtonDelete.length(), rect);

                    // chinh lai ten trong va center button
                    c.drawText("Delete", deleteButtonDelete.centerX() - rect.width() / 2f, deleteButtonDelete.centerY() + rect.height() / 2f, paint);

                    //
                    paint.getTextBounds(textButtonEdit, 0, textButtonEdit.length(), rect);

                    // chinh lai ten trong va center button
                    c.drawText("Edit", editButtonEdit.centerX() - rect.width() / 2f, editButtonEdit.centerY() + rect.height() / 2f, paint);

                    // dX of item run from 0 to '-X' wwidth of screen
                    if (dX <= - editButtonLeft)
                    {
                        deleteButtonVisible = true;
                        editButtonVisible = true;
                        moving = false;
                    }
                    else
                    {
                        deleteButtonVisible = false;
                        editButtonVisible = false;
                        moving = true;
                    }

                    if(dX == 0.0f)
                    {
                        moving = false;
                    }

                    if(deleteButtonVisible && editButtonVisible)
                    {
                        clickSwipedButtonListener(recyclerView, viewHolder, posSwiped);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX/1.75f, dY, actionState, isCurrentlyActive);
                }

                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    return makeMovementFlags(0, ItemTouchHelper.START);
                }

                public void clickDeleteButton(int posSwiped)
                {

                    Work work = workAdapter.get(posSwiped);

                    if (checkWorkTime(work)) {
                        source.deleteWork(work);
                        works.remove(work);
                        workAdapter.remove(posSwiped);
                        adapter.notifyItemChanged(posSwiped);
                        cancelAlarm((int) work.getId());
                        setEventsDay();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Đừng thay đổi những gì đã qua!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void clickEditButton(int posSwiped)
                {
                    Work work = workAdapter.get(posSwiped);

                    if (checkWorkTime(work)) {

                        Intent intent = new Intent(getApplicationContext(), EditWorkActivity.class);

                        intent.putExtra("id", String.valueOf(work.getId()));
                        intent.putExtra("title", work.getTitle());
                        intent.putExtra("time", work.getTime());
                        intent.putExtra("location", work.getLocation());
                        intent.putExtra("notification", work.getNotification());
                        intent.putExtra("indexOfWorks", String.valueOf(works.indexOf(work)));
                        intent.putExtra("indexOfWorkAdapter", String.valueOf(workAdapter.indexOf(work)));

                        int REQUEST_CODE = 9;
                        cancelAlarm((int) work.getId());
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Đừng thay đổi những gì đã qua!", Toast.LENGTH_SHORT).show();
                    }

                }

                private void clickSwipedButtonListener(final RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, final int posSwiped)
                {
                    viewHolder = recyclerView.findViewHolderForAdapterPosition(posSwiped);
                    View item = null;

                    if (viewHolder != null)
                    {
                        item = viewHolder.itemView;
                    }

                    if (item != null)
                    {
                        final float y = item.getY();
                        final float height = item.getHeight();
                        final float x = item.getX();
                        final float width = item.getWidth();



                        recyclerView.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(event.getAction() == MotionEvent.ACTION_UP && event.getY() > y && event.getY() < y + height && event.getX() > buttonEditLeft && event.getX() < buttonEditRight && !moving)
                                    if (deleteButtonVisible && editButtonVisible)
                                    {
                                        clickEditButton(posSwiped);
                                    }
                                if(event.getAction() == MotionEvent.ACTION_UP && event.getY() > y && event.getY() < y + height && event.getX() > buttonDeleteLeft && event.getX() < buttonDeleteRight && !moving)
                                    if (deleteButtonVisible && editButtonVisible)
                                    {
                                        clickDeleteButton(posSwiped);
                                    }
                                return false;
                            }
                        });
                    }
                }

            };

    public String convertDateToString(int year, int month, int day)
    {
        String month_string;
        String day_string;
        String year_string = Integer.toString(year);
        if(day < 10)
        {
            day_string = "0" + Integer.toString(day);
        }
        else
        {
            day_string = Integer.toString(day);
        }
        if(month < 9)
        {
            month_string = "0" + Integer.toString(month + 1);
        }
        else
        {
            month_string = Integer.toString(month + 1);
        }
        String dateMessage = ( day_string + "/" + month_string + "/" + year_string);
        return dateMessage;
    }

    public void processDatePickerResult(String dateString) {
        String [] dateSlice = dateString.split("/");
        int date = Integer.parseInt(dateSlice[0]);
        int month = Integer.parseInt(dateSlice[1]);
        int year = Integer.parseInt(dateSlice[2]);
        String dateMessage = convertDateToString(year, month - 2, date);
        txtDate.setText(dateMessage);
        setWorksToDay();
        adapter.notifyDataSetChanged();
    }

    String nextDay(int date, int month, int year)
    {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
        {
            if (month == 2)
            {
                if(date == 29)
                {
                    date = 1;
                    month = 3;
                }
                else date += 1;
            }
            else if (month == 4 || month == 6 || month == 9 || month == 11)
            {
                if (date == 30)
                {
                    date = 1;
                    month += 1;
                }
                else date += 1;
            }
            else if (month == 12)
            {
                if (date == 31)
                {
                    date = 1;
                    month = 1;
                    year += 1;
                }
                else
                    date += 1;
            }
            else
            {
                if ( date == 31)
                {
                    date = 1;
                    month += 1;
                }
                else date += 1;
            }
        }
        else
        {
            if (month == 2)
            {
                if(date == 28)
                {
                    date = 1;
                    month = 3;
                }
                else date += 1;
            }
            else if (month == 4 || month == 6 || month == 9 || month == 11)
            {
                if (date == 30)
                {
                    date = 1;
                    month += 1;
                }
                else date += 1;
            }
            else if (month == 12)
            {
                if (date == 31)
                {
                    date = 1;
                    month = 1;
                    year += 1;
                }
                else
                    date += 1;
            }
            else
            {
                if ( date == 31)
                {
                    date += 1;
                    month += 1;
                }
                else date += 1;
            }
        }
        return convertDateToString(year, month, date);
    }

    String previousDay(int date, int month, int year)
    {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
        {
            if (month == 3)
            {
                if (date == 1)
                {
                    month = 2;
                    date = 29;
                }
                else date -= 1;
            }
            else if (month == 8)
            {
                if (date == 1)
                {
                    month = 7;
                    date = 31;
                }
                else date -= 1;
            }
            else if (month == 1)
            {
                if (date == 1)
                {
                    date = 31;
                    month = 12;
                    year -= 1;
                }
                else date -= 1;
            }
            else if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)
            {
                if (date == 1)
                {
                    date = 31;
                    month -= 1;
                }
                else date -= 1;
            }
            else
            {
                if (date == 1)
                {
                    date = 30;
                    month -= 1;
                }
                else date -= 1;
            }
        }
        else
        {
            if (month == 3)
            {
                if (date == 1)
                {
                    month = 2;
                    date = 28;
                }
                else date -= 1;
            }
            else if (month == 8)
            {
                if (date == 1)
                {
                    month = 7;
                    date = 31;
                }
                else date -= 1;
            }
            else if (month == 1)
            {
                if (date == 1)
                {
                    date = 31;
                    month = 12;
                    year -= 1;
                }
                else date -= 1;
            }
            else if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)
            {
                if (date == 1)
                {
                    date = 31;
                    month -= 1;
                }
                else date -= 1;
            }
            else
            {
                if (date == 1)
                {
                    date = 31;
                    month -= 1;
                }
                else date -= 1;
            }
        }
        return convertDateToString(year, month, date);
    }

    class DateGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            String dateString = txtDate.getText().toString();
            int date = 0, month = 0, year = 0;
            if (e1.getX() - e2.getX() > 30)
            {
                String [] dateSlice = dateString.split("/");
                date = Integer.parseInt(dateSlice[0]);
                month = Integer.parseInt(dateSlice[1]);
                year = Integer.parseInt(dateSlice[2]);
                processDatePickerResult(nextDay(date, month, year));
            }
            if (e2.getX() - e1.getX() > 50)
            {
                String [] dateSlice = dateString.split("/");
                date = Integer.parseInt(dateSlice[0]);
                month = Integer.parseInt(dateSlice[1]);
                year = Integer.parseInt(dateSlice[2]);
                processDatePickerResult(previousDay(date, month, year));
            }

            setWorksToDay();

            workAdapter.clear();


            setWorksToDay();

            adapter.notifyDataSetChanged();

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            Intent intent = new Intent(getApplicationContext(), DateActivity.class);

            int REQUEST_CODE = 10;
            String dateSplit[] = txtDate.getText().toString().split("/");
            intent.putExtra("date",dateSplit[0]);
            intent.putExtra("month",dateSplit[1]);
            intent.putExtra("year",dateSplit[2]);
            startActivityForResult(intent, REQUEST_CODE);
            return super.onSingleTapUp(e);
        }
    }

}
