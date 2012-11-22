package com.arithmeticpath;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UniversityActivity extends Activity implements OnTouchListener, OnClickListener {

	private static Game game = null;
	
	// Секция таймера
	private Timer timer = new Timer();
	private class UITimerTask extends TimerTask {
		private long startTime = System.currentTimeMillis();
		// Возвращаем время в секундах от запуска таймера
		public double getTime() {
			long currentTime = System.currentTimeMillis();
			return (currentTime - startTime)/1000d;
		}
        @Override
        public void run() {
            UniversityActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	long currentTime = System.currentTimeMillis();
                	String timeStr = Double.toString((currentTime - startTime)/1000d);
                	int dotIndex = timeStr.indexOf(".");
                	((TextView)findViewById(R.id.timertext)).setText(timeStr.subSequence(0, dotIndex+2));
                }
            });
        }
   };
   // Конец таймера
	
	public static void setGame(Game game) {
		UniversityActivity.game = game;
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn2:
			finish();
			break;
		default:
			break;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		try {
			game.LoadGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
        setContentView(R.layout.university);
        findViewById(R.id.btn2).setOnClickListener(this);
        ((GameView)findViewById(R.id.gameview)).setOnTouchListener(this);
        ((GameView)findViewById(R.id.gameview)).setBackColor(Color.argb(0xff, 0x00, 0x99, 0xcc));
		((TextView)findViewById(R.id.target)).setText(getResources().getString(R.string.target_text) + " " + game.needResult());
		((TextView)findViewById(R.id.now)).setText(getResources().getString(R.string.now_text) + " " + game.nowResult());
		((TextView)findViewById(R.id.username)).setText(game.getUserName());
		int freeNodeColor = Color.argb(0xff, 0x33, 0xb5, 0xe5);
        int busyNodeColor = Color.argb(0xff, 0xff, 0xff, 0xff);
        int freeFontColor = Color.argb(0xff, 0xff, 0xff, 0xff);
        int busyFontColor = Color.argb(0xff, 0x33, 0xb5, 0xe5);
		game.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
		timer.schedule(new UITimerTask(), 0, 100);
    }


	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (view.getId()) {
		case R.id.gameview:
			game.move(event.getX() / view.getWidth(), event.getY() / view.getHeight());
			try {
				((GameView)findViewById(R.id.gameview)).draw();
			} catch (Exception e) {
				e.printStackTrace();
			}
			((TextView)findViewById(R.id.now)).setText(getResources().getText(R.string.now_text) + " " + game.nowResult());
			break;
		default:
			break;
		}
		return false;
	}
}
