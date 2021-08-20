import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;

enum Types {Ground, Wall, Start, End}

enum Status{Blank, Open, Closed}

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
		
	public void setGround() {
		setBackground(Color.white);
		type = Types.Ground;
	}
	
	public void setWall() {
		setBackground(Color.black);
		type = Types.Wall;
	}
	
	public void setStart() {
		setBackground(Color.green);
		type = Types.Start;
	}
	
	public void setEnd() {
		setBackground(Color.blue);
		type = Types.End;
	}
	
	public void setPath() {
		setBackground(Color.yellow);
	}
	
	//This method is used to reset the board data but keep the types
	public void resetPixelData() {
		search_status = Status.Blank;
		g_cost = f_cost = Integer.MAX_VALUE;
		isVisited =false;
		setFather(null);
		setText(null);
	}
	
	public int getXIndex() {return x_index;}

	public int getYIndex() {return y_index;}
	
	public Types getType() {return type;}
	
	public void setFCost(int f) {f_cost=f;}
	
	public void setGCost(int g) {g_cost=g;}
	
	public int getFCost() {return f_cost;}
	
	public int getGCost() {return g_cost;}

	public void setBlank() {
		search_status = Status.Blank;
		//FOR ILLUSTRATION
		if(type == Types.Ground)
			setBackground(Color.white);
	}
	
	public void setOpen() {
		search_status = Status.Open;
		//FOR ILLUSTRATION
		if(type == Types.Ground)
			setBackground(Color.green);
		setText(Integer.toString(f_cost));
	}
	
	public void setClosed() {
		search_status = Status.Closed;
		//FOR ILLUSTRATION
		if(type == Types.Ground)
			setBackground(Color.red);
		setText(Integer.toString(f_cost));
	}
	
	public Status getSearchStatus() {return search_status;}

	public void setFather(Pixel p) {Father = p;}

	public Pixel getFather() {return Father;}
	
	public void setVisited(boolean isVisited) {this.isVisited=isVisited;}
	
	public boolean getVisited() {return isVisited;}
}
