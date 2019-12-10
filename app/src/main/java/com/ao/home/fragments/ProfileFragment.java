package com.ao.home.fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ao.home.model.User;
import com.ao.pushnotification.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


	CircleImageView image_profile;
	TextView username;

	DatabaseReference reference;
	FirebaseUser fuser;

	StorageReference storageReference;
	private static final int IMAGE_REQUEST = 1;
	private Uri imageUri;
	private StorageTask uploadTask;

	public ProfileFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		image_profile = view.findViewById(R.id.profile_image);
		username = view.findViewById(R.id.username);

		//storageReference = FirebaseStorage.getInstance().getReference("uploads");

		fuser = FirebaseAuth.getInstance().getCurrentUser();
		reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class);
				username.setText(user.getUsername());
				if (user.getImageURL().equals("default")){
					image_profile.setImageResource(R.drawable.ic_perm_identity_black);
					
					
				}   else {
					Glide.with(getActivity()) .load(user.getImageURL()).into(image_profile);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

		return view;
	}

}
