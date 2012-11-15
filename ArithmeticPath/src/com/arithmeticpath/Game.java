package com.arithmeticpath;

import java.util.ArrayList;
import java.util.Dictionary;

import com.ditloids.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class Game {
	// Игровой объект
	private GameGraph gameGraph = null;
	
	// Количество уровней сложности в кампании
	private int countRanges = 0;
	
	//Индексов последнего пройденного в текущей кампании уровня 
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
    
    // Конструктор игры
    public Game(Context context) {
    	Resources res = context.getResources();
    	String[] levels = res.getStringArray(R.array.levels);
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
       
    // Устанавливаем текущий уровень сложности
    public boolean setCurrentRange(int currentRange) {
    	if (currentRange < countRanges) {
    		this.currentRange = currentRange;
    		return true;
    	}
    	else
    		return false;
    }
    
    // Устанавливаем текущий уровень на текущем сложности
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
