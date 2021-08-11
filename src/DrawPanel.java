import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.lang.Math; 

public class DrawPanel extends JPanel{
	
	ArrayList<ArrayList<Pixel>> Pixel_arr;
	Pixel start,end;
	LinkedList<Pixel> open;
	LinkedList<Pixel> closed;
	final int panel_size, resolution, space_size;
	String current_op;
	
	DrawPanel(int panel_size, int resolution , int space_size ,String current_op){
		setBackground(Color.black);
		this.current_op = current_op;
		this.panel_size= panel_size;
		this.resolution =resolution;
		this.space_size =space_size;
		open = new LinkedList<Pixel>();
		closed = new LinkedList<Pixel>();
		
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution;
		setLayout(null);
		
		Pixel_arr = new ArrayList<>(resolution);
		
		for(int i=0; i < resolution; i++) 
			Pixel_arr.add(new ArrayList<Pixel>());
		
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) 
			{
				Pixel_arr.get(i).add(new Pixel(i,j)); //CHANGE
				Pixel_arr.get(i).get(j).setBounds(i*(space_size+Pixel_size) , j*(space_size+Pixel_size) ,Pixel_size,Pixel_size);
				this.add(Pixel_arr.get(i).get(j));
			}
		setVisible(true);
		
		MouseHandler mouse_handler = new MouseHandler();
		this.addMouseListener( mouse_handler );
		this.addMouseMotionListener( mouse_handler );

	}
	
	public void reset() {
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) 
				Pixel_arr.get(i).get(j).setGround();
		System.out.println(Pixel_arr.get(0).get(0).getClosed());
	}
	
	public void setCurrentOp(String Op) {
		current_op = Op;
	}
	
	private class MouseHandler implements MouseMotionListener, MouseListener
	{
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution;
		
		@Override
		public void mousePressed(MouseEvent e) {
			int pixel_x = e.getX()/(Pixel_size+space_size), pixel_y= e.getY()/(Pixel_size+space_size);
			
			if(e.getX()<panel_size && e.getY()<panel_size && e.getX()>0 && e.getY() >0)
				if(current_op == "Wall")
					Pixel_arr.get(pixel_x).get(pixel_y).setWall();
				else if(current_op == "Start") {
					if(start!= null && start.getType()=="Start")
						start.setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).setStart();
					start = Pixel_arr.get(pixel_x).get(pixel_y);
				}
				else if(current_op == "End") {
					if(end!= null && end.getType()=="End")
						end.setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).setEnd();
					end = Pixel_arr.get(pixel_x).get(pixel_y);
				}
				else if(current_op == "Erase")
					Pixel_arr.get(pixel_x).get(pixel_y).setGround();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			int pixel_x = e.getX()/(Pixel_size+space_size), pixel_y= e.getY()/(Pixel_size+space_size);
			
			if(e.getX()<panel_size && e.getY()<panel_size && e.getX()>0 && e.getY() >0)
			{
				if(current_op == "Wall")
					Pixel_arr.get(pixel_x).get(pixel_y).setWall();
				else if(current_op == "Erase")
					Pixel_arr.get(pixel_x).get(pixel_y).setGround();
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}
		
	}

	public void solve() {
		if(start == null ||start.getType() != "Start" || end ==null || end.getType() != "End") {
			JOptionPane.showMessageDialog(null,"Couldn't find start and end");  
			return;
		}
	}

	private double hCost(Pixel p){
		return Math.pow (Math.pow(p.getX()- end.getX(),2) + Math.pow(p.getY()- end.getY(),2) ,0.5);
	}
	
	private double distance(Pixel p1, Pixel p2) {
		return Math.pow (Math.pow(p1.getX()- p2.getX(),2) + Math.pow(p1.getY()- p2.getY(),2) ,0.5);
	}
	/*
	public void calculateNear(Pixel p){
		p.setClosed(); //add to closed
		if(p.getParent() == null) // in case we're in the starting pixel 
			p.setGcost(0);
		
		for(int dx = -1; dx <2 ; dx++)
			for(int dy = -1 ; dy<2 ;dy++) {
				if(dx == 0 && dy == 0 )
					continue;
				if(p.getX() +dx < panel_size && p.getY() + dy < panel_size && p.getX() + dx > 0 && p.getY() + dy > 0)
					if(p.getType() == "Ground")
					{
						Pixel neighbor = Pixel_arr.get(p.getX() +dx).get(p.getY() +dy);
						if(p.getGcost()+distance(p, neighbor) < neighbor.getGcost()) {
							neighbor.setParent(p);
							neighbor.setGcost(p.getGcost()+distance(p, neighbor)); //updating g_cost
							neighbor.setFcost(neighbor.getGcost() + hCost(neighbor)); // updating f_cost
						}
						//add to open
					}
					else if(p.getType() == "End")
						return;
			}
	}
*/
}
