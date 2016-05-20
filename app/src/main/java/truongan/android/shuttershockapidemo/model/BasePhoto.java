package truongan.android.shuttershockapidemo.model;

import java.io.Serializable;

/**
 * Created by truongan91 on 5/17/16.
 */
public class BasePhoto implements Serializable {
    String id;
    int collectionId;
    String assetFamily;
    String title;
    String url;
    int width;
    int height;

    public BasePhoto(String id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

    public BasePhoto(String id, int collectionId, String assetFamily, String title, String url, int width, int height) {
        this.id = id;
        this.collectionId = collectionId;
        this.assetFamily = assetFamily;
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
