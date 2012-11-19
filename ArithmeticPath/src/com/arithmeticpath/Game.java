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
	// Контекст приложения
	private Context context = null; 
	
	// Игровой объект
	private GameGraph gameGraph = null;
	
	// Количество уровней сложности в кампании
	private int countRanges = 0;
	
	//Индекс последнего пройденного в текущей кампании уровня 
	private int lastDoneLevelIndex = -1;
	
	// Массив уровней в кампаниях по рангам сложностей (длина массива countRanges)
	private ArrayList<ArrayList<String>> levels = null;
	
	// Имя пользователя
	private String userName = "";
	
	// Объект настроек приложения
    private SharedPreferences settings = null;
    
    // Текущий индекс уровня сложности
    private int currentRange = -1;
    
    // Текущий индекс игрового уровня на уровне сложности
    private int currentLevel = -1;
    
	// Цвета фонов узлов на текущем уровне
	// Цвет свободного узла
    int freeNodeColor = Color.argb(0, 0, 0, 0);
	
	// Цвет занятого узла
	int busyNodeColor = Color.argb(0, 0, 0, 0);
	
	// Цвет шрифта свободной клетки на текущем уровне
	int freeFontColor = Color.argb(0, 0, 0, 0);

	// Цвет шрифта занятой клетки на текущем уровне
	int busyFontColor = Color.argb(0, 0, 0, 0);

    // Конструктор игры
    public Game(Context context) {
    	this.context = context;
    	Resources res = context.getResources();
    	String[] reslevels = res.getStringArray(R.array.levels);
    	String prefsName = res.getString(R.string.prefs_name);
        settings = context.getSharedPreferences(prefsName, 0);
        this.levels = new ArrayList<ArrayList<String>>();
        // Определяем количество уровней сложности
        int ind = reslevels[reslevels.length-1].indexOf("_");
        countRanges = Integer.parseInt(reslevels[reslevels.length-1].substring(0, ind)) + 1;
        // Создаем пустые массивы под игровые уровни на уровнях сложности
        for (int i = 0; i < countRanges; i++)
        	levels.add(new ArrayList<String>());
        // Заполняем массивы игровых уровней на уровнях сложности
        for(int i = 0; i < reslevels.length; i++) {
        	ind = reslevels[i].indexOf("_");
        	int r = Integer.parseInt(reslevels[i].substring(0, ind));
        	String str = reslevels[i].substring(ind + 1);
        	levels.get(r).add(str);
        }
        gameGraph = new GameGraph();
    }
    
    // Сохраняем игру (в кампании сохраняется текущий уровень)
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
    
    // Загружаем игру (в кампании загружается текущий уровень)
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
       
    // Устанавливаем текущий уровень сложности
    public boolean setCurrentRange(int currentRange) {
    	if (currentRange < countRanges) {
    		this.currentRange = currentRange;
    		return true;
    	}
    	else
    		return false;
    }
    
    // Устанавливаем текущий индекс игрового уровня на текущем уровне сложности
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
    
    // Возвращаем текущий уровень сложности
    public int getCurrentRange() {
    	return currentRange;
    }

    // Возвращаем текущий индекс игрового уровня на текущем уровене сложности
    public int getCurrentLevel() {
    	return currentLevel;
    }
    
    // Рисуем текущий уровень на канве
    public void draw(Canvas canvas) throws Exception {
    	try {
    		gameGraph.draw(canvas);
    	}
    	catch (Exception e) {
			Log.e("altavista", "Can't draw the game level!");
			throw e;
		}
    }
    
    // Устанавливаем текущую гамму отображения
    public void setGamma(int freeNodeColor, int busyNodeColor, int freeFontColor, int busyFontColor) {
	    this.busyNodeColor = busyNodeColor;
	    this.freeNodeColor = freeNodeColor;
	    this.freeFontColor = freeFontColor;
	    this.busyFontColor = busyFontColor;
	    gameGraph.setGamma(freeNodeColor, busyNodeColor, freeFontColor, busyFontColor);
    }
    
    // Делаем ход по клику мышкой в точку с координатами x, y
    public void move(double x, double y) {
    	int nodeIndex = gameGraph.getNodeIndexXY(x, y);
    	gameGraph.addNodeInPath(nodeIndex);
    }
    
    // Очистить прогресс
    public void clearSettings() {
    	SharedPreferences.Editor editor = settings.edit();
    	editor.clear();
    	editor.commit();
    }
    
    // Необходимо набрать на текущем игровом уровне
    public String needResult() {
    	return Integer.toString(gameGraph.rightResult());
    }

    // Результат на данный момент на текущем игровом уровне
    public String nowResult() {
    	return gameGraph.toString();
    }

}
