package Game_objects;
import java.awt.Image;
import Game_screen.GamePanel;

public class Player implements MovableObject{
	public int x; // 캐릭터 초기 위치 x좌표
	public int y; // 캐릭터 초기 위치 y좌표
	public int pX; // 캐릭터의 현 위치의 픽셀단위 x좌표
	public int pY; // 캐릭터의 현 위치의 픽셀단위 y좌표
	public Image image; // 캐릭터 상태 이미지
	private int tileState; // 현재 캐릭터가 있는 타일의 상태
	public int bombCount; // 폭탄 설치 개수 능력치
	public int bombLength; // 폭탄 폭발 범위 능력치
	public int imageX; // 캐릭터 이미지 x 픽셀
	public int imageY; // 캐릭터 이미지 y 픽셀
	public int IsAlive;
	
	public Player(int x, int y, Image image, int tileState, int bombCount, int bombLength, int pX, int pY, int IsAlive) {
		this.x = x; // 캐릭터 초기 위치 x좌표
		this.y = y; // 캐릭터 초기 위치 y좌표
		this.image = image;
		this.tileState = tileState;
		this.bombCount = bombCount;
		this.bombLength = bombLength;
		this.pX = pX; // 캐릭터의 현 위치의 픽셀단위 x좌표
		this.pY = pY; // 캐릭터의 현 위치의 픽셀단위 y좌표
		this.IsAlive = IsAlive;
	}
	@Override
	public Image getImage() { // 캐릭터 상태 이미지 리턴 멤버 함수
		return image;
	}
	@Override
	public int getRow() { // 캐릭터 y좌표 리턴 멤버 함수
		return y;
	}
	@Override
	public int getCol() { // 캐릭터 x좌표 리턴 멤버 함수
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
	public int getBombCount() {
		return bombCount;
	}
	public int getBombLength() {
		return bombLength;
	}
	public int getIsAlive() {
		return IsAlive;
	}
	public void setIsAlive(int IsAlive) {
		this.IsAlive = IsAlive;
	}
	public void setpX(int pX) {
		this.pX = pX;
	}
	public void setpY(int pY) {
		this.pY = pY;
	}
}
