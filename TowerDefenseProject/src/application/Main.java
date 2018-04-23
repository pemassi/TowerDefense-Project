package application;
	
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Kind.MapSoruceType;
import Kind.TowerType;
import Modules.TowerDefenseMangager;
import Modules.TowerDefenseMangager.ScreenUpdate;
import Unit.Pos;
import Unit.Size;
import util.Log;

/**
 * 
 * Main Class for displaying
 * 
 * @author kyungyoonkim
 *
 */
public class Main extends JPanel implements MouseListener {
	
	//Global
	private static 	JFrame 					frame 	= null;
	public static 	Main 					panel 	= null;
	private static 	TowerDefenseMangager 	manager = null;
	
	//Instance
	private boolean isMouseInScreen = false;
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		try
		{
			//Draw a frame
			if(isMouseInScreen)	manager.update(g, panel.getMousePosition());	
			else manager.update(g, null);	
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	public static void main (String [] args){
		
		//Create Manager Class Object
		manager = new TowerDefenseMangager(screenupdate);
		
		//Set Map
		manager.setMap(MapSoruceType.MAP_2);	
		
		//Get Map real size
		Size screenSize = manager.getScreenSize();
		
		//Create window
		frame = new JFrame("Tower Defense");
		
		//Add panel
		panel = new Main();
		frame.getContentPane().add(panel);
		frame.setSize(screenSize.getWidth(), screenSize.getHeigth());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		JButton button = new JButton("Start Game!");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Start Game
				manager.start();
				
				button.setVisible(false);
				
			}
		});
		
		frame.setLayout(null);
		panel.setLayout(null);
		
		button.setBounds(screenSize.getWidth()- 150, screenSize.getHeigth() - 100, 130, 50);
		panel.add(button);
		
		
		//Add event listener
		panel.addMouseListener(panel);
		
		manager.startDrawingThread();
		
		
	
	}
	
	private static ScreenUpdate screenupdate = new ScreenUpdate() {

		public void update() {
			//Update screen			
			panel.repaint();
		}
				
	};

	@Override
	public void mouseClicked(MouseEvent e) {	
		int x = e.getX();
		int y = e.getY();
		
		Log.d("Mouse Clicked (x:" + x + "/y:" + y + ")");
		
		//Pass to manager where clicked
		manager.clicked(new Pos(x,y));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseInScreen = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseInScreen = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
