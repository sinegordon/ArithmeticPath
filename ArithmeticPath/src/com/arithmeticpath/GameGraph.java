package com.arithmeticpath;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameGraph {
	// ��� ���������� � ����� ������ � ������ ����� (�� ���� �� 0 �� 1)
    // ������ �����
	private ArrayList<GameNode> nodes = null;
	// ������ �������� ���������� �����
	private ArrayList<Integer> path = null;
	// ������ �������� ���������� ����� � ���������� ������
	private ArrayList<Integer> rightpath = null;
	// ������� �������� ���� � ������
	int sizex = 0;
	int sizey = 0;
	// ����� ����� �����
	// ���� ���������� ����
	int freeNodeColor = Color.argb(0, 0, 0, 0);
	// ���� �������� ����
	int busyNodeColor = Color.argb(0, 0, 0, 0);
	// ���� ������ ��������� ����
	int freeFontColor = Color.argb(0, 0, 0, 0);
	// ���� ������ �������� ����
	int busyFontColor = Color.argb(0, 0, 0, 0);
	// �������� ����������
	private Context context = null;
	
	
	// ����������� �� ���������
	public GameGraph(Context context) {
		this.nodes = new ArrayList<GameNode>();
	    this.path = new ArrayList<Integer>();
	    this.rightpath = new ArrayList<Integer>();
	    this.context = context;
	}
	
	// ����������� � �����������, ��������� ��������� ����
	public GameGraph(Context context, int sizex, int sizey, int freeNodeColor, int busyNodeColor, int freeFontColor, int busyFontColor) throws Exception {
	    this.context = context;
		this.nodes = new ArrayList<GameNode>();
	    this.path = new ArrayList<Integer>();
	    this.rightpath = new ArrayList<Integer>();
	    this.busyNodeColor = busyNodeColor;
	    this.freeNodeColor = freeNodeColor;
	    this.freeFontColor = freeFontColor;
	    this.busyFontColor = busyFontColor;
	    if (sizex % 2 == 0 || sizey % 2 == 0)
	    	throw new Exception();
	    else {
		    this.sizex = sizex;
		    this.sizey = sizey;
	    }
	    // ����
	    double stepx = 1.0 / sizex;
	    double stepy = 1.0 / sizey;
	    double line = 0.01;
	    double width = stepx - line;
	    double height = stepy - line;
	    // ��������� ������ ����� �������� �����
	    for (int i = 0; i < sizey; i++)
	        for (int j = 0; j < sizex; j++) {
	            if ((i * sizex + j) % 2 == 0) {
	                int max = 9;
	                int data = (int) (max * Math.random());
	                while (data == 0)
	                    data = (int) (max * Math.random());
	                GameNode node = new GameNode(context, 0, data, stepx / 2 + j * stepx, stepy / 2 + i * stepy);
		            node.setSizeX(width);
		            node.setSizeY(height);
		            addNode(node);
	            }
	            else {
	                int type = (int)(3 * Math.random());
	                while (type == 0)
	                    type = (int)(3 * Math.random());
	                GameNode node = new GameNode(context, type, 0, stepx / 2 + j * stepx, stepx / 2 + i * stepy);
		            node.setSizeX(width);
		            node.setSizeY(height);
		            addNode(node);
	            };

	        }
	    setRandomPath();
	}
	
    // ��������� ���� node
    public void addNode(GameNode node) {
    	this.nodes.add(node);
    }
    
    // �������� ���� � ����
    public int addNodeInPath(int nodeIndex) {
    	// ���� ������ ���� �� ����� - �������
        if (this.path.size() == 0 && this.nodes.get(nodeIndex).getType() > 0)
            return -1;
        // ���� ������ ���� ����� - ���������
        if (this.path.size() == 0) {
            this.path.add(nodeIndex);
            return 0;
        }
        // ���� ���� ��� � ���� - ����� ���� �� ����� ����
        if (inPath(nodeIndex)) {
            ArrayList<Integer> mas = new ArrayList<Integer>();
            for (int i = 0; this.path.get(i) != nodeIndex; i++)
                mas.add(this.path.get(i));
            mas.add(nodeIndex);
            this.path = mas;
            return 0;
        }
        // ���� ���� �� ����� ��������� ����� ��� ����������� ������
        // - ������� ��� ���� ���� ����������� ��� ���������
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

    // ������� ��������� ���������
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
    
    // ���������� ���������
    public int rightResult() {
        int res = 0;
        if (this.rightpath.size() == 0)
            return Integer.MAX_VALUE;
        else
            res = this.nodes.get(0).getData();
        for (int i = 2; i < this.rightpath.size(); i += 2) {
            if (this.nodes.get(this.rightpath.get(i - 1)).getType() == 1)
                res += this.nodes.get(this.rightpath.get(i)).getData();
            if (this.nodes.get(this.rightpath.get(i - 1)).getType() == 2)
                res -= this.nodes.get(this.rightpath.get(i)).getData();
            if (this.nodes.get(this.rightpath.get(i - 1)).getType() == 3)
                res *= this.nodes.get(this.rightpath.get(i)).getData();
        }
        return res;
    }

    // ������� ��������� �������������
    public String toString() {
        if (this.path.size() == 0)
            return "�����";
        if (this.nodes.get(this.path.size() - 1).getType() == 0)
            return Integer.toString(this.nowResult());
        else
            return Integer.toString(this.nowResult()) + " " + this.nodes.get(this.path.get(this.path.size() - 1)).toString();
    }

    // ���������� ���� � ������� �������� ����� x, y
    public GameNode getNodeXY(double x, double y) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).isInside(x, y)) {
                return this.nodes.get(i);
            }
        return null;
    }

    // ���������� ������ ���� � ������� �������� ����� x, y
    public int getNodeIndexXY(double x, double y) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).isInside(x, y)) {
                return i;
            }
        return -1;
    }

    // ������������ GameGraph �� canvas
    public void draw(Canvas canvas) {
        // ������ ���� �����
        for (int i = 0; i < this.nodes.size() - 1; i++) {
            if (inPath(i)) {
                this.nodes.get(i).draw(canvas, this.busyNodeColor, this.busyFontColor);
            }
            else
                this.nodes.get(i).draw(canvas, this.freeNodeColor, this.freeFontColor);
        }
        this.nodes.get(this.nodes.size() - 1).draw(canvas, this.busyNodeColor, this.busyFontColor);
    }

    // ������ �� ���� � ����
    public boolean inPath(int nodeIndex) {
        for (int i = 0; i < this.path.size(); i++) {
            if (this.path.get(i) == nodeIndex)
                return true;
        };
        return false;
    }

    // ������� ����
    public void clearPath() {
        this.path.clear();
    }

    // ������ ��������� ������ ����
    public int getLastPathIndex() {
        if (this.path.size() == 0)
            return -1;
        return this.path.get(this.path.size() - 1);
    }
    
    // ������������� ������� ����� �����������
    public void setGamma(int freeNodeColor, int busyNodeColor, int freeFontColor, int busyFontColor) {
	    this.busyNodeColor = busyNodeColor;
	    this.freeNodeColor = freeNodeColor;
	    this.freeFontColor = freeFontColor;
	    this.busyFontColor = busyFontColor;
    }
    
    // ������������� ������
    public void setSize(int sizex, int sizey) throws Exception {
	    if (sizex % 2 == 0 || sizey % 2 == 0) {
	    	Log.e("altavista", "Illegal size of gameGraph");
	    	throw new Exception();
	    }
	    else {
		    this.sizex = sizex;
		    this.sizey = sizey;
	    }
    }
    
    // ������ ��������� ������������� �����
    // ������ ���� <sizex>_<sizey>_<������ �� ����� <��� ����>_<������ ����>>_<������ ����� � ���������� ����>_<������ ����� � ������� ���������� ����>
    public String getGraph() {
    	String ret = "";
    	ret += Integer.toString(sizex) + "_";
    	ret += Integer.toString(sizey) + "_";
    	for(int i = 0; i < sizey; i++)
    		for(int j = 0; j < sizex; j++) {
    			ret += Integer.toString(nodes.get(sizex * i + j).getType()) + "_";
    			ret += Integer.toString(nodes.get(sizex * i + j).getData()) + "_";
    		};
    	for(int i = 0; i < rightpath.size(); i++)
    		ret += Integer.toString(rightpath.get(i)) + "_";
    	for(int i = 0; i < path.size() - 1; i++)
    		ret += Integer.toString(path.get(i)) + "_";
    	ret += Integer.toString(path.get(path.size() - 1));
    	return ret;
    }
    
    // ��������� ���� �� ���������� �������������
    public void setGraph(String str) throws Exception {
    	try {
	    	String[] strdata = str.split("_");
	    	nodes.clear();
	    	rightpath.clear();
	    	path.clear();
		    sizex = Integer.parseInt(strdata[0]);
		    sizey = Integer.parseInt(strdata[1]);
		    // ����
		    double stepx = 1.0 / sizex;
		    double stepy = 1.0 / sizey;
		    double line = 0.01;
		    double width = stepx - line;
		    double height = stepy - line;
		    int k = 1;
		    // ��������� ������ ����� �������� �����
		    for (int i = 0; i < sizey; i++)
		        for (int j = 0; j < sizex; j++) {
		        	k += 1;
		        	int type = Integer.parseInt(strdata[k]);
		        	k += 1;
		        	int data = Integer.parseInt(strdata[k]);	        	
		            if (type == 0) {
		                GameNode node = new GameNode(context, 0, data, stepx / 2 + j * stepx, stepy / 2 + i * stepy);
			            node.setSizeX(width);
			            node.setSizeY(height);
			            addNode(node);
		            }
		            else {
		                GameNode node = new GameNode(context, type, 0, stepx / 2 + j * stepx, stepx / 2 + i * stepy);
			            node.setSizeX(width);
			            node.setSizeY(height);
			            addNode(node);
		            };
	
		        }
		    // ��������� ������ ����������� ����
		    k += 1;
		    for( int i = 0; i < sizex + sizey - 1; i++ ) {
		    	int p = Integer.parseInt(strdata[k]);
		    	k += 1;
		    	rightpath.add(p);
		    }
		    // ��������� ������ ����
		    for( int i = k; i < strdata.length; i++ ) {
		    	int p = Integer.parseInt(strdata[i]);
		    	path.add(p);
		    }
    	}
    	catch (Exception ex) {
    		Log.e("altavista", ex.getMessage());
    		throw ex;
    	}
    }
    
    // ����� � ��������� ���������� ���� � �����
    public void setRandomPath() {
	    rightpath.clear();
	    int lastIndex = nodes.size() - 1;
	    int index = 0;
	    rightpath.add(index);
	    int i = 0;
	    int j = 0;
	    while (index != lastIndex) {
	        ArrayList<Integer> n = new ArrayList<Integer>();
	        ArrayList<int[]> ind = new ArrayList<int[]>();
	        if ((i + 1) < sizey) {
	            n.add((i + 1) * sizex + j);            
	            ind.add(new int[]{i + 1, j});
	        }
	        if ((j + 1) < sizex) {
	            n.add(i * sizex + j + 1);
	            ind.add(new int[]{i, j + 1});
	        };
	        if (n.size() == 1) {
	            index = n.get(0);
	            i = ind.get(0)[0];
	            j = ind.get(0)[1];
	        }
	        if (n.size() == 2) {
	            int who = Math.random() > 0.5 ? 1 : 0;
	            index = n.get(who);
	            i = ind.get(who)[0];
	            j = ind.get(who)[1];
	        };
	        rightpath.add(index);
	    }
	    path.add(0);
    }
}
