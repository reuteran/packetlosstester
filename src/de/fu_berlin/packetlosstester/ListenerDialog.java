package de.fu_berlin.packetlosstester;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ListenerDialog extends DialogFragment {
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose neighbour to listen to");
        String[] links = this.getArguments().getStringArray("links");

        builder.setSingleChoiceItems(links,0, selectListener);
        return builder.create();

    }
    
    DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        	
        	
        }
      };
}