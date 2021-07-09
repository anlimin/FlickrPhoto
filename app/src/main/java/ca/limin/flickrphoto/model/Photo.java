package ca.limin.flickrphoto.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Photo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String link;
    private String media;
    private String date;
    private String publish;
    private String author;
    private String authorId;
    private String tag;

    public Photo() {
    }

    public Photo(String title, String link, String media, String date, String publish, String author, String authorId, String tag) {
        this.title = title;
        this.link = link;
        this.media = media;
        this.date = date;
        this.publish = publish;
        this.author = author;
        this.authorId = authorId;
        this.tag = tag;
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

    public String getLink() {
        return link;
    }

    public String getMedia() {
        return media;
    }

    public String getDate() {
        return date;
    }

    public String getPublish() {
        return publish;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTag() {
        return tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return  "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", media='" + media + '\'' +
                ", date='" + date + '\'' +
                ", publish='" + publish + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", tag='" + tag + '\'' +
                '}' + '\n';
    }
}
