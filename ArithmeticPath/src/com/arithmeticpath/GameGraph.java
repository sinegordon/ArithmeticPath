package com.arithmeticpath;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class GameGraph {
	// Все координаты в долях высоты и ширины канвы (то есть от 0 до 1)
    // Массив узлов
	private ArrayList<GameNode> nodes = null;
	// Список смежности узлов
	private ArrayList<ArrayList<Integer>> neighbours = null;
	// Список индексов пройденных узлов
	private ArrayList<Integer> path = null;
	// Размеры игрового поля в фишках
	int sizex = 0;
	int sizey = 0;
	// Цвета фонов узлов
	// Цвет свободного узла
	int freeNodeColor = Color.argb(0, 0, 0, 0);
	// Цвет занятого узла
	int busyNodeColor = Color.argb(0, 0, 0, 0);
	// Цвет шрифта
	int fontColor = 0;
	
	// Конструктор
	public GameGraph(int sizex, int sizey, int freeNodeColor, int busyNodeColor, int fontColor) throws Exception {
		this.nodes = new ArrayList<GameNode>();
	    this.neighbours  = new ArrayList<ArrayList<Integer>>();
	    this.path = new ArrayList<Integer>();
	    this.busyNodeColor = busyNodeColor;
	    this.freeNodeColor = freeNodeColor;
	    this.fontColor = fontColor;
	    if (sizex % 2 == 0 || sizey % 2 == 0)
	    	throw new Exception();
	    else {
		    this.sizex = sizex;
		    this.sizey = sizey;
	    }    
	    // Шаги
	    double stepx = 1.0 / sizex;
	    double stepy = 1.0 / sizey;
	    double line = 0.01;
	    double width = stepx - line;
	    double height = stepy - line;
	    // Заполняем список узлов игрового графа
	    for (int i = 0; i < sizey; i++)
	        for (int j = 0; j < sizex; j++) {
	            if ((i * sizex + j) % 2 == 0) {
	                int max = 9;
	                int data = (int) (max * Math.random());
	                while (data == 0)
	                    data = (int) (max * Math.random());
	                GameNode node = new GameNode(0, data, stepx / 2 + j * stepx, stepy / 2 + i * stepy);
		            node.setSizeX(width);
		            node.setSizeY(height);
		            addNode(node);
	            }
	            else {
	                int type = (int)(3 * Math.random());
	                while (type == 0)
	                    type = (int)(3 * Math.random());
	                GameNode node = new GameNode(type, 0, stepx / 2 + j * stepx, stepx / 2 + i * stepy);
		            node.setSizeX(width);
		            node.setSizeY(height);
		            addNode(node);
	            };

	        }
	    // Заполняем список соседей игрового графа
	    addNodeInPath(0);
	    for (int i = 0; i < sizey; i++)
	        for (int j = 0; j < sizex; j++) {
	            if (i == 0 && j == 0) {
	                addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j + 1), (i * sizex + j));
	                continue;
	            }
	            if (i == (sizey - 1) && j == 0) {
	                addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j + 1), (i * sizex + j));
	                continue;
	            }
	            if (i == 0 && j == (sizex - 1)) {
	                addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j - 1), (i * sizex + j));
	                continue;
	            }
	            if (i == (sizey - 1) && j == (sizex - 1)) {
	                addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j - 1), (i * sizex + j));
	                continue;
	            }
	            if (i == 0) {
	                addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j - 1), (i * sizex + j));
	                addNeighbour((i * sizex + j + 1), (i * sizex + j));
	                continue;
	            }
	            if (i == (sizey - 1)) {
	                addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j - 1), (i * sizex + j));
	                addNeighbour((i * sizex + j + 1), (i * sizex + j));
	                continue;
	            }
	            if (j == 0) {
	                addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	                addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j + 1), (i * sizex + j));
	                continue;
	            }
	            if (j == (sizex - 1)) {
	                addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	                addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	                addNeighbour((i * sizex + j - 1), (i * sizex + j));
	                continue;
	            };
	            addNeighbour(((i - 1) * sizex + j), (i * sizex + j));
	            addNeighbour(((i + 1) * sizex + j), (i * sizex + j));
	            addNeighbour((i * sizex + j - 1), (i * sizex + j));
	            addNeighbour((i * sizex + j + 1), (i * sizex + j));
	        }
	}
	
    // Добавляем узел node
    public void addNode(GameNode node) {
    	this.nodes.add(node);
    	this.neighbours.add(new ArrayList<Integer>());
    }
    
    // Добавить соседа с индексом nei к узлу с индексом nodeIndex
    public void addNeighbour(int nei, int nodeIndex) {
        if ((this.nodes.get(nodeIndex).getType() == 0 && this.nodes.get(nei).getType() > 0) ||
            (this.nodes.get(nodeIndex).getType() > 0 && this.nodes.get(nei).getType() == 0))
            this.neighbours.get(nodeIndex).add(nei);
    }
    
    // Добавить узел в путь
    public int addNodeInPath(int nodeIndex) {
    	// Если первый узел не число - выходим
        if (this.path.size() == 0 && this.nodes.get(nodeIndex).getType() > 0)
            return -1;
        // Если первый узел число - вставляем
        if (this.path.size() == 0) {
            this.path.add(nodeIndex);
            return 0;
        }
        // Если узел уже в пути - откат пути до этого узла
        if (inPath(nodeIndex)) {
            ArrayList<Integer> mas = new ArrayList<Integer>();
            for (int i = 0; this.path.get(i) != nodeIndex; i++)
                mas.add(this.path.get(i));
            mas.add(nodeIndex);
            this.path = mas;
            return 0;
        }
        // Если узел - сосед справа или внизу - заносим
        ArrayList<Integer> nei = this.neighbours.get(this.path.get(this.path.size() - 1));
        for (int i = 0; i < nei.size(); i++) {
            if (nei.get(i) == nodeIndex) {
                this.path.add(nodeIndex);
                return 0;
            }
        }
        // Если узел на одной вертикали внизу или горизонтали справа
        // - заносим все узлы этой горизонтали или вертикали
        int lastNodeIndexInPath = this.path.get(this.path.size() - 1);
        int ilast = lastNodeIndexInPath / this.sizex;
        int jlast = lastNodeIndexInPath % this.sizex;
        int inew = nodeIndex / this.sizex;
        int jnew = nodeIndex % this.sizex;
        if (ilast == inew && jnew > jlast) {
        	for(int j = jlast + 1; j <= jnew; j++)
        		this.path.add(this.sizex * ilast + j);
        	return 0;
        };
        if(jlast == jnew && inew > ilast){
        	for(int i = ilast + 1; i <= inew; i++)
        		this.path.add(this.sizex * i + jlast);
        	return 0;
        }
        return -1;
    }

    // Текущий набранный результат
    public int nowResult() {
        int res = 0;
        if (this.path.size() == 0)
            return Integer.MAX_VALUE;
        else
            res = this.nodes.get(0).getData();
        for (int i = 2; i < this.path.size(); i += 2) {
            if (this.nodes.get(this.path.get(i - 1)).getType() == 1)
                res += this.nodes.get(this.path.get(i)).getData();
            if (this.nodes.get(this.path.get(i - 1)).getType() == 2)
                res -= this.nodes.get(this.path.get(i)).getData();
            if (this.nodes.get(this.path.get(i - 1)).getType() == 3)
                res *= this.nodes.get(this.path.get(i)).getData();
        }
        return res;
    }

    // Текущее строковое представление
    public String toString() {
        if (this.path.size() == 0)
            return "Пусто";
        if (this.nodes.get(this.path.size() - 1).getType() == 0)
            return Integer.toString(this.nowResult());
        else
            return Integer.toString(this.nowResult()) + " " + this.nodes.get(this.path.get(this.path.size() - 1)).toString();
    }

    // Возвращаем узел в который попадает точка x, y
    public GameNode getNodeXY(double x, double y) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).isInside(x, y)) {
                return this.nodes.get(i);
            }
        return null;
    }

    // Возвращаем индекс узла в который попадает точка x, y
    public int getNodeIndexXY(double x, double y) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).isInside(x, y)) {
                return i;
            }
        return -1;
    }

    // Отрисовываем GameGraph на canvas
    public void draw(Canvas canvas) {
        // Рисуем узлы графа
        for (int i = 0; i < this.nodes.size(); i++) {
            if (inPath(i)) {
                this.nodes.get(i).draw(canvas, this.busyNodeColor, this.fontColor);
            }
            else
                this.nodes.get(i).draw(canvas, this.freeNodeColor, this.fontColor);
        }
    }

    // Входит ли узел в путь
    public boolean inPath(int nodeIndex) {
        for (int i = 0; i < this.path.size(); i++) {
            if (this.path.get(i) == nodeIndex)
                return true;
        };
        return false;
    }

    // Очищаем путь
    public void clearPath() {
        this.path.clear();
        this.path.add(0);
    }

    // Выдаем последний индекс пути
    public int getLastPathIndex() {
        if (this.path.size() == 0)
            return -1;
        return this.path.get(this.path.size() - 1);
    }	
}
