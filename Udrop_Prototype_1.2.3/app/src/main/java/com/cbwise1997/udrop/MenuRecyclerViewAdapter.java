package com.cbwise1997.udrop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MenuRVAdapter";

    private ArrayList<String> mMenuItems = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MenuRecyclerViewAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public MenuRecyclerViewAdapter(ArrayList<String> menuItems) {
        mMenuItems = menuItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mLabel;
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mLabel = itemView.findViewById(R.id.menu_item_title_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,parent,false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called.");
        String currentItem = mMenuItems.get(position);
        holder.mLabel.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }
}