package Game_objects;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import Game_screen.GamePanel;


public class BombExplosion implements KeyListener, ActionListener{
	public Tile tile[][];
	public Block blocks[][];
	public List<Integer>KeyList;
	public List<WaterBomb> leftPlayerBomb; // 물폭탄 객체를 넣은 리스트
	public List<WaterBomb> rightPlayerBomb;
	public Image Bomb = null; //물폭탄 이미지
	public Player leftPlayer;
	public Player rightPlayer;
	public GamePanel g;
	public Timer bombTimer; // 200ms 마다 화면에 그려주기 위해
	public WaterBomb leftBomb[]; // 배열로 해야 능력치만큼 객체 생성 제거 컨트롤 가능 (리스트는 가변이니까 능력치 제한하려고 특별히 배열 씀)
	public WaterBomb rightBomb[];
	
	
	public BombExplosion(Tile tile[][], Block blocks[][], Timer bombTimer, List<Integer>KeyList, List<WaterBomb> leftPlayerBomb, List<WaterBomb> rightPlayerBomb, Player leftPlayer, Player rightPlayer, GamePanel g) {
		 this.tile = tile;
		 this.blocks = blocks;
		 //this.WaterBomb = WaterBomb; // 일단 보류 하자
		 this.bombTimer = bombTimer;
         this.KeyList = KeyList;
         this.leftPlayerBomb = leftPlayerBomb;
         this.rightPlayerBomb = rightPlayerBomb;
         this.leftPlayer = leftPlayer;
         this.rightPlayer = rightPlayer;
         this.g = g;
         
         try {
     		Bomb = ImageIO.read(new File("./Image/WaterBomb.png"));
     	}catch(IOException e) {
     		System.out.println("no image");
     		System.exit(1);
     	}
	}
	// 타일 상태가 0일때만 물폭탄을 설치할 수 있도록 하는 메소드
	public boolean IsInstallable(int x, int y) {
		if(tile[y][x].getTileState()!=0) {
			return false;
		}
		return true;
	}
	// 타일 상태가 0, 1, 3, 4일때만 물폭탄에 의해 블록 제거되거나 폭탄 범위가 지나갈 수 있도록 하기 위한 메소드
	public boolean IsRemovable(int x, int y) {
		if(tile[y][x].getTileState()==1 || tile[y][x].getTileState()==0 || tile[y][x].getTileState()==3 || tile[y][x].getTileState()==4) {
			return true;
		}
		return false;
	}
	
	// 키리스트 상태보고 바로 물폭탄 객체 생성 + 타이머 시작 + 화면 갱신
	public void installBomb(Tile tile[][], List<Integer>KeyList, List<WaterBomb>leftPlayerBomb, List<WaterBomb>rightPlayerBomb, Player leftPlayer, Player rightPlayer) {
		// 왼쪽 캐릭터
		if (leftBomb == null) {
		    leftBomb = new WaterBomb[leftPlayer.getBombCount()]; // 물폭탄 개수 능력치가 배열의 크기인 폭탄 객체 배열 레퍼런스만 만듦 
		}
		for (int i = 0; i < leftPlayer.getBombCount(); i++) {
		    if (KeyList.contains(KeyEvent.VK_SHIFT) && 
		        IsInstallable(leftPlayer.getCol(), leftPlayer.getRow()) && 
		        leftPlayerBomb.size() < leftPlayer.getBombCount()) { // 능력치보다 현재 리스트 내 폭탄 객체가 적어야 더 설치 가능하도록

		        tile[leftPlayer.y][leftPlayer.x].setTileState(3); // 타일 상태 변경

		        // 물풍선 객체 생성
		        WaterBomb newBomb = new WaterBomb(leftPlayer.x, leftPlayer.y, Bomb, 3, leftPlayer.x * GamePanel.one_block_size,  leftPlayer.y * GamePanel.one_block_size);
		        
		        leftBomb[i] = newBomb; // 배열원소 객체로 초기화
		        leftPlayerBomb.add(newBomb); // 리스트에 폭탄 객체 삽입

		        // 로컬 변수로 고정된 폭탄 참조-> 왜 람다식은 로컬변수로 안하면 자꾸 오류가 날까
		        final int bombX = newBomb.x; // 폭탄의 x 좌표
		        final int bombY = newBomb.y; // 폭탄의 y 좌표

		        // 5초 후 왼쪽 캐릭터의 물폭탄 리스트에서 폭탄 객체 삭제
		        Timer deleteTimer = new Timer(5000, e -> {
		            leftPlayerBomb.remove(newBomb); // 리스트에서 삭제
		            tile[bombY][bombX].setTileState(0); // 폭탄 제거된 타일 상태 다시 0으로
		            int length = leftPlayer.getBombLength(); // 폭탄 범위 능력치 접근
		            
		            // 위 방향으로 폭발 범위 만큼 블록 제거 반복문
		            int upY = bombY; 
		            while((upY >=0 && upY<11) && upY >= bombY-length) { // 위쪽으로의 폭발 범위
		            	if(IsRemovable(bombX, upY)) { // 타일 상태 0, 1 일때 폭발 범위가 직진할 수 있게 하고
		            		if(leftPlayer.x==bombX && leftPlayer.y==upY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
	            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
	            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
	            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(rightPlayer.x==bombX && rightPlayer.y==upY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
		            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
		            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(blocks[upY][bombX]!=null) { // 그 중 1일때만 블록이 있으니까 블록 제거 하도록 함
		            			g.ObjectList.remove(blocks[upY][bombX]); // 상 블록 제거
		            			tile[upY][bombX].setTileState(0);
		            			
		            			
		            		}
		            		upY--; // 폭발 범위 한 칸씩 위로
		            	}
		            	else
		            		break; // 만약 블록 상태가 2인 애한테 막혔으면 어차피 폭발 범위는 더 직진 불가
		            }
		            // 아래 방향으로 폭발 범위만큼 블록 제거 반복문
		            int downY = bombY; 
		            while((downY >=0 && downY<11) && downY <= bombY+length) { // 아래쪽으로의 폭발 범위
		            	if(IsRemovable(bombX, downY)) { // 타일 상태 0, 1 일때 폭발 범위가 직진할 수 있게 하고
		            		if(leftPlayer.x==bombX && leftPlayer.y==downY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
	            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
	            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
	            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(rightPlayer.x==bombX && rightPlayer.y==downY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
		            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
		            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(blocks[downY][bombX]!=null) { // 그 중 1일때만 블록이 있으니까 블록 제거 하도록 함
		            			g.ObjectList.remove(blocks[downY][bombX]); // 하 블록 제거
		            			tile[downY][bombX].setTileState(0);
		            			
		            		}
		            		downY++; // 폭발 범위 한 칸씩 아래로
		            	}
		            	else
		            		break; // 만약 블록 상태가 2인 애한테 막힘과 동시에 폭발 범위 더 이상 직진 불가
		            }
		            // 오른쪽 방향으로 폭발 범위 만큼 블록 제거 반복문
		            int rightX = bombX;
		            while((rightX>=0 && rightX<11) && rightX <= bombX+length) {
		            	if(IsRemovable(rightX, bombY)) {
		            		if(leftPlayer.x==rightX && leftPlayer.y==bombY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
	            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
	            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
	            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(rightPlayer.x==rightX && rightPlayer.y==bombY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
		            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
		            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(blocks[bombY][rightX]!=null) {
		            			g.ObjectList.remove(blocks[bombY][rightX]);
		            			tile[bombY][rightX].setTileState(0);
		            			
		            		}
		            		rightX++;
		            	}
		            	else
		            		break;
		            }
		            // 왼쪽 방향으로 폭발 범위만큼 블록 제거 반복문
		            int leftX = bombX;
		            while((leftX>=0 && leftX<11) && leftX >= bombX-length) {
		            	if(IsRemovable(leftX, bombY)) {
		            		if(leftPlayer.x==leftX && leftPlayer.y==bombY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
	            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
	            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
	            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(rightPlayer.x==leftX && rightPlayer.y==bombY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
		            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
		            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
	            			}
		            		if(blocks[bombY][leftX]!=null) {
		            			g.ObjectList.remove(blocks[bombY][leftX]);
		            			tile[bombY][leftX].setTileState(0);
		            			
		            		}
		            		leftX--;
		            	}
		            	else
		            		break;
		            }
		        });
		        deleteTimer.setRepeats(false); 
		        deleteTimer.start();
		        
		        
		     }
		}
		
		// 오른쪽 캐릭터
		if (rightBomb == null) {
		    rightBomb = new WaterBomb[rightPlayer.getBombCount()]; // 물폭탄 개수 능력치가 배열의 크기인 폭탄 객체 배열 레퍼런스만 만듦 
		}
		for(int i=0; i<rightPlayer.getBombCount(); i++) {
			if(KeyList.contains(KeyEvent.VK_ENTER) && IsInstallable(rightPlayer.getCol(), rightPlayer.getRow()) && rightPlayerBomb.size()<rightPlayer.getBombCount()) {
				tile[rightPlayer.y][rightPlayer.x].setTileState(4);
				
				WaterBomb newBomb = new WaterBomb(rightPlayer.x, rightPlayer.y, Bomb, 4, rightPlayer.x * GamePanel.one_block_size,  rightPlayer.y * GamePanel.one_block_size);
				
				rightBomb[i] = newBomb;
				rightPlayerBomb.add(newBomb);
				
				 final int bombX = newBomb.x; // 폭탄의 x 좌표
			     final int bombY = newBomb.y; // 폭탄의 y 좌표
			     
			  // 5초 후 오른쪽 캐릭터의 물폭탄 리스트에서 폭탄 객체 삭제
			        Timer deleteTimer = new Timer(5000, e -> {
			            rightPlayerBomb.remove(newBomb); // 리스트에서 삭제
			            tile[bombY][bombX].setTileState(0); // 폭탄 제거된 타일 상태 다시 0으로
			            int length = rightPlayer.getBombLength(); // 폭탄 범위 능력치 접근
			            
			            // 위 방향으로 폭발 범위 만큼 블록 제거 반복문
			            int upY = bombY; 
			            while((upY >=0 && upY<11) && upY >= bombY-length) { // 위쪽으로의 폭발 범위
			            	if(IsRemovable(bombX, upY)) { // 타일 상태 0, 1 일때 폭발 범위가 직진할 수 있게 하고
			            		if(leftPlayer.x==bombX && leftPlayer.y==upY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
		            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
		            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(rightPlayer.x==bombX && rightPlayer.y==upY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
			            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
			            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
			            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(blocks[upY][bombX]!=null) { // 그 중 1일때만 블록이 있으니까 블록 제거 하도록 함
			            			g.ObjectList.remove(blocks[upY][bombX]); // 상 블록 제거
			            			tile[upY][bombX].setTileState(0);
			            			
			            		}
			            		upY--; // 폭발 범위 한 칸씩 위로
			            	}
			            	else
			            		break; // 만약 블록 상태가 2인 애한테 막혔으면 어차피 폭발 범위는 더 직진 불가
			            }
			            // 아래 방향으로 폭발 범위만큼 블록 제거 반복문
			            int downY = bombY; 
			            while((downY >=0 && downY<11) && downY <= bombY+length) { // 아래쪽으로의 폭발 범위
			            	if(IsRemovable(bombX, downY)) { // 타일 상태 0, 1 일때 폭발 범위가 직진할 수 있게 하고
			            		if(leftPlayer.x==bombX && leftPlayer.y==downY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
		            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
		            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(rightPlayer.x==bombX && rightPlayer.y==downY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
			            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
			            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
			            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(blocks[downY][bombX]!=null) { // 그 중 1일때만 블록이 있으니까 블록 제거 하도록 함
			            			g.ObjectList.remove(blocks[downY][bombX]); // 하 블록 제거
			            			tile[downY][bombX].setTileState(0);
			            		}
			            		downY++; // 폭발 범위 한 칸씩 아래로
			            	}
			            	else
			            		break; // 만약 블록 상태가 2인 애한테 막힘과 동시에 폭발 범위 더 이상 직진 불가
			            }
			            // 오른쪽 방향으로 폭발 범위 만큼 블록 제거 반복문
			            int rightX = bombX;
			            while((rightX>=0 && rightX<11) && rightX <= bombX+length) {
			            	if(IsRemovable(rightX, bombY)) {
			            		if(leftPlayer.x==rightX && leftPlayer.y==bombY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
		            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
		            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(rightPlayer.x==rightX && rightPlayer.y==bombY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
			            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
			            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
			            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(blocks[bombY][rightX]!=null) {
			            			g.ObjectList.remove(blocks[bombY][rightX]);
			            			tile[bombY][rightX].setTileState(0);
			            		}
			            		rightX++;
			            	}
			            	else
			            		break;
			            }
			            // 왼쪽 방향으로 폭발 범위만큼 블록 제거 반복문
			            int leftX = bombX;
			            while((leftX>=0 && leftX<11) && leftX >= bombX-length) {
			            	if(IsRemovable(leftX, bombY)) {
			            		if(leftPlayer.x==leftX && leftPlayer.y==bombY) { // 왼쪽 캐릭터가 폭발 범위 내에 있다면
		            				leftPlayer.setIsAlive(0); // 캐릭터 죽음 처리
		            				leftPlayer.setpX(leftPlayer.x * GamePanel.one_block_size - 10);
		            				leftPlayer.setpY(leftPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(rightPlayer.x==leftX && rightPlayer.y==bombY) { // 오른쪽 캐릭터가 폭발 범위 내에 있다면
			            			rightPlayer.setIsAlive(0); // 캐릭터 죽음 처리
			            			rightPlayer.setpX(rightPlayer.x * GamePanel.one_block_size - 10);
			            			rightPlayer.setpY(rightPlayer.y * GamePanel.one_block_size - 20);
		            			}
			            		if(blocks[bombY][leftX]!=null) {
			            			g.ObjectList.remove(blocks[bombY][leftX]);
			            			tile[bombY][leftX].setTileState(0);
			            		}
			            		leftX--;
			            	}
			            	else
			            		break;
			            }
			        });
			     deleteTimer.setRepeats(false); 
			     deleteTimer.start();
			}
		}
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
		installBomb(tile, KeyList, leftPlayerBomb, rightPlayerBomb, leftPlayer, rightPlayer);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (!KeyList.contains(e.getKeyCode())) {
	        KeyList.add(e.getKeyCode());
	    }
	}
	@Override
	public void keyReleased(KeyEvent e) {
		KeyList.remove((Integer)e.getKeyCode()); 
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
