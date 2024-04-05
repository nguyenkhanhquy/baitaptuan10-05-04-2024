package vn.iotstar.appfoods;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name, fname, email, gender, images;

    public User(int id, String name, String fname, String email, String gender, String images) {
        this.id = id;
        this.name = name;
        this.fname = fname;
        this.email = email;
        this.gender = gender;
        this.images = images;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
