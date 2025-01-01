package Game_objects;
import java.awt.Image;

public class Block implements MovableObject{
	private int x; // x좌표
	private int y; // y좌표
	private int pX; // 픽셀단위 x좌표
	private int pY; // 픽셀단위 y좌표
	private int tileState; // 해당 블록이 있는 타일의 현상태
	private Image image;
	
	
	public Block(int x, int y, int tileState, Image image, int pX, int pY) { 
		this.x = x;
		this.y = y;
		this.tileState = tileState;
		this.image = image;
		this.pX = pX;
		this.pY = pY;
	}
	@Override
	public Image getImage() { // 블록 객체의 이미지를 리턴하는 멤버함수
		return image;
	}
	@Override
	public int getRow() { // 블록 객체 x좌표 리턴 멤버 함수
		return y;
	}
	@Override
	public int getCol() { // 블록 객체 y좌표 리턴 멤버 함수
		return x;
	}
	@Override
	public int getpX() {
		return pX;
	}
	@Override
	public int getpY() {
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
