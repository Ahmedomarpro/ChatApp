package com.ao.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.ao.home.MessageActivity;
import com.ao.home.model.User;
import com.ao.pushnotification.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

	private static final String TAG = UserAdapter.class.getSimpleName();

	//private Context context;
	protected List<User> list;
	public OnItemClickListenerView onItemClickListener;
	Context mcontext;


	public UserAdapter(List<User> list, Context mcontext) {
		this.list = list;
		this.mcontext = mcontext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.user_item, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final User user = list.get(position);

		holder.username.setText(user.getUsername());
		if (user.getImageURL().equals("default")) {
			holder.profile_image.setImageResource(R.drawable.ic_perm_identity_black);

		} else {
			Glide.with(mcontext)
					.load(user.getImageURL())
					.into(holder.profile_image);
		}
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mcontext, MessageActivity.class);
				intent.putExtra("userId", user.getId());
				mcontext.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		//return iterator
		return list == null ? 0 : list.size();

	}

	public void setOnItemClickListener(OnItemClickListenerView onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListenerView {
		void onItemClick(int position);
	}


	public static class ViewHolder extends RecyclerView.ViewHolder {
		//ViewHolder
		public  TextView username;
		public  ImageView profile_image;

		/*private ImageView img_on;
		private ImageView img_off;
		private TextView last_msg;
*/

		public ViewHolder(View itemView) {
			super(itemView);

			username = itemView.findViewById(R.id.username);
			profile_image = itemView.findViewById(R.id.profile_image);

/*			img_on = itemView.findViewById(R.id.img_on);
			img_off = itemView.findViewById(R.id.img_off);
			last_msg = itemView.findViewById(R.id.last_msg);
			*/
 	}
	}
}