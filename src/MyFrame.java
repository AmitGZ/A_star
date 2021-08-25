import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class MyFrame extends JFrame{
	private JPanel button_panel;
	private DrawPanel draw_panel;
	private final JComboBox<String> operation_box; // List of all available shapes 
	private JButton reset,create_maze;
	private final static String[] operation_list = {"Start", "End" ,"Wall","Erase"};
	final int screen_size=810, resolution =55, space_size =1;
	int panel_size=710;
	
	MyFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(screen_size,screen_size+30);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setResizable(false);
		setLocationRelativeTo(null);
		
		ButtonHandler button_handler = new ButtonHandler();
		operationHandler op_handler = new operationHandler();
		
		operation_box= new JComboBox<String>(operation_list);
		operation_box.addItemListener(op_handler);
		
		reset = new JButton("Reset");
		create_maze = new JButton("Create Maze");
		reset.addActionListener(button_handler);
		create_maze.addActionListener(button_handler);
		
		button_panel = new JPanel();
		button_panel.add(operation_box);
		button_panel.add(reset);
		button_panel.add(create_maze);

		//Fixing the panel size, accounting for rounding errors
		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution; 
		panel_size = (Pixel_size+space_size)*resolution-space_size;
		
		//Defining and adding panels
		draw_panel = new DrawPanel(panel_size, resolution, space_size, operation_box.getItemAt(0));
		draw_panel.setPreferredSize(new Dimension(panel_size,panel_size));
		add(button_panel);
		add(draw_panel);

		setVisible(true);
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			draw_panel.interruptAndWait();
			if(event.getSource() == reset) {
				draw_panel.resetTotal(); //total reset
			}
			else if(event.getSource() == create_maze)
				draw_panel.createMaze(); //create maze
		}
	}
	
	private class operationHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			if (event.getSource() == operation_box && event.getStateChange() == event.SELECTED) // setting operation -  Wall Start End Erase
				draw_panel.setCurrentOp(operation_box.getSelectedItem().toString());
		}
	}
	
	public static void main(String[] args){
		MyFrame app = new MyFrame();
	}
}
