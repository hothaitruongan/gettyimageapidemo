package truongan.android.shuttershockapidemo.model;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoDetail {

    String id;
    String uri;
    String artist;
    String title;
    String caption;
    String[] people;
    int width;
    int height;

    public PhotoDetail(String id, String artist, String title) {
        this.id = id;
        this.artist = artist;
        this.title = title;
    }

    public PhotoDetail(String id, String artist, String title, String caption, String[] people) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.caption = caption;
        this.people = people;
    }

    public PhotoDetail(String id, String uri, String artist, String title, int width, int height) {
        this.id = id;
        this.uri = uri;
        this.artist = artist;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getCaption() {
        return caption;
    }

    public String[] getPeople() {
        return people;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
