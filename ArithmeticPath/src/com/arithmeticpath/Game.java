package com.arithmeticpath;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class Game {
	// �������� ����������
	private Context context = null; 
	
	// ������ �������� �����
	private GameGraph gameGraph = null;
	
	// ���������� ������� ����� � ��������
	private int countRanges = 0;
	
	//������ ���������� ����������� � ������� �������� ������ 
	private int lastDoneLevelIndex = -1;
	
	// ������ ������� � ��������� �� ������ ����� (����� ������� countRanges)
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
        // ���������� ���������� ������� �����
        int ind = reslevels[reslevels.length-1].indexOf("_");
        countRanges = Integer.parseInt(reslevels[reslevels.length-1].substring(0, ind)) + 1;
        // ������� ������ ������� ��� ������� ������ �� ������� �����
        for (int i = 0; i < countRanges; i++)
        	levels.add(new ArrayList<String>());
        // ��������� ������� ������� ������� �� ������� �����
        for(int i = 0; i < reslevels.length; i++) {
        	ind = reslevels[i].indexOf("_");
        	int r = Integer.parseInt(reslevels[i].substring(0, ind));
        	String str = reslevels[i].substring(ind + 1);
        	levels.get(r).add(str);
        }
        // ������ ��� ������������ �� ���������
        userName = context.getResources().getString(R.string.default_user_name);
        // ������� ������ ����
        gameGraph = new GameGraph(context);
    }
    
    // ��������� ���� (� �������� ����������� ������� �������)
    public void SaveGame() {
        SharedPreferences.Editor editor = settings.edit();
        if( currentRange < countRanges && currentRange > -1 && 
        	currentLevel < levels.get(currentRange).size() && currentLevel > -1) {
        	editor.putString(userName + "game" + Integer.toString(currentRange), gameGraph.getGraph());
        	editor.putInt(userName + "lastDoneLevelIndex" + Integer.toString(currentRange), currentLevel - 1);
        	editor.putInt(userName + "currentLevel" + Integer.toString(currentRange), currentLevel);
        };
        if( currentRange == -1)
        	editor.putString(userName + "game", gameGraph.getGraph());
        editor.commit();
    }
    
    // ��������� ���� (� �������� ����������� ������� �������)
    public void LoadGame() throws Exception {
        if( currentRange < countRanges && currentRange > -1 ) {   	
        	String game = settings.getString(userName + "game" + Integer.toString(currentRange) , "");
        	lastDoneLevelIndex = settings.getInt(userName + "lastDoneLevelIndex" + Integer.toString(currentRange) , -1);
        	currentLevel = settings.getInt(userName + "currentLevel" + Integer.toString(currentRange) , -1);
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
        	String game = settings.getString(userName + "game", "");
        	lastDoneLevelIndex = -1;
        	currentLevel = -1;
        	if (game.equals(""))
        		gameGraph = new GameGraph(context, 5, 5, freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
        	else {
        		gameGraph.setGraph(game);
        		gameGraph.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
        	}
        };
    }
       
    // ������������� ������� ������� �����
    public boolean setCurrentRange(int currentRange) {
    	if (currentRange < countRanges) {
    		this.currentRange = currentRange;
    		return true;
    	}
    	else
    		return false;
    }
    
    // ������������� ������� ������ �������� ������ �� ������� ������ �����
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
    
    // ���������� ������� ������� �����
    public int getCurrentRange() {
    	return currentRange;
    }

    // ���������� ������� ������ �������� ������ �� ������� ������� �����
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
    
    // ������������� ��� ������������
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    // ���������� ��� ������������
    public String getUserName() {
    	return userName;
    }
    
    // �������� ���������� ���� � �������� ����� sizex �� sizey, ������ ������������ userName �
    // ����������� result ������
    // ���������� ����� � ��� 10 (0, ���� �� �����)
    public int updateStatistics(int sizex, int sizey, double result) {
    	String table = settings.getString(Integer.toString(sizex) + "_" + Integer.toString(sizey) + "_records" , "");
    	String[] mastable = table.split("_");
    	// ���� ������� �� ������ ������ ����������� ������� - ������� ��
    	if(mastable.length == 0) {
    		mastable = new String[20];
    		for(int i = 0; i < 20; i++) {
    			if (i % 2 == 0)
    				mastable[i] = "John/Jane Doe";
    			else
    				mastable[i] = "1000000.000";
    		}
    	}
    	// ���� ���� �� �������� ����� ���������
    	// ���� �� ���� ������� - �������
    	if(Double.parseDouble(mastable[19]) < result)
    		return 0;
    	else {
    		//  ����� �������� ���� ������ � ����� ���� �� ������ ������� ������� ���������� ������
    		mastable[19] = Double.toString(result);
    		mastable[18] = userName;
    		int k = 10;// ������� �����
    		for(int i = 17; i > -1; i-=2) {
    			// ���� ��������� ������ ������ ������ ���������� - ������� ������������ �� �������
    			if(Double.parseDouble(mastable[i]) > result) {
    	    		mastable[i+2] = mastable[i];
    	    		mastable[i+1] = mastable[i-1];    				
    	    		mastable[i] = Double.toString(result);
    	    		mastable[i-1] = userName;
    	    		k -= 1;
    			}
    			// ���� ��� - ��������� ���������� � �������
    			else {
    				table = "";
    	    		for(int j = 0; j < 19; j++) {
   	    				table += mastable[j] + "_";
    	    		}
    	    		table += mastable[19];
    				SharedPreferences.Editor editor = settings.edit();
    	        	editor.putString(Integer.toString(sizex) + "_" + Integer.toString(sizey) + "_records", table);
    	            editor.commit();
    				return k;
    			}
    		}
    	}    		
    	return 0;
    }

}
