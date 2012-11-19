package com.arithmeticpath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectGameActivity extends Activity implements OnClickListener {

	private static Game game = null;
	
	public static void setGame(Game game) {
		SelectGameActivity.game = game;
	}
	
	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.UniversityButton:
			game.setCurrentRange(-1);
			game.setCurrentLevel(-1);
	    	intent = new Intent(this, UniversityActivity.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(intent);
			break;
		case R.id.SchoolButton:
			break;
		case R.id.btn2:
			finish();
		default:
			break;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_game);
        findViewById(R.id.UniversityButton).setOnClickListener(this);
        findViewById(R.id.SchoolButton).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

}
