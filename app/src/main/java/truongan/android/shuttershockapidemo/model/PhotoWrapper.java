package truongan.android.shuttershockapidemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoWrapper implements Serializable {
    List<BasePhoto> photos;

    public PhotoWrapper(List<BasePhoto> photos) {
        this.photos = photos;
    }
    public List<BasePhoto> getPhotos() {
        return photos;
    }
}
