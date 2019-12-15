package com.miewlabs.karyaseni27;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miewlabs.karyaseni27.adapter.ImageViewAdapter;
import com.miewlabs.karyaseni27.firebase.UploadImages;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DasboardFragment extends Fragment {

    private View v;
    private RecyclerView mRecyclerVi;
    private ImageViewAdapter mAdapter;
    private ShimmerFrameLayout shimFrame;
    //
    private DatabaseReference mDatabaseRef;
    private List<UploadImages> mUploadList;

    public DasboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dasboard, container, false);

        //find
        mRecyclerVi = v.findViewById(R.id.recycler_view);
        shimFrame  = v.findViewById(R.id.shimmer_dasboard);

        mRecyclerVi.setHasFixedSize(false);

        mRecyclerVi.setLayoutManager( new GridLayoutManager(getActivity(), 2));

        //database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploadList = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadImages imgUp = postSnapshot.getValue(UploadImages.class);
                    mUploadList.add(imgUp);
                }
                // showing
                mRecyclerVi.setVisibility(View.VISIBLE);
                shimFrame.stopShimmer();
                shimFrame.setVisibility(View.GONE);

                mAdapter = new ImageViewAdapter(getContext(), mUploadList);
                mRecyclerVi.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimFrame.startShimmer();
    }
}
