package com.miewlabs.karyaseni27.firebase;

public class UploadImages {

    private String mName,mImageUrl;

    public UploadImages(){}

    public UploadImages(String name, String iurl){
        if (name.trim().equals("")){
            name = "no name";
        }
        mName = name;
        mImageUrl = iurl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
