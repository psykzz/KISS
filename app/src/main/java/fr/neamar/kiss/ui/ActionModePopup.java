package fr.neamar.kiss.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.MenuRes;
import android.support.annotation.RequiresApi;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

interface OnActionItemClickListener {
    void onActionItemClick(MenuItem item);
}

@RequiresApi(Build.VERSION_CODES.M)
public class ActionModePopup extends ActionMode.Callback2{

    public OnActionItemClickListener onActionItemClickListener;

    private ActionMode mode;

    @MenuRes private Integer menuResId = 0;
    private Integer contentLeft = 0;
    private Integer contentTop = 0;
    private Integer contentRight = 0;
    private Integer contentBottom = 0;

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mode = actionMode;
        actionMode.getMenuInflater().inflate(menuResId, menu);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if(onActionItemClickListener != null) {
            onActionItemClickListener.onActionItemClick(menuItem);
        }
        mode.finish();
        return true;
    }

    @Override
    public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
        outRect.set(contentLeft, contentTop, contentRight, contentBottom);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
    }

    public void startActionMode(View view, @MenuRes Integer menuResId) {
        startActionMode(view, menuResId, 0, 0);
    }
    public void startActionMode(View view, @MenuRes Integer menuResId, Integer contentLeft, Integer contentTop) {
        startActionMode(view, menuResId, contentLeft, contentTop, view.getWidth(), view.getHeight());

    }
    public void startActionMode(View view, @MenuRes Integer menuResId, Integer contentLeft, Integer contentTop, Integer contentRight, Integer contentBottom) {
        this.menuResId = menuResId;
        this.contentLeft = contentLeft;
        this.contentTop = contentTop;
        this.contentRight = contentRight;
        this.contentBottom = contentBottom;
        view.startActionMode(this, ActionMode.TYPE_FLOATING);
    }
    public void startActionMode(Activity activity, @MenuRes Integer menuResId) {
        startActionMode(activity, menuResId, 0, 0);
    }
    public void startActionMode(Activity activity, @MenuRes Integer menuResId, Integer contentLeft, Integer contentTop) {
        // No idea how to get the dimensions of an activity.
        startActionMode(activity, menuResId, contentLeft, contentTop, 100, 50);
    }
    public void startActionMode(Activity activity, @MenuRes Integer menuResId, Integer contentLeft, Integer contentTop, Integer contentRight, Integer contentBottom) {
        this.menuResId = menuResId;
        this.contentLeft = contentLeft;
        this.contentTop = contentTop;
        this.contentRight = contentRight;
        this.contentBottom = contentBottom;
        activity.startActionMode(this, ActionMode.TYPE_FLOATING);
    }
}
