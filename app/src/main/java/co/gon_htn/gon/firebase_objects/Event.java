package co.gon_htn.gon.firebase_objects;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Event
{
    // recommendedItems and endDate are not mandatory but other ones are mandatory

    private ArrayList<String> mUserItems;
    private ArrayList<String> mRecommendedItems;
    private String mStartDate;
    private String mEndDate;
    private String mLocation;
    private String mName;
    private String mSource;

    public Event(String name, String source, String location, String startDate, String endDate,
                 ArrayList<String> userItems, ArrayList<String> recommendedItems) {
        mName = name;
        mSource = source;
        mLocation = location;
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        mRecommendedItems = recommendedItems;
        mUserItems = userItems;
    }

    public Event(String name, String source, String location, String startDate, String endDate,
                 ArrayList<String> userItems) {
        mName = name;
        mSource = source;
        mLocation = location;
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        mUserItems = userItems;
    }

    public Event(String name, String source, String location, String startDate, String endDate) {
        mName = name;
        mSource = source;
        mLocation = location;
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        mRecommendedItems = new ArrayList<>();
        mUserItems = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public String getSource() {
        return mSource;
    }

    public String getLocation()
    {
        return mLocation;
    }

    public String getStartDate()
    {
        return mStartDate;
    }

    public String getEndDate()
    {
        return mEndDate;
    }

    public ArrayList<String> getUserItems()
    {
        return mUserItems;
    }

    public ArrayList<String> getRecommendedItems()
    {
        return mRecommendedItems;
    }
}
