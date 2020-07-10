package com.cbwise1997.udrop.Locations;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbwise1997.udrop.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private static final String TAG = "LocationAdapter";

    private ArrayList<Loc> mLocations;
    private OnItemClickListener mListener;
    private Context mContext;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onDeleteClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public LocationAdapter(ArrayList<Loc> locations) {
        mLocations = locations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mLocationImage;
        public TextView mName;
        public ImageView mDeleteImage;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mLocationImage = itemView.findViewById(R.id.location_item_location_imgvw);
            mName = itemView.findViewById(R.id.location_item_name_TV);
            mDeleteImage = itemView.findViewById(R.id.location_item_delete_imgvw);

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location,parent,false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called.");
        Loc currentItem = mLocations.get(position);
        Log.d(TAG, "onBindViewHolder: currentItem="+currentItem);
        holder.mName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

}
