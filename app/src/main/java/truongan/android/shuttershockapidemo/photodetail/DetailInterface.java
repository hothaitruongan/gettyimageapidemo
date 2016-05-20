package truongan.android.shuttershockapidemo.photodetail;

import java.util.List;

import truongan.android.shuttershockapidemo.base.BaseView;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoDetail;
import truongan.android.shuttershockapidemo.photolist.PhotoListingAdapter;

/**
 * Created by truongan91 on 5/17/16.
 */
public interface DetailInterface {
    interface PhotoDetailView extends BaseView {
        void navigateToDetail(BasePhoto photo, SimilarListingAdapter.PhotoViewHolder holder);
        void loadSimilarPhoto(List<BasePhoto> photos);
        void loadTheFuckingDetail(PhotoDetail photo);
        void showError();
    }
    interface PhotoDetailPresentor {
        void requestForPhoto(String id);
        void requestSimilar(String id);
        void onPhotoClick(BasePhoto photo, SimilarListingAdapter.PhotoViewHolder  holder);
        void onStop();
    }
}
