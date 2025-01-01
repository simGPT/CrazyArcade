package Game_objects;
import java.awt.Image;

public interface MovableObject { // 인터페이스에서는 선언만
	int getRow();
	int getCol();
	Image getImage();
	int getpX();
	int getpY();
	void setTileState(int tileState);
	int getTileState();
}
