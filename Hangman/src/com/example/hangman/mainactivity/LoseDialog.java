package com.example.hangman.mainactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class LoseDialog extends DialogFragment 
{
	final private DialogListener listener;	
	final private String msg;
		
	public LoseDialog(DialogListener listener, String word)
	{
		this.listener = listener;
		msg = "You have failed to guess the word: " + word + ". Want to play again?";
		setCancelable(false);
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {       	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
               .setPositiveButton("New game", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                       listener.onNewGameSelect();
                   }
               })
               .setNegativeButton("Highscores", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                	   listener.onHighscoreSelect();                       
                   }
               });        
        return builder.create();
    }
}