package com.ao.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ao.chatApp.R;
import com.ao.home.MessageActivity;
import com.ao.home.model.Chat;
import com.ao.home.model.User;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

	private static final String TAG = UserAdapter.class.getSimpleName();

 	Context mcontext;
	protected List<User> list;
	private boolean ischat;

	String theLastMessage;

	public UserAdapter(Context mcontext, List<User> list, boolean ischat) {
		this.mcontext = mcontext;
		this.list = list;
		this.ischat = ischat;
	}




	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mcontext)
				.inflate(R.layout.user_item, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final User user = list.get(position);

		holder.username.setText(user.getUsername());
		if (user.getImageURL().equals("default")) {
			holder.profile_image.setImageResource(R.drawable.ic_icons_user);

		} else {
			Glide.with(mcontext)
					.load(user.getImageURL())
					.into(holder.profile_image);
		}
		if (ischat){
			lastMessage(user.getId(), holder.last_msg);
		} else {
			holder.last_msg.setVisibility(View.GONE);
		}

		if (ischat){
			if (user.getStatus().equals("online")){
				holder.img_on.setVisibility(View.VISIBLE);
				holder.img_off.setVisibility(View.GONE);
			} else {
				holder.img_on.setVisibility(View.GONE);
				holder.img_off.setVisibility(View.VISIBLE);
			}
		} else {
			holder.img_on.setVisibility(View.GONE);
			holder.img_off.setVisibility(View.GONE);
		}




		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mcontext, MessageActivity.class);
				intent.putExtra("userid", user.getId());
				mcontext.startActivity(intent);
			}
		});
/*
		if (ischat) {
			if (user.getStatus().equals("Online")) {
				holder.img_on.setVisibility(View.VISIBLE);
				holder.img_off.setVisibility(View.GONE);
			} else {

				holder.img_on.setVisibility(View.GONE);
				holder.img_off.setVisibility(View.VISIBLE);
			}
		} else {
			holder.img_on.setVisibility(View.GONE);
			holder.img_off.setVisibility(View.GONE);

		}

		if (ischat){
			lastMessage(user.getId().trim(),holder.last_msg);
		}else {
			holder.last_msg.setVisibility(View.GONE);
		}
*/
	}

	@Override
	public int getItemCount() {
		//return iterator
		//return list == null ? 0 : list.size();
		return  list.size();

	}

	/*public void setOnItemClickListener(OnItemClickListenerView onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListenerView {
		void onItemClick(int position);
	}*/


	public static class ViewHolder extends RecyclerView.ViewHolder {
		//ViewHolder
		public TextView username;
		public ImageView profile_image;

		private ImageView img_on;
		private ImageView img_off;
		private TextView last_msg;

		public ViewHolder(View itemView) {
			super(itemView);

			username = itemView.findViewById(R.id.username);
			profile_image = itemView.findViewById(R.id.profile_image);

			img_on = itemView.findViewById(R.id.img_on);
			img_off = itemView.findViewById(R.id.img_off);
			last_msg = itemView.findViewById(R.id.last_msg);
		}
	}

	private void lastMessage(final String userid, final TextView last_msg) {

		theLastMessage = "default";
		final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					Chat chat = snapshot.getValue(Chat.class);
					if (firebaseUser != null && chat != null) {
						if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
								chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
							theLastMessage = chat.getMessage();
						}
					}
				}
				switch (theLastMessage){
					case  "default":
						last_msg.setText("No Message");
						break;

					default:
						last_msg.setText(theLastMessage);
						break;
				}

				theLastMessage = "default";

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});


	}
}