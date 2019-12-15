package com.miewlabs.karyaseni27.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
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
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final UploadImages cImg = mListUploads.get(position);

        Picasso.get().load(cImg.getmImageUrl()).resize(580,580).centerCrop().into(holder.imgView);
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContex, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.view_image);

                PhotoView p = dialog.findViewById(R.id.img_viewer);
                ImageView btn_close = dialog.findViewById(R.id.img_closer);
                Picasso.get().load(cImg.getmImageUrl()).into(p);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
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
