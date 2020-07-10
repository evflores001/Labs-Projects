package com.cbwise1997.udrop.Drops;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cbwise1997.udrop.Friends.FriendListAdapter;
import com.cbwise1997.udrop.Messages.Messages;
import com.cbwise1997.udrop.R;
import com.cbwise1997.udrop.UserInfo;

import java.util.ArrayList;

public class DropsAdapter extends RecyclerView.Adapter {

    private static final String TAG = "DropsAdapter";

    private ArrayList<Messages> mDrops;

    private DropsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(View v ,int position);
        void onDeleteClick(View v, int position);
    }

    public void setOnItemClickListener(DropsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public DropsAdapter(ArrayList<Messages> drops) {
        mDrops = drops;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public ImageView mStatusImage;
        public ImageView mDeleteImage;

        public ViewHolder(View itemView, final DropsAdapter.OnItemClickListener listener) {
            super(itemView);
            mStatusImage = itemView.findViewById(R.id.drop_item_status_imgvw);
            mName = itemView.findViewById(R.id.drop_item_name_txtvw);
            mDeleteImage = itemView.findViewById(R.id.drop_item_delete_imgvw);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(v ,position);
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Created new ViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drop, parent, false);
        return new DropsAdapter.ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DropsAdapter.ViewHolder viewHolder = (DropsAdapter.ViewHolder) holder;
        if(mDrops.get(position).getStatus() == 0){
            viewHolder.mStatusImage.setImageResource(R.drawable.ic_green_circle);
        }
        else if (mDrops.get(position).getStatus() == 1){
            viewHolder.mStatusImage.setImageResource(R.drawable.ic_red_circle);
        }
        else {
            viewHolder.mStatusImage.setImageResource(R.drawable.ic_blue_circle);
        }
        viewHolder.mName.setText(mDrops.get(position).getSenderInfo().getFirstName() + " " + mDrops.get(position).getSenderInfo().getLastName());
    }

    @Override
    public int getItemCount() {
        return mDrops.size();
    }
}
