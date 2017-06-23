package editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser 
{
  private PreviewPanel panel;
  private JFileChooser fileChooser = new JFileChooser();
  private BufferedReader read = null;
  private BufferedWriter write = null;
  
  public FileChooser(PreviewPanel _panel)
  {
	  super();
	  this.panel = _panel;
	  fileChooser.setFileFilter(new TxtFileFilter());
  }
    
  public void loadFiles()
  {
      try
      {
        int n = fileChooser.showOpenDialog(this);
        if (n == JFileChooser.APPROVE_OPTION)
        {
			File f = fileChooser.getSelectedFile();
			read = new BufferedReader(new FileReader(f));
			Converter.fromFile(read);
			read.close();
        }
      } catch (Exception ex) {}
      
      uploadIcons();
    }

    public void saveFile(int[][] points)
    {
      try
      {
        int n = fileChooser.showSaveDialog(this);
        if (n == JFileChooser.APPROVE_OPTION)
        {
			File f = fileChooser.getSelectedFile();
			write = new BufferedWriter(new FileWriter(f));
			Converter.toFile(write, points);
			write.flush();
			write.close();
        }
      } catch (Exception ex) {}
    }

	private void uploadIcons()
	{
		PreviewPanel.icons.clear();
		
		for(int i=0; i<PreviewPanel.points.length; i++)
			for (int j = 0; j < PreviewPanel.points[i].length; j++)
				for(int id = 1; id <= 7; id++)
					if(PreviewPanel.points[i][j] == id)
						PreviewPanel.icons.add(new Icon(j,i,toolsPanel.image.get(id)));
		this.panel.repaint();
	}

}