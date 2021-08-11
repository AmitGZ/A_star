import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;

public class Pixel extends JLabel{
	 private int x_index , y_index;
	 private String type;
	 private double f_cost, g_cost;
	 boolean is_closed; //initialized as false
	 Pixel parent;
	
	Pixel(int x, int y){
		x_index=x;
		y_index=y;
		g_cost = f_cost = Double.POSITIVE_INFINITY;
		setHorizontalAlignment(SwingConstants.CENTER);
		setBackground(Color.white);
		setOpaque(true);
		type = "Ground";
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
	
	public int getXIndex() {
		return x_index;
	}

	public int getYIndex() {
		return y_index;
	}
	
	public String getType() {
		return type;
	}
	
	public void setFcost(double f) {
		f_cost=f;
	}
	
	public void setGcost(double g) {
		g_cost=g;
	}
	
	public double getFcost() {
		return f_cost;
	}
	
	public double getGcost() {
		return g_cost;
	}

	public void setClosed() {
		this.is_closed = true;
	}
	
	public boolean getClosed() {
		return is_closed;
	}

	public void setParent(Pixel p) {
		parent = p;
	}
	/*
	public Pixel getParent() {
		return this.parent;
	}
	*/
}
