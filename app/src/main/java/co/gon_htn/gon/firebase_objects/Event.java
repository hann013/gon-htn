package co.gon_htn.gon.firebase_objects;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Event
{
    // recommendedItems and endDate are not mandatory but other ones are mandatory

    private ArrayList<String> mUserItems;

    public void setStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public void setEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    private ArrayList<String> mRecommendedItems;
    private String mStartDate;
    private String mEndDate;
    private String mLocation;
    private String mName;
    private String mSource;

    public Event()
    {

    }

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

    @Override
    public String toString() {
        return "Event{" +
                "mUserItems=" + mUserItems +
                ", mRecommendedItems=" + mRecommendedItems +
                ", mStartDate='" + mStartDate + '\'' +
                ", mEndDate='" + mEndDate + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mName='" + mName + '\'' +
                ", mSource='" + mSource + '\'' +
                '}';
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

    public void setUserItems(ArrayList<String> mUserItems) {
        this.mUserItems = mUserItems;
    }

    public void setRecommendedItems(ArrayList<String> mRecommendedItems) {
        this.mRecommendedItems = mRecommendedItems;
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
