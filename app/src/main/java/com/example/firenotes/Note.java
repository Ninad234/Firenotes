package com.example.firenotes;
import java.util.List;

public class Note {
    String title;
    String content;
    String time;
    List<String> tags;
    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getTags(){
        return tags;
    }

    public void setTags(List <String> tags){
        this.tags = tags;
    }
}
