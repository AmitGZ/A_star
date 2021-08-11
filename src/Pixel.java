import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;

public class Pixel extends JLabel{
	 private int x_index , y_index;
	 private String type;
	 private String search_status; //initialized as false
	 private int f_cost, g_cost;
	 private Pixel Father;
	
	Pixel(int x, int y){
		x_index=x;
		y_index=y;
		type = "Ground";
		search_status = "Blank";
		g_cost = f_cost = Integer.MAX_VALUE;
		setHorizontalAlignment(SwingConstants.CENTER);
		setBackground(Color.white);
		setOpaque(true);
	}
		
	public void setGround() {
		setBackground(Color.white);
		type = "Ground";
	}
	
	public void setWall() {
		setBackground(Color.black);
		type = "Wall";
	}
	
	public void setStart() {
		setBackground(Color.green);
		type = "Start";
	}
	
	public void setEnd() {
		setBackground(Color.blue);
		type = "End";
	}
	
	public void setPath() {
		setBackground(Color.yellow);
	}
	
	public int getXIndex() {return x_index;}

	public int getYIndex() {return y_index;}
	
	public String getType() {return type;}
	
	public void setFcost(int f) {f_cost=f;}
	
	public void setGcost(int g) {g_cost=g;}
	
	public int getFcost() {return f_cost;}
	
	public int getGcost() {return g_cost;}

	public void setOpen() {
		search_status = "Open";
		//FOR ANIMATION
		setBackground(Color.green);
		setText(String.format("%d  ", g_cost) + String.format("%d",f_cost));
	}
	
	public void setClosed() {
		search_status = "Closed";
		//FOR ANIMATION
		setBackground(Color.red);
		setText(String.format("%d  ",g_cost) + String.format("%d",f_cost));
	}
	
	public String getSearchStatus() {return search_status;}

	public void setFather(Pixel p) {Father = p;}

	public Pixel getFather() {return Father;}
	
}
