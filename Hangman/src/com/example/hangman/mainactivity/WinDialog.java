package com.example.hangman.mainactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WinDialog extends DialogFragment 
{
	private final DialogListener listener;		
	private final String msg;
	
	public WinDialog(DialogListener listener, String word, int score)
	{
		this.listener = listener;
		msg = "Congratulations! You have guessed: " + word + " for " + 
				Integer.toString(score) + "points. Want to play again?";
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