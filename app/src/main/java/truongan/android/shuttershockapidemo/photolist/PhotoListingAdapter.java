package truongan.android.shuttershockapidemo.photolist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoListingAdapter extends RecyclerView.Adapter<PhotoListingAdapter.PhotoViewHolder> implements OnAdapterItemTouchListener {
    Context context;
    List<BasePhoto> photos;
    Picasso picasso;
    PhotoListInterface.PhotoListPresentor mPresentor;
    View mEmptyView;

    public PhotoListingAdapter(Context context, PhotoListInterface.PhotoListPresentor presentor) {
        this.context = context;
        this.mPresentor = presentor;
        this.photos = new ArrayList<>();
        this.picasso = Picasso.with(context);
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    public void updateEmptyView() {
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
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
        if(mEmptyView != null)
            updateEmptyView();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_photo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        final BasePhoto photo = photos.get(position);

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

    @Override
    public int getItemCount() {
        return (photos != null) ? photos.size() : 0;
    }

    @Override
    public void onItemSwipedToDismiss(RecyclerView.ViewHolder viewHolder, final int position) {
        notifyItemRemoved(position);
        Snackbar.make(viewHolder.itemView, "Removed Successful", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(photos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(photos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder implements OnViewHolderTouchHelper {
        @Bind(R.id.image)
        ImageView imageView;
        @Bind(R.id.title)
        TextView textView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#11000000"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}


