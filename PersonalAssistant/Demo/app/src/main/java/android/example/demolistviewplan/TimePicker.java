package android.example.demolistviewplan;

import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanced) {
        Calendar c = Calendar.getInstance();
        int cHour = c.get(Calendar.HOUR_OF_DAY);
        int cMinute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, cHour, cMinute, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        if (getActivity() instanceof EditWorkActivity)
        {
            EditWorkActivity activity = (EditWorkActivity)getActivity();
            activity.processTimePickerResult(hourOfDay, minute);
        }
        else if (getActivity() instanceof  AddWorkActivity)
        {
            AddWorkActivity activity = (AddWorkActivity)getActivity();
            activity.processTimePickerResult(hourOfDay, minute);
        }
    }
}
