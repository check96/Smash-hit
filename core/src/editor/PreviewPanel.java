package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import videogame.GameConfig;

@SuppressWarnings("serial")
public class PreviewPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private int width = 500;
	private int height = 600;
	private int dimIconX = width/GameConfig.ROOM_COLUMN;
	private int dimIconY = height/GameConfig.ROOM_ROW;
	public BufferedImage image = null;
	public int id = 0;	
	public static int[][] points = new int[GameConfig.ROOM_ROW][GameConfig.ROOM_COLUMN];
	public static Vector<Icon> icons = new Vector<Icon>();
	
	public PreviewPanel(int numLevels) 
	{
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < points[i].length; j++)
				points[i][j] = 0;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0,0,800,600);
		
		for(int i = 0; i < width; i += dimIconX)
		{
			g.setColor(Color.WHITE);
			g.fillRect(i,0,1,height);
		}

		for(int i = 0; i < height; i += dimIconY)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0,i, width,1);
		}

		
		for(int i=0; i<icons.size(); i++)
		{
			Icon temp = icons.get(i);
			g.drawImage(temp.getImage(),temp.getX() * dimIconX, temp.getY() * dimIconY, dimIconX, dimIconY,null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(e.getX() <= width && e.getY() <= height)
			mousePressed(e);
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		int x = e.getX() / dimIconX;
		int y = e.getY() / dimIconY;
		removeDuplicate(x, y);
		
		Icon i = new Icon(x,y,image);
		points[GameConfig.ROOM_ROW-1 - y][x] = id;
		
		icons.add(i);
		repaint();
	}
	
	private void removeDuplicate(int x, int y)
	{
		for(int i=0; i<icons.size(); i++)
		{
			Icon temp = icons.get(i);
			if(temp.getX() == x && temp.getY() == y)
			{
				icons.remove(i);
				return;
			}	
		}
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
