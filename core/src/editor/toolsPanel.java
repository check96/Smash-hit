package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import GameGui.EditorScreen;
import GameGui.GameManager;

@SuppressWarnings("serial")
public class toolsPanel extends JPanel implements ActionListener
{
	private Editor frame;
	private PreviewPanel pp;
	private JButton next = new JButton("NEXT");
	private JButton play = new JButton("PLAY");
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private BufferedImageLoader loader = new BufferedImageLoader();
	public static ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
	private int numLevelsCreated = 0;
	private GameManager game;
	
	public toolsPanel(PreviewPanel pp, Editor _frame, GameManager _game)
	{
		super();

		this.frame = _frame;
		this.pp = pp;
		this.game = _game;
		this.setLayout(new BorderLayout());
		
		play.addActionListener(this);
		play.setPreferredSize(new Dimension(60, 60));

		next.addActionListener(this);
		next.setPreferredSize(new Dimension(60, 60));

		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/black.png"));
		
		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/desk.png"));
				
		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/printer.png"));
		
		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/plant.png"));

		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/locker.png"));
		
		buttons.add(new JButton());
		image.add(loader.loadImage("/Icons/chair.png"));
		
		JPanel buttonPanel = new JPanel();

		for(int i=0; i<buttons.size(); i++)
		{
			buttons.get(i).addActionListener(this);
			buttons.get(i).setIcon(new ImageIcon(image.get(i).getScaledInstance(30,30,0)));
			buttonPanel.add(buttons.get(i));
		}

		this.add(buttonPanel);

		this.add(next, BorderLayout.SOUTH);
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == next)
		{
			if(numLevelsCreated == frame.numLevels-1)
			{
				EditorScreen.CREATED = true;
				frame.setVisible(false);
			}
			numLevelsCreated++;
			saveLevel();
		}
		
		if(e.getSource() == buttons.get(0))
		{
			pp.image = null;
			pp.id = 0;
		}
		
		for(int i=1; i<buttons.size(); i++)
			if(e.getSource() == buttons.get(i))
			{
				pp.image = this.image.get(i);
				pp.id = i;
			}
	}

	private void saveLevel()
	{
		String level = "";
		for(int i=0; i<PreviewPanel.points.length; i++)
		{
			for(int j=0; j<PreviewPanel.points[i].length; j++)
				level += Integer.toString(PreviewPanel.points[i][j]).charAt(0);
			
//			level += '\n';
		}

		game.editorLevels.putString("level"+numLevelsCreated, level);
		game.editorLevels.flush();
		
		PreviewPanel.icons.clear();
		for(int i=0; i<PreviewPanel.points.length; i++)
			for(int j=0; j<PreviewPanel.points[i].length; j++)
				PreviewPanel.points[i][j] = 0;

		pp.repaint();
	}
}
