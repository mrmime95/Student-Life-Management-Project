package com.halcyon.ubb.studentlifemanager;

/**
 * Created by Szilard on 09.12.2016.
 */

public class Reminder {
    private int id;
    private String name, date, time;

    public Reminder(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reminder(int id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
