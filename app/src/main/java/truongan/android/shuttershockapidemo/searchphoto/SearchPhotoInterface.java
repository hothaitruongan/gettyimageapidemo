package truongan.android.shuttershockapidemo.searchphoto;

import java.util.List;

import truongan.android.shuttershockapidemo.base.BaseView;
import truongan.android.shuttershockapidemo.model.BasePhoto;

/**
 * Created by truongan91 on 5/18/16.
 */
public interface SearchPhotoInterface {
    interface SearchView extends BaseView {
        void loadData(List<BasePhoto> photo);
        void forceCloseKeyboard();
    }

    interface SearchPresentor {
        void enterQuery(String query);
        void onStop();
    }
}
