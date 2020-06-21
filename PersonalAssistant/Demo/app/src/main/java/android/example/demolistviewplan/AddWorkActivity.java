package android.example.demolistviewplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddWorkActivity extends AppCompatActivity {

    private EditText EditTextWork, EditTextLocation;
    private TextView EditTextHour;
    private RadioButton notifyNone, notify5Min, notify30Min, notify60Min;

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
        setContentView(R.layout.activity_work_detail);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        assign();
    }

    private void assign()
    {
        EditTextHour = (TextView) findViewById(R.id.editTextHour);
        EditTextWork = (EditText)findViewById(R.id.editTextTitle);
        EditTextLocation = (EditText)findViewById(R.id.editTextLocation);

        notifyNone = (RadioButton)findViewById(R.id.notifyNone);
        notify5Min = (RadioButton)findViewById(R.id.notify5Min);
        notify30Min = (RadioButton)findViewById(R.id.notify30Min);
        notify60Min = (RadioButton)findViewById(R.id.notify60Min);

    }

    public void btnFinish(View view){
        finish();
    }


    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("activity", "add");
        data.putExtra("hour", EditTextHour.getText().toString());
        data.putExtra("work", EditTextWork.getText().toString());
        data.putExtra("location", EditTextLocation.getText().toString());

        if (notifyNone.isChecked())
        {
            data.putExtra("notification", "none");
        }
        else if (notify5Min.isChecked())
        {
            data.putExtra("notification", "5");
        }
        else if (notify30Min.isChecked())
        {
            data.putExtra("notification", "30");
        }
        else if (notify60Min.isChecked())
        {
            data.putExtra("notification", "60");
        }

        setResult(RESULT_OK, data);
        super.finish();
    }

    public void chooseTime(View view)
    {
        androidx.fragment.app.DialogFragment timePicker = new TimePicker();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void processTimePickerResult(int hour, int minute)
    {
        String stringHour, stringMinute;
        if (hour < 10)
        {
            stringHour = "0" + hour;
        }
        else
        {
            stringHour = String.valueOf(hour);
        }

        if (minute < 10)
        {
            stringMinute = "0" + minute;
        }
        else
        {
            stringMinute = String.valueOf(minute);
        }

        EditTextHour.setText(stringHour + ":" + stringMinute);
    }

}
