package com.arithmeticpath;


import java.util.Date;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.content.Context;

//Все координаты в долях высоты и ширины канвы (то есть от 0 до 1)
public class GameNode {
	// Контекст приложения
	private Context context = null;
	// Шрифт
	Typeface typeface = null;
	// Тип узла
	// 0 - число
	// 1 - сложение
	// 2 - вычитание
	// 3 - умножение
	private int type = -1;
	// Размеры узла
	private double sizex = 0.2;
	private double sizey = 0.2;
	// Данные узла (имеют смысл, если узел числовой)
	private int data = -1;
	// Координаты узла
	private double x = 0.0;
	private double y = 0.0;
	// Возвращаем тип узла
	public int getType () {
	     return this.type;
	}
	// Возвращаем данные узла
	public int getData () {
	    return this.data;
	}
	// Возвращаем координату x узла
	public double getX () {
	    return this.x;
	}
	// Возвращаем координату x узла
	public double getY () {
	    return this.y;
	}
	// Возвращаем размеры узла
	public double getSizeX() {
	    return this.sizex;
	}
	public double getSizeY() {
	    return this.sizey;
	}
	// Устанавливаем размеры узла
	public void setSizeX(double sizex) {
	    this.sizex = sizex;
	}
	public void setSizeY(double sizey) {
	    this.sizey = sizey;
	}
	// Возвращаем строковое представление данных
	public String toString() {
	    if (this.type == 0)
	        return Integer.toString(data);
	    if (this.type == 1)
	        return "+";
	    if (this.type == 2)
	        return "-";
	    if (this.type == 3)
	        return "*";
	    return null;
	}
	// Определяем попадание точки x, y в узел
	public boolean isInside(double x, double y) {
	    if (x <= (this.x + this.sizex / 2) && x >= (this.x - this.sizex / 2 ) 
	        && y <= (this.y + this.sizey / 2) && y >= (this.y - this.sizey / 2))
	        return true;
	    else
	        return false;
	}
	// Прорисовываем элемент на canvas
	public void draw(Canvas canvas, int backColor, int fontColor) {
	    int xx = (int) Math.round(this.x * canvas.getWidth());
	    int yy = (int) Math.round(this.y * canvas.getHeight());
	    int w = (int) Math.round(this.sizex * canvas.getWidth());
	    int h = (int) Math.round(this.sizey * canvas.getHeight());
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setColor(backColor);
	    Rect rect = new Rect();
	    rect.set(xx - w / 2, yy - h / 2, xx + w / 2, yy + h / 2);
	    canvas.drawRect(rect, paint);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    paint.setStrokeWidth(5);
	    paint.setColor(fontColor);
	    paint.setTypeface(typeface);
	    paint.setTextSize(h/2);
	    canvas.drawText(this.toString(), xx - w/6, yy + h/5, paint);
	}
	
	public GameNode(Context context, int type, int data, double x, double y) {
		this.type = type;
		this.data = data;
		this.x = x;
		this.y = y;
		this.context = context;
		this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/PTC55F.ttf");
	}
}
