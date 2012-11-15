package com.arithmeticpath;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

//��� ���������� � ����� ������ � ������ ����� (�� ���� �� 0 �� 1)
public class GameNode {
	// ��� ����
	// 0 - �����
	// 1 - ��������
	// 2 - ���������
	// 3 - ���������
	private int type = -1;
	// ������� ����
	private double sizex = 0.2;
	private double sizey = 0.2;
	// ������ ���� (����� �����, ���� ���� ��������)
	private int data = -1;
	// ���������� ����
	private double x = 0.0;
	private double y = 0.0;
	// ���������� ��� ����
	public int getType () {
	     return this.type;
	}
	// ���������� ������ ����
	public int getData () {
	    return this.data;
	}
	// ���������� ���������� x ����
	public double getX () {
	    return this.x;
	}
	// ���������� ���������� x ����
	public double getY () {
	    return this.y;
	}
	// ���������� ������� ����
	public double getSizeX() {
	    return this.sizex;
	}
	public double getSizeY() {
	    return this.sizey;
	}
	// ������������� ������� ����
	public void setSizeX(double sizex) {
	    this.sizex = sizex;
	}
	public void setSizeY(double sizey) {
	    this.sizey = sizey;
	}
	// ���������� ��������� ������������� ������
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
	// ���������� ��������� ����� x, y � ����
	public boolean isInside(double x, double y) {
	    if (x <= (this.x + this.sizex / 2) && x >= (this.x - this.sizex / 2 ) 
	        && y <= (this.y + this.sizey / 2) && y >= (this.y - this.sizey / 2))
	        return true;
	    else
	        return false;
	}
	// ������������� ������� �� canvas
	public void draw(Canvas canvas, int backColor, int fontColor) {
	    int xx = (int) Math.round(this.x * canvas.getWidth());
	    int yy = (int) Math.round(this.y * canvas.getHeight());
	    int w = (int) Math.round(this.sizex * canvas.getWidth());
	    int h = (int) Math.round(this.sizey * canvas.getHeight());
	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL);
	    //canvas = view.getHolder().lockCanvas();
	    paint.setColor(backColor);
	    Rect rect = new Rect();
	    rect.set(xx - w / 2, yy - h / 2, xx + w / 2, yy + h / 2);
	    canvas.drawRect(rect, paint);
	    paint.setColor(fontColor);
	    canvas.drawText(this.toString(), xx, yy, paint);
	}
	
	public GameNode(int type, int data, double x, double y) {
		this.type = type;
		this.data = data;
		this.x = x;
		this.y = y;
	}
}
