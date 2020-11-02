package com.picone.go4lunch.presentation.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemClickUtil {

    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private RecyclerViewItemClickUtil(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(itemID, this);
        RecyclerView.OnChildAttachStateChangeListener mAttachListener =
                new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                if (mOnItemClickListener != null) view.setOnClickListener(mOnClickListener);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) { }
        };
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static RecyclerViewItemClickUtil addTo(RecyclerView view, int itemID) {
        RecyclerViewItemClickUtil support = (RecyclerViewItemClickUtil) view.getTag(itemID);
        if (support == null) support = new RecyclerViewItemClickUtil(view, itemID);

        return support;
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mOnItemClickListener = listener; }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }
}