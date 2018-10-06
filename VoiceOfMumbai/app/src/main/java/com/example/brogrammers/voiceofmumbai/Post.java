package com.example.brogrammers.voiceofmumbai;
import android.media.Image;

/**
 * Created by Sagar on 10/5/2018.
 */

public class Post {

    private long post_id;
    private String user_id;
    private String user_name;
    private String description;
    private Image image;
    private String location;
    private String status;


    private int spam;
    private int upvote;


    private String pic_url;
    Post(){}
    Post(long post_id, String user_id, String user_name, String description, Image image, String location, String status) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.description = description;
        this.image = image;
        this.location = location;
        this.status = status;
    }

    Post(int post_id, String user_id, String user_name, String description, String location, String status) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.description = description;
        this.location = location;
        this.status = status;
    }
    public int getSpam() {
        return spam;
    }

    public void setSpam(int spam) {
        this.spam = spam;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }
    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return(" "+this.getDescription()+" "+this.getPost_id());
    }
}
