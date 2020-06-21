package android.example.demolistviewplan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewWorks extends RecyclerView.Adapter<RecyclerViewWorks.WorksViewsHolder> {
    private List<Work> works;
    private Context context;

    public RecyclerViewWorks(Context context, List<Work> works)
    {
        this.context = context;
        this.works = works;
    }


    @NonNull
    @Override
    public WorksViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work, parent, false);
                break;
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_finished, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work, parent, false);
                break;
        }
        return new WorksViewsHolder(itemView);
    }

    public static String getToday()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getTimeNow()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date time = new Date();
        return formatter.format(time);
    }

    public static boolean compareDateWork(String dateNow, String timeNow, Work work)
    {

        String dateString[] = dateNow.split("/");
        String timeString[] = timeNow.split(":");
        int dayNow = Integer.valueOf(dateString[0]);
        int monthNow = Integer.valueOf(dateString[1]);
        int yearNow = Integer.valueOf(dateString[2]);
        int hourNow = Integer.valueOf(timeString[0]);
        int minuteNow = Integer.valueOf(timeString[1]);

        String dateWorkString[] = work.getDate().split("/");
        String timeWorksString[] = work.getTime().split(":");
        int dayWork = Integer.valueOf(dateWorkString[0]);
        int monthWork = Integer.valueOf(dateWorkString[1]);
        int yearWork = Integer.valueOf(dateWorkString[2]);
        int hourWork = Integer.valueOf(timeWorksString[0]);
        int minuteWork = Integer.valueOf(timeWorksString[1]);

        if (yearNow > yearWork)
        {
            return false;
        }
        else if (yearNow < yearWork)
        {
            return true;
        }
        else {

            if (monthNow > monthWork)
            {
                return false;
            }
            else if (monthNow < monthWork)
            {
                return true;
            }
            else
            {
                if (dayNow > dayWork)
                {
                    return false;
                }
                else if(dayNow < dayWork)
                {
                    return true;
                }
                else
                {
                    if (hourNow > hourWork)
                    {
                        return false;
                    }
                    else if (hourNow < hourWork)
                    {
                        return true;
                    }
                    else
                    {
                        if (minuteNow <= minuteWork)
                        {
                            return true;
                        }
                        else
                            return false;
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        String dateNow = getToday();
        String timeNow = getTimeNow();
        Work work = works.get(position);

        if(compareDateWork(dateNow, timeNow, work))
        {
            return 1;
        }
        else
            return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWorks.WorksViewsHolder holder, int position) {
        String stringTitle = works.get(position).getTitle();
        String stringTime = works.get(position).getTime();
        String stringLocation = works.get(position).getLocation();

        holder.txtTitle.setText(stringTitle);
        holder.txtTime.setText(stringTime);
        holder.txtLocation.setText(stringLocation);

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        holder.work_layout.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return works==null?0:works.size();
    }


    public static class WorksViewsHolder extends RecyclerView.ViewHolder {

        private LinearLayout work_layout;
        private TextView txtTitle;
        private TextView txtTime;
        private TextView txtLocation;

        public WorksViewsHolder(@NonNull View itemView) {
            super(itemView);
            work_layout = (LinearLayout)itemView.findViewById(R.id.work_layout);
            txtTitle = (TextView)itemView.findViewById(R.id.titleWork);
            txtTime = (TextView)itemView.findViewById(R.id.workHour);
            txtLocation = (TextView) itemView.findViewById(R.id.location);
        }
    }
}
