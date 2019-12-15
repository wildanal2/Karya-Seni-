package com.miewlabs.karyaseni27.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miewlabs.karyaseni27.R;
import com.miewlabs.karyaseni27.firebase.UploadImages;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {
    private Context mContex;
    private List<UploadImages> mListUploads;

    public ImageViewAdapter(Context mCon, List<UploadImages> list){
        mContex = mCon;
        mListUploads = list;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContex).inflate(R.layout.item_images,parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UploadImages cImg = mListUploads.get(position);

        Picasso.get().load(cImg.getmImageUrl()).resize(640,640).centerCrop().into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return mListUploads.size();
    }


    //class temp
    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_view);
        }
    }

}
