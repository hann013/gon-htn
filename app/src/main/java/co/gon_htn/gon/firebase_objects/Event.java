package co.gon_htn.gon.firebase_objects;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by dayeonglee on 15-09-19.
 */
public class Event
{
    // recommendedItems and endDate are not mandatory but other ones are mandatory

    private String[] mRecommendedItems;
    private String[] mUserItems;
    private String mStartDate;
    private String mEndDate;
    private String mLocation;
    private String mName;
    private String mSource;

    public Event(String name, String source, String location, String startDate, String endDate, String[] userItems,
                 String[] recommendedItems)
    {
        mName = name;
        mSource = source;
        mLocation = location;
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        mRecommendedItems = recommendedItems;
        mUserItems = userItems;
    }

    public Event(String name, String source, String location, String startDate, String endDate, String[] userItems)
    {
        mName = name;
        mSource = source;
        mLocation = location;
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        mUserItems = userItems;
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

    public String[] getUserItems()
    {
        return mUserItems;
    }

    public String[] getRecommendedItems()
    {
        return mRecommendedItems;
    }
}
