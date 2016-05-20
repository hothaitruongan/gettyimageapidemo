package truongan.android.shuttershockapidemo.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

/**
 * Created by truongan91 on 5/18/16.
 */
public class KeyboardVisibilityListener implements ViewTreeObserver.OnGlobalLayoutListener {

    boolean isAttachedListener = false;
    View mView;
    Activity mActivity;
    KeyboardResponseListener mListener;

    public KeyboardVisibilityListener(Activity activity, View view, KeyboardResponseListener listener) {
        this.mActivity = activity;
        this.mView = view;
        this.mListener = listener;
        mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        isAttachedListener = true;
    }

    @Override
    public void onGlobalLayout() {
        int heightDiff = mView.getRootView().getHeight() - mView.getHeight();
        int contentViewTop = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mActivity);
        Log.d(KeyboardVisibilityListener.class.getSimpleName(), "contentViewTop:" + contentViewTop);
        Log.d(KeyboardVisibilityListener.class.getSimpleName(), "heightDiff:" + heightDiff);
        if (heightDiff <= contentViewTop) {
            mListener.onHideKeyboard();

            Intent intent = new Intent("KeyboardWillHide");
            broadcastManager.sendBroadcast(intent);
        } else {
            int keyboardHeight = heightDiff - contentViewTop;
            mListener.onShowKeyboard(keyboardHeight);
            Intent intent = new Intent("KeyboardWillShow");
            intent.putExtra("KeyboardHeight", keyboardHeight);
            broadcastManager.sendBroadcast(intent);
        }
    }

    public void clearListener() {
        mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        isAttachedListener = false;
    }

    public interface KeyboardResponseListener {
        void onHideKeyboard();

        void onShowKeyboard(int keyboardHeight);
    }
}
