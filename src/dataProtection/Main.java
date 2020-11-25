package dataProtection;

import javax.swing.JFrame;

public class Main 
{
	static GUI gui;

	public static void main(String[] args) 
	{
		gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.setSize(550, 600);
	}

}
