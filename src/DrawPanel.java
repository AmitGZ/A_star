import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import java.lang.Math; 

public class DrawPanel extends JPanel{
	
	private ArrayList<ArrayList<Pixel>> Pixel_arr;// Two Dimensional pixel array
	private Pixel start,end; //Keeps track of start and end pixels initialized as null
	private List open; // Linked list of open nodes sorted by f_cost
	private final int panel_size, resolution, space_size; // Final resolution given by constructor
	private String current_op; // Current operation
	Thread second_thread;
	
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
				Pixel_arr.get(i).get(j).setFont(new Font("Calibri", Font.BOLD, Pixel_size));
				this.add(Pixel_arr.get(i).get(j));
			}
		
		//creating and associating mouse hander
		MouseHandler mouse_handler = new MouseHandler();
		this.addMouseListener( mouse_handler );
		this.addMouseMotionListener( mouse_handler );

		setVisible(true);
	}
	
	private class MouseHandler extends MouseAdapter
	{
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution;
		
		private void mouseDraggedAndPressed(MouseEvent e) {
			int pixel_x = e.getX()/(Pixel_size+space_size), pixel_y= e.getY()/(Pixel_size+space_size);
			Pixel current;
			boolean changed=false;
			
			//checking mouse is within panel
			if(e.getX() < panel_size && e.getY() < panel_size && e.getX()>0 && e.getY() >0)
			{
				current = Pixel_arr.get(pixel_x).get(pixel_y);
				//if Wall is selected
				if(current_op == "Wall") {
					if(current.getType()!=Types.Wall)
						changed=true;
					current.setType(Types.Wall);
				}
				//if Erase is selected
				else if(current_op == "Erase") {
					if(current.getType()!=Types.Ground)
						changed=true;
					current.setType(Types.Ground);
					current.resetPixelData();
				}
				else if(Pixel_arr.get(pixel_x).get(pixel_y).getType() == Types.Ground)//only allow Start and End on Ground type
				{
					if(current_op == "Start") {
						if(current.getType()!=Types.Start)
							changed=true;
						if(start!= null && start.getType()==Types.Start)
							start.setType(Types.Ground);
						current.setType(Types.Start);
						start = current;
					}
					//if End is selected
					else if(current_op == "End") {
						if(current.getType()!=Types.End)
							changed=true;
						if(end!= null && end.getType()==Types.End)
							end.setType(Types.Ground);
						current.setType(Types.End);
						end = current;
					}
				}
				if(isBoardValid() && changed) {
					interruptAndWait();
			        second_thread = new Thread(new Runnable() {
						public void run(){ 
								solve();
							}});
			        second_thread.start();
				}
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			mouseDraggedAndPressed(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseDraggedAndPressed(e);
		}
	}
	
	public void interruptAndWait() {
		if(second_thread!=null) {
			second_thread.interrupt();
			try {
				second_thread.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//this method uses the A star algorithm to find the shortest path between start and end
	public boolean solve() {
		resetData(); // resetting any previous data
		//setting start cost
		start.setGCost(0);
		start.setFCost(hCost(start));
		//adding to the organized list the starting point
		List.addOrganize(start);
		//while the list isn't empty keep exploring nodes,
		while(!Thread.currentThread().isInterrupted() && !List.isEmpty())
			if(!Thread.currentThread().isInterrupted() && calculateNear(List.pop())) {// if caluclateNear returns true -> reached the end
				traverseBack(); //reached the end traverse back the path we came
				return true; //return true to indicate we found a path
			}
		return false;//return true to indicate we didn't find a path
	}	
	
	//This function traverses back the path once we found the end point
	private void traverseBack() {

		Pixel tmp=end;
		while(tmp!=null) {
			tmp.setStatus(Status.Path);
			tmp=tmp.getFather();
			delay();
		}
		
	}

	//This function is used for the A star algorithm to calculate all the near pixels and choose which path to take
	public boolean calculateNear(Pixel p){
		if(p==null)
			return false;
		Pixel neighbor;
		for(int dx = -1; dx <=1 ; dx++)
			for(int dy = -1 ; dy<=1 ;dy++) {
				//if(dx == 0 && dy == 0) //use if to allow diagonal movement
				if(!(dx == 0 ^ dy == 0)) // use to forbid diagonal
					continue;
				if(p.getXIndex() + dx < resolution && p.getYIndex() + dy < resolution && p.getXIndex() + dx >= 0 && p.getYIndex() + dy >= 0) {
					neighbor = Pixel_arr.get(p.getXIndex() +dx).get(p.getYIndex() + dy);
					if(neighbor.getType() == Types.Ground && neighbor.getSearchStatus() != Status.Closed) {
						if(p.getGCost()+distance(p, neighbor) < neighbor.getGCost()) { //Checks if we need to update costs
							neighbor.setFather(p);   //setting the father
							neighbor.setGCost(p.getGCost()+distance(p, neighbor)); //updating g_cost
							neighbor.setFCost(neighbor.getGCost() + hCost(neighbor)); // updating f_cost
							neighbor.setStatus(Status.Open);  // setting open
							if(neighbor.getSearchStatus()== Status.Open)
								List.remove(neighbor);
							List.addOrganize(neighbor);
							delay();
						}
					}
					else if(neighbor.getType() == Types.End) {
						neighbor.setFather(p); //setting the father
						return true; //Found the end
					}
				}
			}
		p.setStatus(Status.Closed); //add to closed
		return false; //Didn't find the end
	}
	
	//This method is used by "createMaze" to get a random and viable neighbor
	private Pixel getNeighbor(Pixel p) {
		Pixel neighbor;
		int[] arr = {0,1,2,3};
		shuffleArray(arr); //shuffling the array
		int dx,dy;
		for(int i = 0; i <4 ; i++)
		{
			if(arr[i]==0){//right
				dx=0;
				dy=2;
			}
			else if(arr[i]==1){//bottom
				dx=2;
				dy=0;
			}
			else if(arr[i]==2){//left
				dx=-2;
				dy=0;
			}
			else {//top
				dx=0;
				dy=-2;
			}
				if(p.getXIndex() + dx < resolution && p.getYIndex() + dy < resolution && p.getXIndex() + dx >= 0 && p.getYIndex() + dy >= 0) {
					neighbor = Pixel_arr.get(p.getXIndex() +dx).get(p.getYIndex() + dy);
					if(!neighbor.getVisited())
						return neighbor;
				}
			}
		return null;
	}
	
	//This function is used by getNeighbor to get a random neighbor
	private void shuffleArray(int[] arr) {
		int tmp, j;
	    for (int i = arr.length - 1; i > 0; i--) {
	        j = (int) Math.floor(Math.random() * (i + 1));
	        tmp = arr[i];
	        arr[i] = arr[j];
	        arr[j] = tmp;
	    }
	}

	public void createMaze() {		
		//Resetting the board
		resetTotal(); 
		//Creating all the walls, later to be removed by the maze creator
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) 
				if(( i%2 ==0  || j%2 ==0)) {
					Pixel_arr.get(i).get(j).setType(Types.Wall);
				}


		//Creating the stack and helper variables
		Stack<Pixel> stack = new Stack<Pixel>();
		Pixel current, neighbor;
		
		//Setting the starting position at top left
		current= Pixel_arr.get(1).get(1);
		stack.push(current);
		
		//Maze generating algorithm
		while(!stack.isEmpty()) {//if stack is empty -finish
			current = stack.pop(); //setting the current node to the node from the stack
			current.setVisited(true);// setting as visited
			neighbor= getNeighbor(current); //getting a neighbor
			if(neighbor!=null) { //if no neighbors left get from stack,
				stack.push(current); //pushing the current to stack
				//removing wall between current and neighbor
				Pixel_arr.get((current.getXIndex() + neighbor.getXIndex()) /2).get((current.getYIndex() + neighbor.getYIndex()) /2).setType(Types.Ground);
				delay(); //adding delay for maze creation for animation
				neighbor.setVisited(true);// setting visited
				stack.push(neighbor); //pushing neighbor to stack
			}
		}
	}
	
	//This function clears all the pixels' data
	public void resetTotal() {   
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) {
				Pixel_arr.get(i).get(j).resetPixelData();
				Pixel_arr.get(i).get(j).setType(Types.Ground);
			}
		open.clearList();
	}
	
	//This function clears all the pixels' data except for their type
	public void resetData() {
		for(int i=0; i < resolution; i++) 
			for(int j=0; j < resolution; j++) {
				if(Pixel_arr.get(i).get(j).getSearchStatus() != Status.Blank)
					Pixel_arr.get(i).get(j).setStatus(Status.Blank);
				Pixel_arr.get(i).get(j).resetPixelData();
			}
		open.clearList(); //clearing the "open" A-star list
	}
	
	//FOR ANIMATION
	private void delay() {
		try {
			TimeUnit.MICROSECONDS.sleep(80000/resolution);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	//checks if there is a start and an end and returns true or false
	public boolean isBoardValid() {
		return (start != null && start.getType() == Types.Start && end != null && end.getType() == Types.End);
	}
	
	//@param p1,p2  This method calculates distance between two pixels with Pythagoras
	private int distance(Pixel p1, Pixel p2) {
		return (int)(Math.pow (Math.pow(p1.getX()- p2.getX(),2) + Math.pow(p1.getY()- p2.getY(),2) ,0.5));
	}
	
	//@param p This method calculates distance to end
	private int hCost(Pixel p){
		return distance(p,end);
	}

	//setting the current operation - wall,start,end,erase
	public void setCurrentOp(String Op) {current_op = Op;}
}
