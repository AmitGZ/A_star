import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

enum Types {Ground, Wall, Start, End}

enum Status{Blank, Open, Closed,Path}

public class Pixel extends JLabel{
	 private final int col , row;
	 private Types type; // Ground, Wall, Start or End 
	 private Status search_status; //initialized as false
	 private int f_cost, g_cost;
	 private Pixel Father; 
	 private boolean isVisited; 
	
	Pixel(int col, int row){
		this.col=col;
		this.row=row;
		type = Types.Ground; // initializing all as ground
		search_status = Status.Blank; // initializing all as blank 
		g_cost = f_cost = Integer.MAX_VALUE; // for A_star algorithm
		setHorizontalAlignment(SwingConstants.CENTER); // initializing at the center
		setBackground(Color.white);
		setOpaque(true);
	}
		
	public void setType(Types type) {
		if(type==Types.Ground) { 
			setBackground(Color.white);
			setText(null);
		}
		else if(type ==Types.Wall) {
			setBackground(Color.black);
		}
		else if(type ==Types.End) {
			setBackground(Color.blue);
			setText("x");
		}
		else { //Start
			setBackground(Color.green);
			setText("o");
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
		update(this.getGraphics()); //FOR ANIMATION
	}
	
	 //This method is used to reset the board data but keep the types  
	public void resetPixelData() {
		search_status = Status.Blank;
		g_cost = f_cost = Integer.MAX_VALUE;
		isVisited =false;
		setFather(null);
	}
	
	// Getters and Setters
	public int getCol() {return col;}

	public int getRow() {return row;}
	
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
