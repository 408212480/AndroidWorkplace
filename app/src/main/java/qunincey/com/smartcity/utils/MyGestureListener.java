package qunincey.com.smartcity.utils;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    RecyclerView recyclerView;

    public MyGestureListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        View childView=recyclerView.findChildViewUnder(e.getX(),e.getY());
        if (childView !=null){
            int position = recyclerView.getChildAdapterPosition(childView);
            System.out.println("短按第几个："+position);
            return true;
        }
        return super.onSingleTapUp(e);

    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);

        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            int position = recyclerView.getChildLayoutPosition(childView);
            System.out.println("长按第几个："+position);
        }
    }
}
