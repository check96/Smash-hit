package editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Converter
{
	public static void fromFile(BufferedReader read)
	{
		for(int i=0; i<PreviewPanel.points.length; i++)
		{
			String str = "";
			try
			{
				str = read.readLine();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			for(int j=0; j<PreviewPanel.points[i].length; j++)
			{
					PreviewPanel.points[i][j] = str.charAt(j) - 48; 	// meno 48 perchè read() restituisce il numero in codice ASCII e 48 rappresenta lo 0
			}
		}
	}
	
	public static void toFile(BufferedWriter write)
	{
		for(int i=0; i<PreviewPanel.points.length; i++)
		{
			for(int j=0; j<PreviewPanel.points[i].length; j++)
			{
				try
				{
					write.append(Integer.toString(PreviewPanel.points[PreviewPanel.points.length-i][j]).charAt(0));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				write.newLine();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
