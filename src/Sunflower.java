import java.awt.event.ActionEvent;
import javax.swing.Timer;


public class Sunflower extends Plant{

	Timer sunProduceTimer;

	public Sunflower(Game gm, int x, int y) {
		super(gm, x, y);
		sunProduceTimer = new Timer(12000,(ActionEvent e) -> {
            Money money = new Money(gm,60 + x*100,110 + y*120,130 + y*120);
            gm.money.add(money);
            gm.add(money,new Integer(1));
        });
        sunProduceTimer.start();
	}
	
}
