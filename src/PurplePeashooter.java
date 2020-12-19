import java.awt.event.ActionEvent;

import javax.swing.Timer;


public class PurplePeashooter extends Plant{
	public Timer shoot;
	public PurplePeashooter(Game gm, int x, int y) {
		super(gm, x, y);
		shoot = new Timer(1500, (ActionEvent e) ->{
			if(game.laneZombies.get(y).size() > 0) {
                game.lanePeas.get(y).add(new PurplePea(game, y, 103 + this.x * 100));
                //game.lanePeas.get(y).add(new PurplePea(game, y, 115 + this.x * 103));
            }
		});
		shoot.start();
	}
	public void stop(){
		shoot.stop();
	}
}
