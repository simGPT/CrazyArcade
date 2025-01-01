package Game_objects;
import java.awt.Image;

public class Tile implements MovableObject{
	private int x;
	private int y;
	private Image image;
	private int tileState;
	private int pX; // 픽셀단위 x좌표
	private int pY; // 픽셀단위 y좌표
	
	public Tile(int x, int y, Image image, int tileState, int pX, int pY) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.tileState = tileState;
		this.pX = pX;
		this.pY = pY;
		
	}
	@Override
	public Image getImage() {
		return image;
	}
	@Override
	public int getRow() {
		return y;
	}
	@Override
	public int getCol() {
		return x;
	}
	@Override
	public int getpX() {
		pX = 40*x;
		return pX;
	}
	@Override
	public int getpY() {
		pY = 40*y;
		return pY;
	}
	@Override
	public int getTileState() {
		return tileState;
	}
	@Override
	public void setTileState(int tileState) {
		this.tileState = tileState;
	}

}
