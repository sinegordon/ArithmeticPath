package com.arithmeticpath;

import java.util.ArrayList;
import java.util.Dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;

public class Game {
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
	
	// ���� ������ �� ������� ������
	int fontColor = Color.argb(0, 0, 0, 0);
    
    // ����������� ����
    public Game(Context context) {
    	Resources res = context.getResources();
    	String[] reslevels = res.getStringArray(R.array.levels);
    	String prefsName = res.getString(R.string.prefs_name);
        settings = context.getSharedPreferences(prefsName, 0);
        this.levels = new ArrayList<ArrayList<String>>();
        // ���������� ���������� ������� ���������
        int ind = reslevels[reslevels.length-1].indexOf("_");
        countRanges = Integer.parseInt(reslevels[reslevels.length-1].substring(0, ind-1)) + 1;
        // ������� ������ ������� ��� ������� ������ �� ������� ���������
        for (int i = 0; i < countRanges; i++)
        	levels.add(new ArrayList<String>());
        // ��������� ������� ������� ������� �� ������� ���������
        for(int i = 0; i < reslevels.length; i++) {
        	ind = reslevels[i].indexOf("_");
        	int r = Integer.parseInt(reslevels[reslevels.length-1].substring(0, ind-1));
        	String str = reslevels[reslevels.length-1].substring(ind + 1);
        	levels.get(ind).add(str);
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
        		gameGraph.setGraph(levels.get(currentRange).get(0));
        	}
        	else
        		gameGraph.setGraph(game);
        	gameGraph.SetGamma(freeNodeColor, busyNodeColor, fontColor);
        };
        if (currentRange == -1) {
        	String game = settings.getString("game", "");
        	lastDoneLevelIndex = -1;
        	currentLevel = -1;
        	if (game.equals(""))
        		gameGraph = new GameGraph(3, 3, freeNodeColor, busyNodeColor, fontColor);
        	else {
        		gameGraph.setGraph(game);
        		gameGraph.SetGamma(freeNodeColor, busyNodeColor, fontColor);
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


}
