package co.gon_htn.gon.firebase_objects;

import java.util.ArrayList;

public class User
{
    private String mFullName;
    private ArrayList<Event> mEvents;

    public User(String fullName) {
        mFullName = fullName;
        mEvents = new ArrayList<Event>();
    }

    public User(String fullName, ArrayList<Event> events) {
        mFullName = fullName;
        mEvents = events;
    }

    public String getFullName() {
        return mFullName;
    }

    public ArrayList<Event> getEvents() {
        return mEvents;
    }
}
