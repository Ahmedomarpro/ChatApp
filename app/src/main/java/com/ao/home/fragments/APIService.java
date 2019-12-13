package com.ao.home.fragments;

import com.ao.home.Notifications.MyResponse;
import com.ao.home.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
	@Headers(
			{
					"Content-Type:application/json",
					"Authorization:key=AAAA2t7hIGU:APA91bEtPK56CYdQeBgQLDuPJHxki8y_-2CzG8kHkVv0_aJu0d9vhVSb1thR3sSCaQCpNbqyT5S-NJZI50FZya71AZOZaeNjDA_Wu3K1YIM4dmqyI8QwfC56u-UNI91CKk2-KtUwHVSx"
			}
	)
	@POST("fcm/send")
	Call<MyResponse> sendNotification(@Body Sender body);
}
