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
	private JButton solve,reset,create_maze;
	private JCheckBox real_time;
	private final static String[] operation_list = {"Wall", "Start" ,"End","Erase"};
	final int screen_size=810, resolution =155, space_size =1;
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
		
		real_time = new JCheckBox("Real Time");
		real_time.addItemListener(op_handler);
		
		solve = new JButton("Solve");
		reset = new JButton("Reset");
		create_maze = new JButton("Create Maze");
		solve.addActionListener(button_handler);
		reset.addActionListener(button_handler);
		create_maze.addActionListener(button_handler);
		
		button_panel = new JPanel();
		button_panel.add(operation_box);
		button_panel.add(solve);
		button_panel.add(reset);
		button_panel.add(create_maze);
		button_panel.add(real_time);

		int Pixel_size = (space_size*(1-resolution)+panel_size)/resolution; //fixing the panel size, accounting for rounding errors
		panel_size = (Pixel_size+space_size)*resolution-space_size;
		
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
			if(event.getSource()==solve)
				if(!draw_panel.isBoardValid()) //in case board is invalid
					JOptionPane.showMessageDialog(null,"Couldn't find start and end");  
				else
					draw_panel.solve();        //solve board
			else if(event.getSource() == reset)
				draw_panel.resetTotal(); //total reset
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
			else if(event.getSource() == real_time) {
				draw_panel.setRealTime(real_time.isSelected()); // setting
				solve.setEnabled(!real_time.isSelected());
			}
		}
	}
	
	public static void main(String[] args){
		MyFrame app = new MyFrame();
	}
}
