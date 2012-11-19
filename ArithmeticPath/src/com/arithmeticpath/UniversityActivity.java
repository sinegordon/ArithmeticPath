package com.arithmeticpath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class UniversityActivity extends Activity implements OnClickListener, OnTouchListener {

	private static Game game = null;
	
	private SurfaceView gameview = null;
	
	public static void setGame(Game game) {
		UniversityActivity.game = game;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn2:
			finish();
		default:
			break;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.university);
        gameview = (SurfaceView)findViewById(R.id.surfaceView1);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;
	}

}
