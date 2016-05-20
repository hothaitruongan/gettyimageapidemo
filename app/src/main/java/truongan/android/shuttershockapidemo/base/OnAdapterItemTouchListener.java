package truongan.android.shuttershockapidemo.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by truongan91 on 5/17/16.
 */
public interface OnAdapterItemTouchListener {
    void onItemSwipedToDismiss(RecyclerView.ViewHolder viewHolder, int position);
       void onItemMoved(int fromPosition, int toPosition);
}
