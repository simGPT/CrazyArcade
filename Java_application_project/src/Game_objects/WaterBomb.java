package Game_objects;

import java.awt.Image;

import Game_screen.GamePanel;


public class WaterBomb implements MovableObject{
	public int x;
	public int y;
	private Image image;
	private int tileState;
	private int pX; // 픽셀단위 x좌표
	private int pY; // 픽셀단위 y좌표
	
	public WaterBomb(int x, int y, Image image, int tileState, int pX, int pY) {
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
		pX = GamePanel.one_block_size*x;
		return pX;
	}
	@Override
	public int getpY() {
		pY = GamePanel.one_block_size*y;
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
