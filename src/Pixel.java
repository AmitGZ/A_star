import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

enum Types {Ground, Wall, Start, End}

enum Status{Blank, Open, Closed,Path}

public class Pixel extends JLabel{
	 private int x_index , y_index;
	 private Types type; // Ground, Wall, Start or End 
	 private Status search_status; //initialized as false
	 private int f_cost, g_cost;
	 private Pixel Father; 
	 private boolean isVisited; 
	
	Pixel(int x, int y){
		x_index=x;
		y_index=y;
		type = Types.Ground; // initializing all as ground
		search_status = Status.Blank; // initializing all as blank 
		g_cost = f_cost = Integer.MAX_VALUE; // for A_star algorithm
		setHorizontalAlignment(SwingConstants.CENTER); // initializing at the center
		setBackground(Color.white);
		setOpaque(true);
	}
		
	public void setType(Types type) {
		if(type==Types.Start) { 
			setBackground(Color.green);
			setText("o");
		}
		else if(type ==Types.End) {
			setBackground(Color.blue);
			setText("x");
		}
		else if(type ==Types.Wall) 
			setBackground(Color.black);
		else { //Ground
			setBackground(Color.white);
			setText(null);
		}
		this.type=type;
		update(this.getGraphics()); //FOR ANIMATION
	}
	
	public void setStatus(Status stat){
		if(stat==Status.Blank) {
			if(type == Types.Ground)
				setBackground(Color.white);
		}
		else if(stat==Status.Open) {
			if(type == Types.Ground)
				setBackground(Color.green);
			}
		else if(stat==Status.Closed) {
			if(type == Types.Ground)// (&& Closed)
				setBackground(Color.red);
		}
		else //status == Path
			setBackground(Color.yellow);
		
		search_status=stat;
		update(this.getGraphics());  //FOR ANIMATION
	}
	
	 //This method is used to reset the board data but keep the types  
	public void resetPixelData() {
		search_status = Status.Blank;
		g_cost = f_cost = Integer.MAX_VALUE;
		isVisited =false;
		setFather(null);
	}
	
	
	// Getters and Setters
	public int getXIndex() {return x_index;}

	public int getYIndex() {return y_index;}
	
	public Types getType() {return type;}
	
	public void setFCost(int f) {f_cost=f;}
	
	public void setGCost(int g) {g_cost=g;}
	
	public int getFCost() {return f_cost;}
	
	public int getGCost() {return g_cost;}

	public Status getSearchStatus() {return search_status;}

	public void setFather(Pixel p) {Father = p;}

	public Pixel getFather() {return Father;}
	
	public void setVisited(boolean isVisited) {this.isVisited=isVisited;}
	
	public boolean getVisited() {return isVisited;}
	
}
