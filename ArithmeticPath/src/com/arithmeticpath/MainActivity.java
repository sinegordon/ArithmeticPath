package com.arithmeticpath;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener,OnTouchListener {

	static private Game game = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game(getApplicationContext());
        findViewById(R.id.test).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.load).setOnClickListener(this);
        findViewById(R.id.test1).setOnTouchListener(this);
		game.setCurrentRange(0);
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.test:
			game.clearSettings();
			try {
				game.LoadGame();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			break;
		case R.id.save:
			game.SaveGame();
			break;
		case R.id.load:
			try {
				game.LoadGame();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		default:
			break;
		}
		SurfaceView v = (SurfaceView)findViewById(R.id.test1);
		game.setGamma(Color.BLACK, Color.GREEN, Color.RED, Color.RED);
		Canvas canvas = v.getHolder().lockCanvas();
		canvas.drawRGB(4, 145, 132);
		try {
			game.draw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		v.getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.test1:
			game.move(event.getX() / view.getWidth(), event.getY() / view.getHeight());
			break;
		default:
			break;
		}
		SurfaceView v = (SurfaceView)findViewById(R.id.test1);
		game.setGamma(Color.BLACK, Color.GREEN, Color.RED, Color.RED);
		Canvas canvas = v.getHolder().lockCanvas();
		canvas.drawRGB(4, 145, 132);
		try {
			game.draw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		v.getHolder().unlockCanvasAndPost(canvas);
		TextView tv = (TextView)findViewById(R.id.count);
		if (game.needResult().equals(game.nowResult()))
			tv.setText("������!!!");
		else
			tv.setText("�����: " + game.needResult() + " ������: " + game.nowResult());
		
		return false;
	}
}
