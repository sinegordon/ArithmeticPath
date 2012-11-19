package com.arithmeticpath;

import java.util.ArrayList;
import java.util.Dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class Game {
	// �������� ����������
	private Context context = null; 
	
	// ������� ������
	private GameGraph gameGraph = null;
	
	// ���������� ������� ��������� � ��������
	private int countRanges = 0;
	
	//������ ���������� ����������� � ������� �������� ������ 
	private int lastDoneLevelIndex = -1;
	
	// ������ ������� � ��������� �� ������ ���������� (����� ������� countRanges)
	private ArrayList<ArrayList<String>> levels = null;
	
	// ��� ������������
	private String userName = "";
	
	// ������ �������� ����������
    private SharedPreferences settings = null;
    
    // ������� ������ ������ ���������
    private int currentRange = -1;
    
    // ������� ������ �������� ������ �� ������ ���������
    private int currentLevel = -1;
    
	// ����� ����� ����� �� ������� ������
	// ���� ���������� ����
    int freeNodeColor = Color.argb(0, 0, 0, 0);
	
	// ���� �������� ����
	int busyNodeColor = Color.argb(0, 0, 0, 0);
	
	// ���� ������ ��������� ������ �� ������� ������
	int freeFontColor = Color.argb(0, 0, 0, 0);

	// ���� ������ ������� ������ �� ������� ������
	int busyFontColor = Color.argb(0, 0, 0, 0);

    // ����������� ����
    public Game(Context context) {
    	this.context = context;
    	Resources res = context.getResources();
    	String[] reslevels = res.getStringArray(R.array.levels);
    	String prefsName = res.getString(R.string.prefs_name);
        settings = context.getSharedPreferences(prefsName, 0);
        this.levels = new ArrayList<ArrayList<String>>();
        // ���������� ���������� ������� ���������
        int ind = reslevels[reslevels.length-1].indexOf("_");
        countRanges = Integer.parseInt(reslevels[reslevels.length-1].substring(0, ind)) + 1;
        // ������� ������ ������� ��� ������� ������ �� ������� ���������
        for (int i = 0; i < countRanges; i++)
        	levels.add(new ArrayList<String>());
        // ��������� ������� ������� ������� �� ������� ���������
        for(int i = 0; i < reslevels.length; i++) {
        	ind = reslevels[i].indexOf("_");
        	int r = Integer.parseInt(reslevels[i].substring(0, ind));
        	String str = reslevels[i].substring(ind + 1);
        	levels.get(r).add(str);
        }
        gameGraph = new GameGraph();
    }
    
    // ��������� ���� (� �������� ����������� ������� �������)
    public void SaveGame() {
        SharedPreferences.Editor editor = settings.edit();
        if( currentRange < countRanges && currentRange > -1 && 
        	currentLevel < levels.get(currentRange).size() && currentLevel > -1) {
        	editor.putString("game" + Integer.toString(currentRange), gameGraph.getGraph());
        	editor.putInt("lastDoneLevelIndex" + Integer.toString(currentRange), currentLevel - 1);
        	editor.putInt("currentLevel" + Integer.toString(currentRange), currentLevel);
        };
        if( currentRange == -1)
        	editor.putString("game", gameGraph.getGraph());
        editor.commit();
    }
    
    // ��������� ���� (� �������� ����������� ������� �������)
    public void LoadGame() throws Exception {
        if( currentRange < countRanges && currentRange > -1 ) {   	
        	String game = settings.getString("game" + Integer.toString(currentRange) , "");
        	lastDoneLevelIndex = settings.getInt("lastDoneLevelIndex" + Integer.toString(currentRange) , -1);
        	currentLevel = settings.getInt("currentLevel" + Integer.toString(currentRange) , -1);
        	if (game.equals("")) {
        		currentLevel = 0;
        		String str = levels.get(currentRange).get(0);
        		gameGraph.setGraph(str);
        	}
        	else
        		gameGraph.setGraph(game);
        	gameGraph.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
        };
        if (currentRange == -1) {
        	String game = settings.getString("game", "");
        	lastDoneLevelIndex = -1;
        	currentLevel = -1;
        	if (game.equals(""))
        		gameGraph = new GameGraph(5, 5, freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
        	else {
        		gameGraph.setGraph(game);
        		gameGraph.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
        	}
        };
    }
       
    // ������������� ������� ������� ���������
    public boolean setCurrentRange(int currentRange) {
    	if (currentRange < countRanges) {
    		this.currentRange = currentRange;
    		return true;
    	}
    	else
    		return false;
    }
    
    // ������������� ������� ������ �������� ������ �� ������� ������ ���������
    public boolean setCurrentLevel(int currentLevel) {
    	if ( currentRange < levels.size() &&
    		 currentRange > -1 &&
    		 currentLevel < levels.get(currentRange).size()) {
    		this.currentLevel = currentLevel;
    		return true;
    	}
    	else
    		return false;
    }
    
    // ���������� ������� ������� ���������
    public int getCurrentRange() {
    	return currentRange;
    }

    // ���������� ������� ������ �������� ������ �� ������� ������� ���������
    public int getCurrentLevel() {
    	return currentLevel;
    }
    
    // ������ ������� ������� �� �����
    public void draw(Canvas canvas) throws Exception {
    	try {
    		gameGraph.draw(canvas);
    	}
    	catch (Exception e) {
			Log.e("altavista", "Can't draw the game level!");
			throw e;
		}
    }
    
    // ������������� ������� ����� �����������
    public void setGamma(int freeNodeColor, int busyNodeColor, int freeFontColor, int busyFontColor) {
	    this.busyNodeColor = busyNodeColor;
	    this.freeNodeColor = freeNodeColor;
	    this.freeFontColor = freeFontColor;
	    this.busyFontColor = busyFontColor;
	    gameGraph.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
    }
    
    // ������ ��� �� ����� ������ � ����� � ������������ x, y
    public void move(double x, double y) {
    	int nodeIndex = gameGraph.getNodeIndexXY(x, y);
    	gameGraph.addNodeInPath(nodeIndex);
    }
    
    // �������� ��������
    public void clearSettings() {
    	SharedPreferences.Editor editor = settings.edit();
    	editor.clear();
    	editor.commit();
    }
    
    // ���������� ������� �� ������� ������� ������
    public String needResult() {
    	return Integer.toString(gameGraph.rightResult());
    }

    // ��������� �� ������ ������ �� ������� ������� ������
    public String nowResult() {
    	return gameGraph.toString();
    }

}
