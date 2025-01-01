package Game_objects;
import java.awt.Image;

public class Result {
	private Image image;
	private int pX;
	private int pY;
	public Result(int pX, int pY, Image image) {
		this.pX = pX;
		this.pY = pY;
		this.image = image;
	}
	public int getpX() {
		return pX;
	}
	public int getpY() {
		return pY;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Image getImage() {
		return image;
	}

}
