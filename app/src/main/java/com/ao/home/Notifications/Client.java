package com.ao.home.Notifications;

import android.widget.Toast;
import android.widget.Toolbar;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

	private static Retrofit retrofit;

	public static Retrofit getRetrofit(String url) {
		if (retrofit == null) {
			retrofit = new Retrofit
					.Builder()
					.baseUrl(url)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}


		return retrofit;
	}
}
