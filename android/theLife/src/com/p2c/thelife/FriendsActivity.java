package com.p2c.thelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

public class FriendsActivity extends SlidingMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, SlidingMenuActivity.FRIENDS_POSITION, R.layout.activity_friends);
		
		// Get the main application
		TheLifeApplication app = (TheLifeApplication)getApplication();				
		
		GridView friendsGrid = (GridView)findViewById(R.id.grid_friends);
		FriendsAdapter friendAdapter = new FriendsAdapter(this, android.R.layout.simple_list_item_1, app);
		friendsGrid.setAdapter(friendAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}
	
	/**
	 * Friend has been selected.
	 * @param view
	 * @return
	 */
	public boolean selectFriend(View view) {
		System.out.println("FRIEND VIEW SELECTED TAG " + view.getTag());
		
		// get the friend associated with this view
		FriendModel friend = (FriendModel)view.getTag();
		
		Intent intent = new Intent("com.p2c.thelife.FriendActivity");
		intent.putExtra("group_id", friend.group_id);
		intent.putExtra("friend_id", friend.friend_id);
		startActivity(intent);
		
		return true;
	}
}
