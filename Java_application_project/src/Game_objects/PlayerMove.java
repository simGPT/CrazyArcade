package Game_objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import Game_screen.GamePanel;


public class PlayerMove implements KeyListener, ActionListener{
	
    public Tile tile[][];
    public List<Integer> KeyList;
    public Player leftPlayer;
    public Player rightPlayer;
    public GamePanel g;
    
    public PlayerMove(Tile tile[][], List<Integer> KeyList, Player leftPlayer, Player rightPlayer, GamePanel g) {
            this.tile = tile;
            this.KeyList = KeyList;
            this.leftPlayer = leftPlayer;
            this.rightPlayer = rightPlayer;
            this.g = g;
    }
	// 타일 상태가 0 이고 패널 안 영역에서만 움직일 수 있도록 하는 메소드
	public boolean IsMovable(int x, int y) { 
		if(x<0 || x>=GamePanel.blockCount || y<0 || y>=GamePanel.blockCount) { // 패널 넘어가면 안됨
			return false;
		}
		else if(tile[y][x].getTileState() != 0) {
			return false;
		}
		return true;
	}
	public void moving(List<Integer> KeyList, Player leftPlayer, Player rightPlayer) {
		if(KeyList.contains(KeyEvent.VK_UP)||KeyList.contains(KeyEvent.VK_DOWN)||KeyList.contains(KeyEvent.VK_RIGHT)||KeyList.contains(KeyEvent.VK_LEFT)) {
			if(KeyList.contains(KeyEvent.VK_UP) && IsMovable(rightPlayer.getCol(), rightPlayer.getRow()-1)) {
				rightPlayer.y--;
				rightPlayer.pY -= GamePanel.one_block_size;
				//rightPlayer.moveUp();
			}
			else if(KeyList.contains(KeyEvent.VK_DOWN) && IsMovable(rightPlayer.getCol(), rightPlayer.getRow()+1)){
				rightPlayer.y++;
				rightPlayer.pY += GamePanel.one_block_size;
				//rightPlayer.moveDown();
			}
			else if(KeyList.contains(KeyEvent.VK_RIGHT) && IsMovable(rightPlayer.getCol()+1, rightPlayer.getRow())){
				rightPlayer.x++;
				rightPlayer.pX += GamePanel.one_block_size;
				//rightPlayer.moveRight();
			}
			else if(KeyList.contains(KeyEvent.VK_LEFT) && IsMovable(rightPlayer.getCol()-1, rightPlayer.getRow())){
				rightPlayer.x--;
				rightPlayer.pX -= GamePanel.one_block_size;
				//rightPlayer.moveLeft();
			}
		}
		if(KeyList.contains(KeyEvent.VK_W)||KeyList.contains(KeyEvent.VK_S)||KeyList.contains(KeyEvent.VK_D)||KeyList.contains(KeyEvent.VK_A)) {
			if(KeyList.contains(KeyEvent.VK_W) && IsMovable(leftPlayer.getCol(), leftPlayer.getRow()-1)) {
				leftPlayer.y--;
				leftPlayer.pY -= GamePanel.one_block_size;
				//leftPlayer.moveUp();
			}
			else if(KeyList.contains(KeyEvent.VK_S) && IsMovable(leftPlayer.getCol(), leftPlayer.getRow()+1)){
				leftPlayer.y++;
				leftPlayer.pY += GamePanel.one_block_size;
				//leftPlayer.moveDown();
			}
			else if(KeyList.contains(KeyEvent.VK_D) && IsMovable(leftPlayer.getCol()+1, leftPlayer.getRow())){
				leftPlayer.x++;
				leftPlayer.pX += GamePanel.one_block_size;
				//leftPlayer.moveRight();
			}
			else if(KeyList.contains(KeyEvent.VK_A) && IsMovable(leftPlayer.getCol()-1, leftPlayer.getRow())){
				leftPlayer.x--;
				leftPlayer.pX -= GamePanel.one_block_size;
				//leftPlayer.moveLeft();
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) { 
		moving(KeyList, rightPlayer, leftPlayer); // 타이머에 따라 이벤트 발생할때마다 키리스트 상태 확인 후 그에 맞게 캐릭터 이동시키는 메소드
		
		g.repaint();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (!KeyList.contains(e.getKeyCode())) {
	        KeyList.add(e.getKeyCode());
	    }
	}
	@Override
	public void keyReleased(KeyEvent e) {
		KeyList.remove((Integer)e.getKeyCode()); // 누르고 있다가 뗀 키 리스트에서 삭제
	}
	@Override
	public void keyTyped(KeyEvent e) { } // 사용 X
	
}