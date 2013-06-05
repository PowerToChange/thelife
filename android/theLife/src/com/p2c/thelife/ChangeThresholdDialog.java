package com.p2c.thelife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.p2c.thelife.model.DeedModel;
import com.p2c.thelife.model.FriendModel;

/**
 * Create an change threshold event. Uses a dialog fragment as per Android doc, using support library for Androids < 3.0.
 * @author clarence
 *
 */
public class ChangeThresholdDialog extends ServerAccessDialogAbstract {
	
	private static final String TAG = "ChangeThresholdDialog";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		
		final FriendModel friend = ((DeedsForFriendActivity)m_listener).getSelectedFriend();		
		final DeedModel deed = ((DeedsForFriendActivity)m_listener).getSelectedDeed();
				
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.dialog_change_threshold, null);
		
		// set the message and content of the alert
		alertBuilder.setMessage(R.string.change_threshold_prompt);
		alertBuilder.setView(view);

		// set the buttons of the alert
		alertBuilder.setNegativeButton(R.string.cancel, null);
		alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface di, int which) {
				// enable a progress bar
				((Listener)m_listener).notifyAttemptingServerAccess("createEvent");

				FriendModel.Threshold threshold = getThreshold(deed, view);				
				Server server = new Server(getActivity());
				server.createEvent(deed.id, friend.id, true, threshold, (Server.ServerListener)m_listener, "createEvent");						
			}
		});		
		
		return alertBuilder.create();				
	}
		
	
	/**
	 * Helper routine to get the threshold enum from the view.
	 */
	private FriendModel.Threshold getThreshold(DeedModel deed, View view) {
		Spinner thresholdField = (Spinner)view.findViewById(R.id.change_threshold);
		return FriendModel.thresholdValues[thresholdField.getSelectedItemPosition() + 1];  // add 1 because the first threshold, NewContact, is not shown
	}

}
