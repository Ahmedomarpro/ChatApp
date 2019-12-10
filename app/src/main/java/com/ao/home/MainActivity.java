package com.ao.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ao.home.fragments.ChatFragment;
import com.ao.home.fragments.ProfileFragment;
import com.ao.home.fragments.UsersFragment;
import com.ao.home.model.User;
import com.ao.pushnotification.R;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
	CircleImageView profile_image;
	TextView username;

	FirebaseUser firebaseUser;
	DatabaseReference reference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");

		profile_image = findViewById(R.id.profile_image);
		username = findViewById(R.id.username);

		TabLayout tabLayout = findViewById(R.id.tab_layout);
		  ViewPager viewPager = findViewById(R.id.view_pager);
		  

		firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class);
				assert user != null;
				username.setText(user.getUsername());
				if (user.getImageURL().equals("default"))  {
					profile_image.setImageResource(R.drawable.ic_perm_identity_black);
				}else {
					Glide.with(MainActivity.this)
							.load(user.getImageURL())
							.into(profile_image);
				}

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPagerAdapter.addFragMent(new ChatFragment(),"Chats");
		viewPagerAdapter.addFragMent(new UsersFragment(),"Users");
		viewPagerAdapter.addFragMent(new ProfileFragment(),"Profile");

		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()){
			case R.id.logout:
				FirebaseAuth.getInstance().signOut();
				startActivity(new Intent (MainActivity.this,StartActivity.class));
				finish();
				return true;
		}
		return false;
	}

	class ViewPagerAdapter extends FragmentPagerAdapter{

		private List <Fragment> fragments;
		private List <String> tites;


		public ViewPagerAdapter(@NonNull FragmentManager fm) {
			super(fm);
			this.fragments = new ArrayList<>();
			this.tites = new ArrayList<>();
		}

		@NonNull
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		public void addFragMent(Fragment fragment,String title){
			fragments.add(fragment);
			tites.add(title);
		}

		 
		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return tites.get(position);
		}
	}

}
