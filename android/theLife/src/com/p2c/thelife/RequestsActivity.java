package com.p2c.thelife;

import android.os.Bundle;
import android.view.Menu;

public class RequestsActivity extends SlidingMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_requests, SlidingMenuSupport.REQUESTS_POSITION);				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.requests, menu);
		return true;
	}

}
