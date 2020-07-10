package com.cbwise1997.udrop.Friends;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbwise1997.udrop.R;
import com.cbwise1997.udrop.UserInfo;

import java.util.ArrayList;

// adapter for the friend items for friends recycler view

public class FriendListAdapter extends RecyclerView.Adapter{

    private static final String TAG = "FriendAdapter";
    private static final int FRIEND = 0;
    private static final int FRIEND_REQUEST = 1;

    private ArrayList<UserInfo> mFriends;
    private ArrayList<UserInfo> mFriendRequests;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(View v ,int position);
        void onAcceptClick(View v, int position);
        void onDeclineClick(View v, int position);
        void onDeleteClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public FriendListAdapter(ArrayList<UserInfo> friends, ArrayList<UserInfo> friendRequests) {
        mFriends = friends;
        mFriendRequests = friendRequests;
    }

    public static class ViewHolderFriend extends RecyclerView.ViewHolder {

        public ImageView mProfImage;
        public TextView mName;
        public ImageView mDeleteImage;

        public ViewHolderFriend(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mProfImage = itemView.findViewById(R.id.layoutFriendItem_profile_IV);
            mName = itemView.findViewById(R.id.layoutFriendItem_name_TV);
            mDeleteImage = itemView.findViewById(R.id.layoutFriendItem_options_IV);

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

    public static class ViewHolderFriendRequest extends RecyclerView.ViewHolder{
        ImageView mProfImage, mAcceptImage, mDeclineImage;
        TextView mName;

        public ViewHolderFriendRequest(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mProfImage = itemView.findViewById(R.id.friend_request_image);
            mAcceptImage = itemView.findViewById(R.id.friend_request_accept);
            mDeclineImage = itemView.findViewById(R.id.friend_request_decline);
            mName = itemView.findViewById(R.id.friend_request_name);

            mAcceptImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onAcceptClick(v, position);
                    }
                }
            });

            mDeclineImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeclineClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == FRIEND){
            Log.d(TAG, "onCreateViewHolder: Created new ViewHolderFriend");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
            return new ViewHolderFriend(view, mListener);
        }
        else if (viewType == FRIEND_REQUEST){
            Log.d(TAG, "onCreateViewHolder: Created new ViewHolderFriendRequest");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request,parent,false);
            return new ViewHolderFriendRequest(view, mListener);
        }

        Log.d(TAG, "onCreateViewHolder: Could not create view holder");
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position < mFriendRequests.size()){
            ViewHolderFriendRequest viewHolder = (ViewHolderFriendRequest) holder;
            viewHolder.mName.setText(mFriendRequests.get(position).getFirstName() + " " + mFriendRequests.get(position).getLastName());
        }
        else {
            ViewHolderFriend viewHolder = (ViewHolderFriend) holder;
            viewHolder.mName.setText(mFriends.get(position - mFriendRequests.size()).getFirstName() + " " + mFriends.get(position - mFriendRequests.size()).getLastName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mFriendRequests.size()){
            return FRIEND_REQUEST;
        }
        return FRIEND;
    }

    @Override
    public int getItemCount() {
        return mFriends.size() + mFriendRequests.size();
    }
 }