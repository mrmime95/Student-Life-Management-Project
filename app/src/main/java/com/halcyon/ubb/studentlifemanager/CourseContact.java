package com.halcyon.ubb.studentlifemanager;

/**
 * Created by Szilard on 01.12.2016.
 */

public class CourseContact {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int image_id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public CourseContact(int img, String title, String description){
        this.setDescription(description);
        this.setImage_id(img);
        this.setTitle(title);
    }
}
