package com.arithmeticpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
		private static Game game = null;
		
		private int backColor = 0;

		public static void setGame(Game game) {
			GameView.game = game;
		}

		public GameView(final Context context) {
			super(context);
            setFocusable(true);
            getHolder().addCallback(this);
            requestFocus();
        }
		
		public GameView(final Context context, final AttributeSet attrs) {
	        super(context, attrs);
            setFocusable(true);
            getHolder().addCallback(this);
            requestFocus(); 
	    }		
		
		protected void draw() throws Exception {
			Canvas canvas = this.getHolder().lockCanvas();
			try {
				int r = Color.red(backColor);
				int g = Color.green(backColor);
				int b = Color.blue(backColor);
				canvas.drawRGB(r, g, b);
				game.draw(canvas);
			} finally {
				super.onDraw(canvas);
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}	
		
		public void setBackColor(int backColor) {
			this.backColor = backColor;
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			try {
				draw();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			try {
				draw();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
		}
		
}
