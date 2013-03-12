package com.p2c.thelife;

import java.util.Collection;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2c.thelife.model.EventModel;
import com.p2c.thelife.model.FriendModel;
import com.p2c.thelife.model.UserModel;

public class EventsForFriendAdapter extends ArrayAdapter<EventModel> {
	
	private static final String TAG = "DeedsDS"; 
	
	private FriendModel m_friend = null;
	private TheLifeApplication m_app = null;
	
	public EventsForFriendAdapter(Context context, int mode, TheLifeApplication app, FriendModel friend) {
		super(context, mode);
		
		m_app = app;
		m_friend = friend;
		
		// get all the Events for the current user
		Collection<EventModel> events = m_app.getEventsDS().findByFriend(m_friend.group_id, m_friend.friend_id);
		for (EventModel m:events) {
			add(m);
		}
		Log.d(TAG, "FOUND EVENTS FOR FRIEND " + m_friend + ": " + getCount());
	}
	
	// see ApiDemos List14.java for other (maybe better?) ways for this
	// TODO this routine is duplicated
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// get the view
		View eventView = convertView;
		if (eventView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			eventView = inflator.inflate(R.layout.event_cell, null);
		}
		
		// get the event for this view
		EventModel event = getItem(position);
		UserModel user = m_app.getUsersDS().findById(event.group_id, event.user_id);
		FriendModel friend = m_app.getFriendsDS().findById(event.group_id, event.friend_id);
		
		TextView textViewDescription = (TextView)eventView.findViewById(R.id.textViewDescription);
		String eventDescription = Utilities.fill_template_string(user, friend, event.description);
		textViewDescription.setText(Html.fromHtml(eventDescription));
		
		ImageView imageView1 = (ImageView)eventView.findViewById(R.id.imageView1);
		imageView1.setImageDrawable(user.image);
		ImageView imageView2 = (ImageView)eventView.findViewById(R.id.imageView2);
		imageView2.setImageDrawable(friend.image);		
		
		// only show the pledge view if the event requests it
		CheckBox pledgeView = (CheckBox)eventView.findViewById(R.id.pledgeView);				
		if (event.isPledge) {
			pledgeView.setVisibility(View.VISIBLE);
			String pledgeDescription = Utilities.fill_template_string(user, friend, "Pray for $u and $f."); // TODO translated
			pledgeView.setText(pledgeDescription);			
		} else {
			pledgeView.setVisibility(View.GONE);
		}
		
		return eventView;
	}

}
