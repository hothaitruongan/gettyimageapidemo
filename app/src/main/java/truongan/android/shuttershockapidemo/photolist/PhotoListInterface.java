package truongan.android.shuttershockapidemo.photolist;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import truongan.android.shuttershockapidemo.base.BaseView;
import truongan.android.shuttershockapidemo.model.BasePhoto;

/**
 * Created by truongan91 on 5/17/16.
 */
public interface PhotoListInterface {

    interface PhotoListView extends BaseView {
        void setupView();
        void navigateToDetail(BasePhoto photo, RecyclerView.ViewHolder holder);
        void loadData(List<BasePhoto> photos, boolean isReload);
        void refresh(List<BasePhoto> photo);
        void showErrorMesage();
        void navigateToSearchView();
        void forceCloseKeyboard();
    }
    interface PhotoListPresentor {
        void onPhotoClick(BasePhoto photo, RecyclerView.ViewHolder holder);
        void pullToRefresh();
        void requestData();
        void enterQuery(String query);
        void onStop();
    }
}
