package com.ao.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ao.chatApp.R;
import com.ao.home.MessageActivity;
import com.ao.home.model.Chat;
import com.ao.home.model.User;
 import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

	private static final String TAG = MessageAdapter.class.getSimpleName();

	public static final int MSG_TYPE_LEFT = 0;
	public static final int MSG_TYPE_RIGHT = 1;

	Context mcontext;
	protected List<Chat> list;
	private String imageurl;

	FirebaseUser fuser;

	public MessageAdapter(Context mcontext, List<Chat> list, String imageurl) {
		this.mcontext = mcontext;
		this.list = list;
		this.imageurl = imageurl;
	}


	@Override
	public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == MSG_TYPE_RIGHT) {
			View view = LayoutInflater.from(mcontext)
					.inflate(R.layout.chat_item_right, parent, false);

			return new MessageAdapter.ViewHolder(view);
		} else {
			View view = LayoutInflater.from(mcontext)
					.inflate(R.layout.chat_item_left, parent, false);

			return new MessageAdapter.ViewHolder(view);

		}

	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Chat chat = list.get(position);
		holder.show_message.setText(chat.getMessage());
		if (imageurl.equals("default")) {
			holder.profile_image.setImageResource(R.drawable.ic_action_name);
		} else {
			Glide.with(mcontext).load(imageurl).into(holder.profile_image);
		}
		if (position == list.size() - 1) {
			if (chat.isIsseen()) {
				holder.txt_seen.setText("Seen");
			} else {
				holder.txt_seen.setText("Delivered");
			}
		} else {
			holder.txt_seen.setVisibility(View.GONE);
		}

	}

	@Override
	public int getItemCount() {
		//return iterator
		return list == null ? 0 : list.size();

	}


	public static class ViewHolder extends RecyclerView.ViewHolder {
		//ViewHolder
		public TextView show_message;
		public ImageView profile_image;
		public TextView txt_seen;

		public ViewHolder(View itemView) {
			super(itemView);
			show_message = itemView.findViewById(R.id.show_message);
			profile_image = itemView.findViewById(R.id.profile_image);
			txt_seen = itemView.findViewById(R.id.txt_seen);

		}
	}

	@Override
	public int getItemViewType(int position) {
		fuser = FirebaseAuth.getInstance().getCurrentUser();
		if (list.get(position).getSender().equals(fuser.getUid())) {
			return MSG_TYPE_RIGHT;
		} else {
			return MSG_TYPE_LEFT;
		}
	}
}