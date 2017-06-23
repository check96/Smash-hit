package editor;

import java.awt.image.BufferedImage;

public class Icon
{
	private int x;
	private int y;
	private BufferedImage image;	

	public Icon(int x, int y, BufferedImage _image)
	{
		this.x = x;
		this.y = y;
		this.image = _image;
	}

	public BufferedImage getImage() {return image;}
	public int getX() {return x;}
	public int getY() {return y;}

	public void setImage(BufferedImage image) {this.image = image;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}		
}
