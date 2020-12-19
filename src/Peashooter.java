import java.awt.event.ActionEvent;

import javax.swing.Timer;


public class Peashooter extends Plant{

	
	public Timer shoot;
	public Peashooter(Game gm, int x, int y) {
		super(gm, x, y);
		shoot = new Timer(1500,(ActionEvent e) -> {
            //System.out.println("SHOOT");
            if(gm.laneZombies.get(y).size() > 0) {
                gm.lanePeas.get(y).add(new Pea(gm, y, 103 + this.x * 100));
            }
        });
        shoot.start();
	}
	public void stop(){
		shoot.stop();
	}
}
