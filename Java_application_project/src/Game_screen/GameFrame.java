package Game_screen;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	public GameFrame() {
       
		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Crazy Arcade!");
        this.setResizable(false); // 사용자 임의로 화면 크기 조절하는것을 방지
        this.add(new GamePanel()); // GamePanel 클래스로 만든 객체를 프레임에 삽입
        this.pack();
		this.setLocationRelativeTo(null); // 화면 중앙에 컨테이너 배치
       
        
        this.setVisible(true);
    }
	
}
