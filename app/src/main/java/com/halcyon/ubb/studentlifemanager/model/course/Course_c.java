package com.halcyon.ubb.studentlifemanager.model.course;

/**
 *
 * Created by matyas on 2016.12.09..
 */

public class Course_c {
    private String title;
    private String description;
    private String fileName;
    private String fileUrl;
    private String pictureName;
    private String course;

    public Course_c() {
    }

    public Course_c(String title, String description, String fileName, String pictureName, String courseName) {
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.pictureName = pictureName;
        this.course = courseName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
