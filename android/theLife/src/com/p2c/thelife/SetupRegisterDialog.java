package com.p2c.thelife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Register the user for a new account. Uses a dialog fragment as per Android doc, using support library for Androids < 3.0.
 * @author clarence
 *
 */
public class SetupRegisterDialog extends DialogFragment {
	
	private static final String TAG = "SetupRegisterDialog";
	
	public interface Listener {
		public void notifyAttemptingRegister();
	}	
	
	private Object m_listener = null;
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.dialog_setup_register, null);
			
		// make the dialog
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
			
		// set the message, content and buttons of the alert
		alertBuilder.setMessage(R.string.register_prompt);
		alertBuilder.setView(view);
		alertBuilder.setNegativeButton(R.string.cancel, null); 
		alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText usernameField = (EditText)view.findViewById(R.id.setup_register_username);
				String username = usernameField.getText().toString();
				EditText passwordField = (EditText)view.findViewById(R.id.setup_register_password);
				String password = passwordField.getText().toString();
				EditText firstNameField = (EditText)view.findViewById(R.id.setup_register_first_name);
				String firstName = firstNameField.getText().toString();
				EditText lastNameField = (EditText)view.findViewById(R.id.setup_register_last_name);
				String lastName = lastNameField.getText().toString();				
							
				// enable a progress bar
				((Listener)m_listener).notifyAttemptingRegister();

				Server server = new Server();
				server.register(username, password, firstName, lastName, (Server.ServerListener)m_listener, "register");
			}
		}); 
		
		return alertBuilder.create();				
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		m_listener = (Server.ServerListener)activity;
	}
	

}
