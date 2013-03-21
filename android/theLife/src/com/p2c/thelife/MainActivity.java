package com.p2c.thelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends SlidingMenuActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, SlidingMenuActivity.COMMUNITY_POSITION, R.layout.activity_main);		
			
		// attach the event list view
		ListView listView = (ListView)findViewById(R.id.list);
		MainEventsAdapter adapter = new MainEventsAdapter(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
		
		// load the database from the server in the background
		TheLifeConfiguration.getEventsDS().addDataStoreListener(adapter);
		TheLifeConfiguration.getEventsDS().refresh();			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		if (item.getItemId() == R.id.action_help) {
			startActivity(new Intent("com.p2c.thelife.CommunityHelp"));
		}
		return true;
	}

}
