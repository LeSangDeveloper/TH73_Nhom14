package android.example.demolistviewplan;

import java.util.Comparator;

public class Work {

    private long id;
    private String title = null;
    private String date = null;
    private String time = null;
    private String location = null;
    private String notification = null;

    public Work(){}

    public Work(String title, String date, String time, String location, String notification)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notification = notification;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return this.id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return this.time;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return this.location;
    }

    public void setNotification(String notification) { this.notification = notification; }

    public String getNotification() {return this.notification; }

    public static Comparator<Work> workSort = new Comparator<Work>() {
        @Override
        public int compare(Work o1, Work o2) {
            int hour1, hour2, minute1, minute2;
            String time1[] = o1.getTime().split(":");
            String time2[] = o2.getTime().split(":");

            hour1 = Integer.parseInt(time1[0]);
            minute1 = Integer.parseInt(time1[1]);
            hour2 = Integer.parseInt(time2[0]);
            minute2 = Integer.parseInt(time2[1]);

            if (hour1 > hour2)
            {
                return 1;
            }
            else if (hour1 < hour2)
            {
                return -1;
            }
            else
            {
                if (minute1 > minute2)
                    return 1;
                else if (minute1 < minute2)
                    return -1;
                else
                    return 0;
            }
        }
    };

}
