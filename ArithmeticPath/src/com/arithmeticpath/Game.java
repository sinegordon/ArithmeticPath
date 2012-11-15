package com.arithmeticpath;

import java.util.ArrayList;
import java.util.Dictionary;

import com.ditloids.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class Game {
	// ������� ������
	private GameGraph gameGraph = null;
	
	// ���������� ������� ��������� � ��������
	private int countRanges = 0;
	
	//�������� ���������� ����������� � ������� �������� ������ 
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
    
    // ����������� ����
    public Game(Context context) {
    	Resources res = context.getResources();
    	String[] levels = res.getStringArray(R.array.levels);
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
    public void LoadGame() {
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
        };
        if (currentRange == -1) {
        	String game = settings.getString("game", "");
        	lastDoneLevelIndex = -1;
        	currentLevel = -1;
        	if (game.equals(""))
        		gameGraph.setGraph(levels.get(currentRange).get(0));
        	else
        		gameGraph.setGraph(game);
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
    
    // ������������� ������� ������� �� ������� ���������
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
