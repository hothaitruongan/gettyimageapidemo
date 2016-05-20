package truongan.android.shuttershockapidemo.photodetail;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import truongan.android.shuttershockapidemo.R;
import truongan.android.shuttershockapidemo.base.OnAdapterItemTouchListener;
import truongan.android.shuttershockapidemo.base.OnViewHolderTouchHelper;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;
import truongan.android.shuttershockapidemo.photolist.PhotoListInterface;

/**
 * Created by truongan91 on 5/17/16.
 */
public class SimilarListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    DetailInterface.PhotoDetailPresentor mPresentor;
    List<BasePhoto> photos;
    Picasso picasso;
    boolean hasHeader = false;
    final static int TYPE_HEADER = 1231;
    final static int TYPE_DEFAULT = 1213;

    View mEmptyView;

    public SimilarListingAdapter(Context context, DetailInterface.PhotoDetailPresentor presentor) {
        this.context = context;
        this.mPresentor = presentor;
        this.photos = new ArrayList<>();
        this.picasso = Picasso.with(context);
    }

    public void setHasHeader(boolean bool) {
        this.hasHeader = bool;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    private void updateEmptyView() {
        if (getItemCount() != 0)
            mEmptyView.setVisibility(View.GONE);
        else
            mEmptyView.setVisibility(View.VISIBLE);
    }

    public void addAll(List<BasePhoto> photos, boolean isReload) {
        if (isReload) {
            this.photos.clear();
            this.photos.addAll(photos);
            notifyDataSetChanged();
        } else {
            if (getItemCount() != 0) {
                final int currentLastIndex = this.photos.size();
                this.photos.addAll(photos);
                notifyItemRangeInserted(currentLastIndex, photos.size());
            } else {
                this.photos.addAll(photos);
                notifyItemRangeChanged(0, photos.size());
            }
        }

        if (mEmptyView != null)
            updateEmptyView();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (hasHeader) {
            if (viewType == TYPE_DEFAULT)
                return new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_photo_item, parent, false));
            else
                return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.similar_photo_header, parent, false));
        } else {
            return new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_photo_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader) {
            if (position == 0)
                return TYPE_HEADER;
            return TYPE_DEFAULT;
        }
        return TYPE_DEFAULT;
    }

    public PhotoWrapper getDatasetAsWrapper() {
        return new PhotoWrapper(photos);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (hasHeader) {
            if (holder instanceof HeaderHolder)
                onBindHeaderTitle((HeaderHolder) holder, position);
            else
                onBindPhotoHolder((PhotoViewHolder) holder, position);
        } else {
            onBindPhotoHolder((PhotoViewHolder) holder, position);
        }
    }

    private void onBindPhotoHolder(final PhotoViewHolder holder, int position) {
        final BasePhoto photo = photos.get((hasHeader) ? position - 1 : position);
        picasso.load(photo.getUrl())
                .resize(300, 350)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(photo.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresentor.onPhotoClick(photo, holder);
            }
        });
    }

    private void onBindHeaderTitle(HeaderHolder holder, int position) {
        // nothing to do here (-_-)
    }

    @Override
    public int getItemCount() {
        return (photos != null) ? ((hasHeader) ? (photos.size() + 1) : photos.size()) : 0;
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView imageView;
        @Bind(R.id.title)
        TextView textView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


