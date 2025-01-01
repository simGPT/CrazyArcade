package Game_screen;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import Game_objects.Block;
import Game_objects.BombExplosion;
import Game_objects.GameOver;
import Game_objects.MovableObject;
import Game_objects.Player;
import Game_objects.PlayerMove;
import Game_objects.Result;
import Game_objects.Tile;
import Game_objects.WaterBomb;


public class GamePanel extends JPanel{
	// 0: 아무것도 없음, 1: 제거 가능 블록 있음, 2: 제거 불가 블록 있음, 3: 왼쪽 캐릭터가 설치한 물폭탄 있음, 4: 오른쪽 캐릭터가 설치한 물폭탄 있음
	public static int state[][] = 
		  { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{2, 2, 1, 1, 1, 1, 1, 1, 2, 0, 1},
			{1, 1, 2, 0, 0, 1, 1, 1, 0, 0, 1},
			{1, 1, 1, 1, 0, 0, 1, 1, 1, 2, 2},
			{1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
			{2, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1},
			{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1},
			{2, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1},
			{2, 2, 0, 1, 1, 1, 1, 1, 2, 2, 1},
			{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1} };
			
	public static int one_block_size = 40; // 한 칸 픽셀 40로 설정
	public static int width = 440; // 총 가로 픽셀
	public static int height = 440; // 총 세로 픽셀
	public static int blockCount = 11; // 가로 세로 모두 블록들 개수는 11개로 설정
	public Image grass_background = null; // 초록색 잔디 이미지
	public Image green_background = null; // 초록색 블록 이미지
	public Image cross_background = null; // 도로 표시 이미지
	public Image gray_background = null; // 회색 아스팔트 이미지
	public Image tree = null; // 나무 이미지
	public Image redHouse = null; // 빨간 집 이미지
	public Image yellowHouse = null; // 노란 집 이미지
	public Image blueHouse = null; // 파란 집 이미지
	public Image halfTree = null; // 나무 밑동 이미지
	public Image redBlock = null; // 파괴 가능 빨강 블록 이미지
	public Image orangeBlock = null; // 파괴 가능 주황 블록 이미지
	Tile tile[][] = new Tile[11][11];
	Block blocks[][] = new Block [11][11]; // 블록객체 2차원 배열로 생성
	public Player leftPlayer;
	public Player rightPlayer;
	public Image redCharacter = null; 
	public Image blueCharacter = null;
	public Image bombTrap = null;
	private Image Win = null;
	private Image Lose = null;
	// 블록, 플레이어 객체들 리스트에 저장해서 row가 작은순으로 먼저 그리기 위해 생성
	public List <MovableObject>ObjectList = new ArrayList <MovableObject>();
	public List <Integer> KeyList = new ArrayList <Integer>(); // 누른 키를 저장하는 리스트
	public Timer timer;
	public List<WaterBomb> leftPlayerBomb = new ArrayList <WaterBomb>();
	public List<WaterBomb> rightPlayerBomb = new ArrayList <WaterBomb>();
	public Timer bombTimer; // 화면에 폭탄이 나타나는거에 관여하는 타이머
	public Timer deleteTimer; // 폭탄 삭제에 관여하는 타이머
	public Timer PlayerTrap;
	
	public Result leftResult;
	public Result rightResult;
	public List <Result> resultList = new ArrayList <Result>();

	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(width, height)); // 패널 사이즈 설정하기 위해
		
		try { // 이미지 파일 불러오기 (이미지가 없을때의 예외 처리 필요)
			grass_background = ImageIO.read(new File("./Image/grass.png"));
			green_background = ImageIO.read(new File("./Image/greenBlock.png"));
			cross_background = ImageIO.read(new File("./Image/cross.png"));
			gray_background = ImageIO.read(new File("./Image/gray.png"));
			tree = ImageIO.read(new File("./Image/tree.png"));
			redHouse = ImageIO.read(new File("./Image/redHouse_40x60.png"));
		    yellowHouse =	ImageIO.read(new File("./Image/yellowHouse_40x60.png"));
			blueHouse =	 ImageIO.read(new File("./Image/blueHouse_40x60.png"));
			halfTree = ImageIO.read(new File("./Image/halfTree_40x60.png"));
			redBlock = ImageIO.read(new File("./Image/redBlock.png"));
			orangeBlock = ImageIO.read(new File("./Image/orangeBlock.png"));
			redCharacter = ImageIO.read(new File("./Image/redCharacter_44x56.png"));
			blueCharacter = ImageIO.read(new File("./Image/blueCharacter_44x56.png"));
			bombTrap = ImageIO.read(new File("./Image/bombTrap.png"));
			Win = ImageIO.read(new File("./Image/win.png"));
			Lose = ImageIO.read(new File("./Image/lose.png"));
			
		}catch(IOException e) {
			System.out.println("no image");
			System.exit(1);
		}
		// 타일 객체 생성하는 반복문
		for(int row=0; row<blockCount; row++) {
			for(int col=0; col<blockCount; col++) {
				if(col<2 || col>8) {
					tile[row][col] = new Tile(col, row, grass_background, state[row][col], col*one_block_size, row*one_block_size);
				}
				else if(col<4 || col>6) {
					tile[row][col] = new Tile(col, row, green_background, state[row][col], col*one_block_size, row*one_block_size);
				}
				else if(col == 4 || col == 6) {
					tile[row][col] = new Tile(col, row, cross_background, state[row][col], col*one_block_size, row*one_block_size);
				}
				else {
					tile[row][col] = new Tile(col, row, gray_background, state[row][col], col*one_block_size, row*one_block_size);
				}
			}
		}
		
		// 블록, 캐릭터 객체들 생성
		for(int row=0; row<blockCount; row++) {
			for(int col=0; col<blockCount; col++) {
				if(col==1&&(row%4==1) || col==9&&(row%4==3) ) { // 나무 위치
					blocks[row][col] = new Block(col, row, state[row][col], tree, col*one_block_size, row*one_block_size-20);
				}
				else if(col==2&&row==2 || col==7&&row==4 || col==8&&row==9) {
					blocks[row][col] = new Block(col, row, state[row][col], redHouse, col*one_block_size, row*one_block_size-20);
				}
				else if(col==8&&row==1 || col==3&&row==6 || col==0&&row==8 ) {
					blocks[row][col] = new Block(col, row, state[row][col], yellowHouse, col*one_block_size, row*one_block_size-20);
				}
				else if(col==0&&row==5 || col==7&&row==5 || col==9&&row==9 ||col==0&&row==9) {
					blocks[row][col] = new Block(col, row, state[row][col], blueHouse, col*one_block_size, row*one_block_size-20);
				}
				else if(col==0&&row==1 ||  col==10&&row==3 ||col==3&&row==10) {
					blocks[row][col] = new Block(col, row, state[row][col], halfTree, col*one_block_size, row*one_block_size-20);
				}
				else if(col==9&&(row==1||row==2) || col==8&&row==2 || col==2&&(row==8||row==9) || col==1&&row==8) {
					// 두개의 캐릭터가 시작할 장소는 적어도 세칸 공백으로 두어야 함
					if(col==8&&row==2) { // 공백 중 오른쪽 캐릭터가 초기에 존재해야하는 공간
						rightPlayer = new Player(col, row, redCharacter, state[row][col], 3, 2, col*one_block_size-2, row*one_block_size-16, 1); 
					}
					else if(col==2&&row==9) { // 공백중 왼쪽 캐릭터가 초기에 존재해야하는 공간
						leftPlayer = new Player(col, row, blueCharacter, state[row][col], 3, 2, col*one_block_size-2, row*one_block_size-16, 1);
					}
				}
				else if((col==3||col==4)&&row==2 || (col==4||col==5)&&row==3 || col==6&&(row==7||row==8) || col==5&&row==8) {
					// 미관상 공백처리
				}
				else { // 파괴 가능 블록들
					if(col%2==0&&row%2==0 || col%2==1&&row%2==1) {
						blocks[row][col] = new Block(col, row, state[row][col], orangeBlock, col*one_block_size, row*one_block_size-18);
					}
					else {
						blocks[row][col] = new Block(col, row, state[row][col], redBlock, col*one_block_size, row*one_block_size-18);
					}
				}
			}
		}
		
		// 리스트에 블록 객체들과 플레이어 객체들 저장
		for(int row=0; row<blockCount; row++) {
			for(int col=0; col<blockCount; col++) {
				if (blocks[row][col] != null) { // 블록이 없고 타일만 있는 부분은 리스트에 저장 X
				    ObjectList.add(blocks[row][col]); // 리스트에 블록 객체들 삽입
				}
			}
		}
		// 리스트에 플레이어 객체 삽입
		ObjectList.add(leftPlayer);
		ObjectList.add(rightPlayer);
		
		// 결과 객체
		
		
		// 폭탄 리스트 내의 객체들을 ObjectList로 옮겼는데 
		//폭탄 리스트 객체가 MovableObject를 구현하니까 리스트에 잘 들어가고 정렬도 잘 되려나..?
		/*
		ObjectList.addAll(leftPlayerBomb);	
		ObjectList.addAll(rightPlayerBomb);
		*/
		timer = new Timer(200, new PlayerMove(tile, KeyList, rightPlayer, leftPlayer, this)); // 이벤트를 200ms에 한번씩
		timer.start(); // 타이머 시작
		
		bombTimer = new Timer(200, new BombExplosion(tile, blocks, bombTimer, KeyList, leftPlayerBomb, rightPlayerBomb, leftPlayer, rightPlayer, this));
		bombTimer.start(); // 타이머 시작
		
		PlayerTrap = new Timer(100, new GameOver(rightPlayer, leftPlayer, bombTrap, rightResult, leftResult, Win, Lose, resultList));
		PlayerTrap.start();
		
		
		this.setFocusable(true); // false값이 기본값이기에 true로 바꿔서 키이벤트 받을 수 있도록
		this.addKeyListener(new PlayerMove(tile, KeyList, rightPlayer, leftPlayer, this)); // 키리스너 이벤트 삽입
		this.addKeyListener(new BombExplosion(tile, blocks, bombTimer, KeyList, leftPlayerBomb, rightPlayerBomb, leftPlayer, rightPlayer, this)); // 키리스너 이벤트 삽입
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 타일 객체들 화면에 그리는 반복문
		for(int row=0; row<blockCount; row++) { 
			for(int col=0; col<blockCount; col++) {
				g.drawImage(tile[row][col].getImage(),tile[row][col].getpX(), tile[row][col].getpY(),null);
			}
		}
		
		// 블록, 캐릭터 객체들 화면에 그리는 반복문
		// row(작은 값 우선) -> col 오름차순 정렬
		ObjectList.sort(Comparator.comparing(MovableObject::getRow).thenComparing(MovableObject::getCol));
				
		for (MovableObject obj : ObjectList) {
			g.drawImage(obj.getImage(), obj.getpX(), obj.getpY(), null); 
		}
		
		for(WaterBomb obj : leftPlayerBomb) {
			g.drawImage(obj.getImage(), obj.getpX(), obj.getpY(), null);
		}
		for(WaterBomb obj : rightPlayerBomb) {
			g.drawImage(obj.getImage(), obj.getpX(), obj.getpY(), null);
		}
		for(Result obj : resultList) {
			g.drawImage(obj.getImage(), obj.getpX(), obj.getpY(), null);
		}
		
			    
	}
	
}
