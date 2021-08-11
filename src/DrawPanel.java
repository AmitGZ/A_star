import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.lang.Math; 

public class DrawPanel extends JPanel{
	
	private ArrayList<ArrayList<Pixel>> Pixel_arr;
	private Pixel start,end;
	private List open;
	private final int panel_size, resolution, space_size;
	private String current_op;
	
	DrawPanel(int panel_size, int resolution , int space_size ,String current_op){
		setBackground(Color.black);
		this.current_op = current_op;
		this.panel_size= panel_size;
		this.resolution =resolution;
		this.space_size =space_size;
		open = new List();
		
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
	}
	
	public void setCurrentOp(String Op) {current_op = Op;}
	
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

	public boolean solve() {
		if(start == null ||start.getType() != "Start" || end ==null || end.getType() != "End") {
			JOptionPane.showMessageDialog(null,"Couldn't find start and end");  
			return false;
		}
		start.setGcost(0);
		start.setFcost(hCost(start));
		List.addOrganize(start);
		while(!List.isEmpty())
			if(calculateNear(List.pop())) {
				traverseBack();
				return true;
			}
		return false;
	}		

	private int hCost(Pixel p){
		return (int)(Math.pow (Math.pow(p.getX()- end.getX(),2) + Math.pow(p.getY()- end.getY(),2) ,0.5));
	}
	
	private int distance(Pixel p1, Pixel p2) {
		return (int)(Math.pow (Math.pow(p1.getX()- p2.getX(),2) + Math.pow(p1.getY()- p2.getY(),2) ,0.5));
	}
	
	private void traverseBack() {
		Pixel tmp=end;
		while(tmp!=null) {
			tmp.setPath();
			tmp=tmp.getFather();
		}
		
	}
	
	public boolean calculateNear(Pixel p){
		Pixel neighbor;
		for(int dx = -1; dx <2 ; dx++)
			for(int dy = -1 ; dy<2 ;dy++) {
				if(dx == 0 && dy == 0 )
					continue;
				if(p.getXIndex() + dx < resolution && p.getYIndex() + dy < resolution && p.getXIndex() + dx >= 0 && p.getYIndex() + dy >= 0) {
					neighbor = Pixel_arr.get(p.getXIndex() +dx).get(p.getYIndex() + dy);
					if(neighbor.getType() == "Ground" && neighbor.getSearchStatus() != "Closed") {
						if(p.getGcost()+distance(p, neighbor) < neighbor.getGcost()) { //Checks if we need to update costs
							neighbor.setFather(p);                                     //setting the father
							neighbor.setGcost(p.getGcost()+distance(p, neighbor));     //updating g_cost
							neighbor.setFcost(neighbor.getGcost() + hCost(neighbor));  // updating f_cost
							neighbor.setOpen();                                        // setting open
							if(neighbor.getSearchStatus()== "Open")
								List.remove(neighbor);
							List.addOrganize(neighbor);
						}
					}
					else if(neighbor.getType() == "End") {
						neighbor.setFather(p);                                     //setting the father
						p.setClosed();
						return true;
					}
				}
			}
		p.setClosed(); //add to closed
		return false;
	}

}
