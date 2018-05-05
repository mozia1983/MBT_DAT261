package com.company;

import java.util.ArrayList;
import java.util.List;

public class Course {
    String name ;
    String id ;
    ArrayList<String> sessions ;

    public Course(String name, String id) {
        this.name = name;
        this.id   = id  ;
        sessions = new ArrayList<>();
    }

    public void setSessions(ArrayList<String> sessions) {
        this.sessions = sessions;
    }

    public void addSession(String session) {
        this.sessions.add(session);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getSessions() {
        return sessions;
    }

}
