import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.lang.Math; 

public class DrawPanel extends JPanel{
	
	private ArrayList<ArrayList<Pixel>> Pixel_arr;// Two Dimensional pixel array
	private Pixel start,end; //Keeps track of start and end pixels initialized as null
	private List open; // Linked list of open nodes sorted by f_cost
	private final int panel_size, resolution, space_size; // Final resolution given by constructor
	private String current_op; // Current operation
	private boolean real_time; //variable connected to the real time-check box
	
	DrawPanel(int panel_size, int resolution , int space_size ,String current_op){
		setBackground(Color.black);
		setLayout(null);
		
		this.current_op = current_op;
		this.panel_size= panel_size;
		this.resolution =resolution;
		this.space_size =space_size;
		open = new List();
		Pixel_arr = new ArrayList<>(resolution);
		
		//calculating pixels size based on resolution, panel size and space size
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution;
		
		//Initializing Pixel_arr
		for(int i=0; i < resolution; i++) 
			Pixel_arr.add(new ArrayList<Pixel>());
		
		//Initializing Pixel_arr
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) 
			{
				Pixel_arr.get(i).add(new Pixel(i,j));
				Pixel_arr.get(i).get(j).setBounds(i*(space_size+Pixel_size) , j*(space_size+Pixel_size) ,Pixel_size,Pixel_size);
				this.add(Pixel_arr.get(i).get(j));
			}
		
		//creating and associating mouse hander
		MouseHandler mouse_handler = new MouseHandler();
		this.addMouseListener( mouse_handler );
		this.addMouseMotionListener( mouse_handler );

		setVisible(true);
	}
	
	public boolean solve() {
		start.setGCost(0);
		start.setFCost(hCost(start));
		List.addOrganize(start);
		while(!List.isEmpty())
			if(calculateNear(List.pop())) {
				traverseBack();
				return true;
			}
		return false;
	}	
	
	public void createMaze() {		
		resetTotal();
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) 
				if(( i%2 ==0  || j%2 ==0))
					Pixel_arr.get(i).get(j).setWall();
		
		
		Stack<Pixel> stack = new Stack<Pixel>();
		Pixel current, neighbor;
		
		current= Pixel_arr.get(1).get(1); //starting position top left
		stack.push(current);
		
		while(!stack.isEmpty()) {
			current = stack.pop();
			current.setVisited(true);
			neighbor= getNeighbor(current);
			if(neighbor!=null) {
				stack.push(current);
				Pixel_arr.get((current.getXIndex() + neighbor.getXIndex()) /2).get((current.getYIndex() + neighbor.getYIndex()) /2).setGround();
				neighbor.setVisited(true);
				stack.push(neighbor);
			}
		}
		
		
	}
	
	private Pixel getNeighbor(Pixel p) {
		Pixel neighbor;
		int[] arr = {0,1,2,3};
		shuffleArray(arr);
		int dx,dy;
		for(int i = 0; i <4 ; i++)
		{
			if(arr[i]==0){
				dx=0;dy=2;
			}
			else if(arr[i]==1){
				dx=2;dy=0;
			}
			else if(arr[i]==2){
				dx=0;dy=-2;
			}
			else {
				dx=-2;dy=0;
			}
				if(p.getXIndex() + dx < resolution && p.getYIndex() + dy < resolution && p.getXIndex() + dx >= 0 && p.getYIndex() + dy >= 0) {
					neighbor = Pixel_arr.get(p.getXIndex() +dx).get(p.getYIndex() + dy);
					if(!neighbor.getVisited())
						return neighbor;
				}
			}
		return null;
	}
	
	private void shuffleArray(int[] arr) {
		int tmp, j;
	    for (int i = arr.length - 1; i > 0; i--) {
	        j = (int) Math.floor(Math.random() * (i + 1));
	        tmp = arr[i];
	        arr[i] = arr[j];
	        arr[j] = tmp;
	    }
	}

	public boolean calculateNear(Pixel p){
		Pixel neighbor;
		for(int dx = -1; dx <=1 ; dx++)
			for(int dy = -1 ; dy<=1 ;dy++) {
				if(dx == 0 && dy == 0 )
					continue;
				if(p.getXIndex() + dx < resolution && p.getYIndex() + dy < resolution && p.getXIndex() + dx >= 0 && p.getYIndex() + dy >= 0) {
					neighbor = Pixel_arr.get(p.getXIndex() +dx).get(p.getYIndex() + dy);
					if(neighbor.getType() == Types.Ground && neighbor.getSearchStatus() != Status.Closed) {
						if(p.getGCost()+distance(p, neighbor) < neighbor.getGCost()) { //Checks if we need to update costs
							neighbor.setFather(p);   //setting the father
							neighbor.setGCost(p.getGCost()+distance(p, neighbor)); //updating g_cost
							neighbor.setFCost(neighbor.getGCost() + hCost(neighbor)); // updating f_cost
							neighbor.setOpen();  // setting open
							if(neighbor.getSearchStatus()== Status.Open)
								List.remove(neighbor);
							List.addOrganize(neighbor);
						}
					}
					else if(neighbor.getType() == Types.End) {
						neighbor.setFather(p); //setting the father
						return true; //Found the end
					}
				}
			}
		p.setClosed(); //add to closed
		return false; //Didn't find the end
	}
	
	private void traverseBack() {
		Pixel tmp=end;
		while(tmp!=null) {
			tmp.setPath();
			tmp=tmp.getFather();
		}
		
	}

	public void resetTotal() {
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) {
				Pixel_arr.get(i).get(j).resetPixelData();
				Pixel_arr.get(i).get(j).setGround();
			}
		open.clearList();
	}
	
	public void resetData() {
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) {
				if(Pixel_arr.get(i).get(j).getSearchStatus() != Status.Blank)
					Pixel_arr.get(i).get(j).setBlank();
				Pixel_arr.get(i).get(j).resetPixelData();
			}
		open.clearList();
	}
	
	private class MouseHandler implements MouseMotionListener, MouseListener
	{
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution;
		
		@Override
		public void mousePressed(MouseEvent e) {
			int pixel_x = e.getX()/(Pixel_size+space_size), pixel_y= e.getY()/(Pixel_size+space_size);
			
			//checking mouse is within panel
			if(e.getX()<panel_size && e.getY()<panel_size && e.getX()>0 && e.getY() >0)
				//if Wall is selected
				if(current_op == "Wall")
					Pixel_arr.get(pixel_x).get(pixel_y).setWall();
				//if Start is selected
				else if(current_op == "Start") {
					if(start!= null && start.getType()==Types.Start)
						start.setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).setStart();
					start = Pixel_arr.get(pixel_x).get(pixel_y);
				}
				//if End is selected
				else if(current_op == "End") {
					if(end!= null && end.getType()==Types.End)
						end.setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).setEnd();
					end = Pixel_arr.get(pixel_x).get(pixel_y);
				}
				//if Erase is selected
				else if(current_op == "Erase") {
					Pixel_arr.get(pixel_x).get(pixel_y).setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).resetPixelData();
				}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			int pixel_x = e.getX()/(Pixel_size+space_size), pixel_y= e.getY()/(Pixel_size+space_size);
			
			//checking mouse is within panel
			if(e.getX()<panel_size && e.getY()<panel_size && e.getX()>0 && e.getY() >0)
			{
				//if Wall is selected
				if(current_op == "Wall")
					Pixel_arr.get(pixel_x).get(pixel_y).setWall();
				//if Erase is selected
				else if(current_op == "Erase") {
					Pixel_arr.get(pixel_x).get(pixel_y).setGround();
					Pixel_arr.get(pixel_x).get(pixel_y).resetPixelData();
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(isBoardValid() && real_time) {
				resetData();
				solve();
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}
		
	}
	
	public boolean isBoardValid() {
		return (start != null && start.getType() == Types.Start && end != null && end.getType() == Types.End);
	}
	
	//@param p1,p2  This method calculates distance between two pixels
	private int distance(Pixel p1, Pixel p2) {
		return (int)(Math.pow (Math.pow(p1.getX()- p2.getX(),2) + Math.pow(p1.getY()- p2.getY(),2) ,0.5));
	}
	
	//@param p This method calculates distance to end
	private int hCost(Pixel p){
		return distance(p,end);
	}

	public void setRealTime(boolean real_time) {this.real_time = real_time;}	

	public void setCurrentOp(String Op) {current_op = Op;}
}
