package com.ctel_rtc.secretnotifications;

/**
 * Created by ctel-cpu-78 on 6/16/2017.
 */

public class SavedModel {

    private int id;
    private String title;
    private String text;
    private String name;

    public SavedModel(int id, String title, String text, String name) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.name = name;
    }

    public SavedModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
