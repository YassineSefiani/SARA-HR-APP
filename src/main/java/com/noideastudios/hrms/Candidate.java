package com.noideastudios.hrms;

import java.io.Serializable;

public class Candidate implements Serializable {

    private int id;
    private String name;
    private String phone;
    private String position;
    private String status;
    private String photoURI;
    private String resumeURI;

    Candidate() {
    }

    Candidate(String name, String phone, String position, String status, String photoURI, String resumeURI) {
        this.name = name;
        this.phone = phone;
        this.position = position;
        this.status = status;
        this.photoURI = photoURI;
        this.resumeURI = resumeURI;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getResumeURI() {
        return resumeURI;
    }

    public void setResumeURI(String resumeURI) {
        this.resumeURI = resumeURI;
    }
}
