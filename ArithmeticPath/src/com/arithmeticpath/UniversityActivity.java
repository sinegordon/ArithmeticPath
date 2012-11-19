package com.arithmeticpath;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class UniversityActivity extends Activity implements OnClickListener {

	private static Game game = null;
	
	public static void setGame(Game game) {
		UniversityActivity.game = game;
	}
	
	@Override
	public void onClick(View view) {
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.select_game);
    }

}
