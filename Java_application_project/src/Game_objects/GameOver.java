package Game_objects;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import Game_screen.GamePanel;


public class GameOver implements ActionListener{
	public Player rightPlayer;
	public Player leftPlayer;
	public GamePanel g;
	public Image bombTrap;
	public Result rightResult;
	public Result leftResult;
	public Image Win;
	public Image Lose;
	public List <Result> resultList;
	
	public GameOver(Player rightPlayer, Player leftPlayer, Image bombTrap, Result rightResult, Result leftResult, Image Win, Image Lose, List <Result> resultList) {
		this.rightPlayer = rightPlayer;
		this.leftPlayer = leftPlayer;
		this.bombTrap = bombTrap;
		this.Win = Win;
		this.Lose = Lose;
		this.rightResult = rightResult;
		this.leftResult = leftResult;
		this.resultList = resultList;
		/*
		try {
     		bombTrap = ImageIO.read(new File("./Image/bombTrap.png"));
     	}catch(IOException e) {
     		System.out.println("no image");
     		System.exit(1);
     	}*/

	}
	
	public void IsLeftDie() { // 왼쪽 캐릭터가 죽었을때
		if(leftPlayer.getIsAlive()==0) {
			leftPlayer.image = bombTrap;
			
			Timer DieTimer = new Timer(3000, e->{ // 이거 나중에 캐릭터 게임 오버되고 처리할 내용들 넣을 부분
				leftResult = new Result(5, GamePanel.height - 5 - 58, Lose);
				rightResult = new Result(GamePanel.width - 5 - 256, 5, Win);
				resultList.add(leftResult);
				resultList.add(rightResult);
			});
			
			DieTimer.setRepeats(false); 
		    DieTimer.start();
		}
	}
	public void IsRightDie() {  // 오른쪽 캐릭터가 죽었을때
		if(rightPlayer.getIsAlive()==0) {
			rightPlayer.image = bombTrap;
			
			Timer DieTimer = new Timer(3000, e->{ // 이거 나중에 캐릭터 게임 오버되고 처리할 내용들 넣을 부분
				leftResult = new Result(5, 5, Win);
				rightResult = new Result(GamePanel.width - 5 - 333, GamePanel.height - 5 - 58, Lose);
				resultList.add(leftResult);
				resultList.add(rightResult);
			});
			
			DieTimer.setRepeats(false); 
		    DieTimer.start();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		IsLeftDie();
		IsRightDie();
	}

}
