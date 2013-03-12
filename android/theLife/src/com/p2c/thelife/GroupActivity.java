package com.p2c.thelife;

import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

import com.p2c.thelife.model.GroupModel;

public class GroupActivity extends SlidingMenuActivity {
	
	private GroupModel m_group = null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, SlidingMenuActivity.FRIENDS_POSITION, R.layout.activity_group);
		
		// Get the main application
		TheLifeApplication app = (TheLifeApplication)getApplication();				
		
		// Get the group for this activity
		int groupId = getIntent().getIntExtra("group_id", 0);
		m_group = app.getGroupsDS().findById(groupId);		
		
		// Show the group
		if (m_group != null) {		
			TextView nameView = (TextView)findViewById(R.id.activity_group_name);
			nameView.setText(m_group.name);
		}
		
		// attach the users-in-group list view
		GridView usersView = (GridView)findViewById(R.id.activity_group_users);
		GroupUsersAdapter adapter = new GroupUsersAdapter(this, android.R.layout.simple_list_item_1, app, m_group);
		usersView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

}
