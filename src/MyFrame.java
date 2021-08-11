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
	private JButton solve,reset;
	
	private final static String[] operation_list = {"Wall", "Start" ,"End","Erase"};
	final int screen_size=810, panel_size=719, resolution =10, space_size =1;
	
	MyFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(screen_size,screen_size+30);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setResizable(false);
		setLocationRelativeTo(null);
		
		operationHandler op_handler = new operationHandler();
		operation_box= new JComboBox<String>(operation_list);
		operation_box.setSelectedIndex(0);// Default option
		operation_box.addItemListener(op_handler);
		
		button_panel = new JPanel();
		solve = new JButton("Solve");
		reset = new JButton("Reset");
		ButtonHandler button_handler = new ButtonHandler();
		solve.addActionListener(button_handler);
		reset.addActionListener(button_handler);
		
		
		draw_panel = new DrawPanel(panel_size, resolution, space_size, operation_box.getItemAt(0));
		draw_panel.setPreferredSize(new Dimension(panel_size,panel_size));
		
		button_panel.add(operation_box);
		button_panel.add(solve);
		button_panel.add(reset);

		
		add(button_panel);
		add(draw_panel);

		setVisible(true);
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource()==solve)
				draw_panel.solve();
			else if(event.getSource() == reset)
				draw_panel.reset();
		}
	}
	
	private class operationHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			if (event.getSource() == operation_box && event.getStateChange() == event.SELECTED)
				draw_panel.setCurrentOp(operation_box.getSelectedItem().toString());
		}
	}
	
	public static void main(String[] args){
		MyFrame app = new MyFrame();
	}
}
