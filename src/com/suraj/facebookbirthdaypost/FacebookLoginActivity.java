package com.suraj.facebookbirthdaypost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Session;

import com.facebook.SessionState;
import com.facebook.Settings;

public class FacebookLoginActivity extends Activity {
	
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
	private TextView instructionsOrLink;
	private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    
    private static final String TAG = "fb";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);
		
		instructionsOrLink = (TextView) findViewById(R.id.instructionsOrLink);
		buttonLoginLogout = (Button) findViewById(R.id.buttonLoginLogout);
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		Session session = Session.getActiveSession();
		if(session == null)
		{
			Log.i(TAG, "STAART: session null");
			if(savedInstanceState!=null)
			{
				Log.i(TAG, "savedIns not null");
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
				Log.i(TAG, "end savedIns not null");
			}
			
			if(session == null)
			{
				Log.i(TAG, "session null");
				session= new Session(this);
				Log.i(TAG, "end session null");
			}
			
			Session.setActiveSession(session);
			if(session.getState().equals(SessionState.CREATED_TOKEN_LOADED))
			{
				session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			}
			
		}
		
		updateView();
		
		
		
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onsaveins");
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
		Log.i(TAG, "end onsaveins");
		
	}
	
	private void updateView()
	{
		Log.i(TAG, "view update");
		Session session = Session.getActiveSession();
		if(session.isOpened())
		{
			Log.i(TAG, "session open");
			instructionsOrLink.setText(URL_PREFIX_FRIENDS+session.getAccessToken());
			buttonLoginLogout.setText(R.string.logout);
			buttonLoginLogout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Log.i(TAG, "clicked logout");
					onClickLogOut();
				}
			});
			
		}
		
		else
		{
			Log.i(TAG, "session closed");
			
			instructionsOrLink.setText(R.string.instructions);
			buttonLoginLogout.setText(R.string.login);
			buttonLoginLogout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i(TAG, "clicked login");
					
					onClicklogin();
				}
			});
			Log.i(TAG, "end session closed");
			
		}
	}
	
	private void onClicklogin()
	{
		Session session = Session.getActiveSession();
		if(!session.isOpened() && !session.isClosed())
		{
			session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			
		}
		else
		{
			Session.openActiveSession(this, true, statusCallback);
			
		}
		
	}
	
	private void onClickLogOut()
	{
		Log.i(TAG, "logout");
		Session session = Session.getActiveSession();
		if(!session.isClosed())
		{
			session.closeAndClearTokenInformation();
			
		}
		Log.i(TAG, "end logout");
	}
	
   private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }



}
