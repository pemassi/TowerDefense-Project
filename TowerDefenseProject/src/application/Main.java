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


public class Main extends JPanel implements MouseListener {
	
	private static JFrame frame = null;
	private static Main panel = null;
	private static TowerDefenseMangager manager = null;
	
	private static JLabel lb_life = null;
	
	private boolean isMouseInScreen = false;
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		try
		{		
			//Log.d("Main#paint() called");
			
			if(isMouseInScreen)	manager.update(g, panel.getMousePosition());	
			else manager.update(g, null);	
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	public static void main (String [] args){
		
		manager = new TowerDefenseMangager(screenupdate);
		manager.setMap(MapSoruceType.MAP_2);	
		
		Size screenSize = manager.getScreenSize();
		
		frame = new JFrame("Tower Defense");

		
		//Add components
		lb_life = new JLabel("Life : 30", JLabel.RIGHT);
	
		frame.add(lb_life);
		
		
		//Add panel
		panel = new Main();
		frame.getContentPane().add(panel);
		frame.setSize(screenSize.getWidth(), screenSize.getHeigth());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		
		
		
		
		panel.addMouseListener(panel);
		
		manager.start();
	
	}
	
	private static ScreenUpdate screenupdate = new ScreenUpdate() {

		public void update() {
			
			//Log.d("ScreenUpdate#Update() called");
			
			panel.repaint();
			
		}
				
	};


	private static ActionListener btnListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Log.d("ActionPerformed called");
						
			manager.putTower(TowerType.Tier1, new Pos(0, 0));
			
			System.out.println(((JButton) e.getSource()).getName() + " Clicked");
			
		}
	};


	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Log.d("Mouse Clicked (x:" + x + "/y:" + y + ")");
		
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
