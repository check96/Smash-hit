package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class Menu extends JMenuBar implements ActionListener
{	
	private PreviewPanel panel;
	private FileChooser fileChooser;
	
	private JMenu file = new JMenu("File");
	private JMenu edit = new JMenu("Edit");
	
	private JMenuItem load = new JMenuItem("Load");
	private JMenuItem save = new JMenuItem("Save");
	private JMenuItem exit = new JMenuItem("Exit");
	
	private JMenuItem clear = new JMenuItem("Clear");
	
	public Menu(PreviewPanel _panel)
	{
		super();
		this.panel = _panel;
		fileChooser = new FileChooser(panel);
		
		load.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		
		clear .addActionListener(this);
		
		file.add(save);
		file.add(load);
		file.add(exit);
	
		edit.add(clear);
		
		this.add(file);
		this.add(edit);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == load)
			fileChooser.loadFiles();
		else if(e.getSource() == save)
			fileChooser.saveFile(PreviewPanel.points);
		else if(e.getSource() == exit)	
		{
			System.out.println("exit");
		}
		else if(e.getSource() == clear)
		{
			PreviewPanel.icons.clear();
			for(int i=0; i<PreviewPanel.points.length; i++)
				for(int j=0; j<PreviewPanel.points[i].length; j++)
					PreviewPanel.points[i][j] = 0;
			
			panel.repaint();
		}
	}
}
