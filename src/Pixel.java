import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;

public class Pixel extends JLabel{
	 private int x_index , y_index;
	 private String type;
	 private double f_cost, g_cost;
	 private boolean is_closed; //initialized as false
	 static Pixel Father;
	
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
	
	public int getXIndex() {return x_index;}

	public int getYIndex() {return y_index;}
	
	public String getType() {return type;}
	
	public void setFcost(double f) {f_cost=f;}
	
	public void setGcost(double g) {g_cost=g;}
	
	public double getFcost() {return f_cost;}
	
	public double getGcost() {return g_cost;}

	public void setOpen() {
		//FOR ANIMATION
		setBackground(Color.green);
		setText(String.format("%.1f", g_cost) +" " + String.format("%.1f",f_cost));
	}
	
	public void setClosed() {
		this.is_closed = true;
		//FOR ANIMATION
		setBackground(Color.red);
		setText(String.format("%.1f",g_cost) +" " + String.format("%.1f",f_cost));
	}
	
	public boolean getClosed() {return is_closed;}

	public void setFather(Pixel p) {Father = p;}

	public Pixel getFather() {return Father;}
	
}
