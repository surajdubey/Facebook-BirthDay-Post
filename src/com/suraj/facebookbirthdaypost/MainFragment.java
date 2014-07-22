package com.suraj.facebookbirthdaypost;

import com.facebook.Session;
import com.facebook.SessionState;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
	

	private static final String TAG = "MainFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_main, container , false);
		return view;
	}
	
	private void onSessionStateChange(Session session , SessionState state , Exception exception)
	{
		if(session.isOpened())
		{
			Log.i(TAG, "Log In..");
			
		}
		else if(session.isClosed())
		{
			Log.i(TAG, "Logged OUT...");
		}
		
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

}
