import javax.swing.JOptionPane;


public class Zombie {
	public int health = 2000;
	public Game game;
	public int lane;
	public boolean isMoving = true;
	public int x = 1012;
	
	public Zombie(Game gm, int Lane){
		this.lane = Lane;
		this.game = gm;
	}
	
	public void move(){
		if (isMoving){
			boolean isCollides = false;
			Collider collide = null;
			for (int i = lane *9; i <( lane+1 )*9;i++){
				if (game.collide[i].plant != null && game.collide[i].isInsideCollider(x)) {
                    isCollides = true;
                    collide = game.collide[i];
                }
			}
			if (!isCollides){
				x --;
				}
				else {
				collide.plant.health -= 10;
				if (collide.plant.health <0){
					collide.removePlant();
					System.out.println("Remove a plant\n");
				}
			}
			if (x < 0){
				isMoving = false;
				JOptionPane.showMessageDialog(game,"YOU FAIL!!!" + "\n" + "Click"+" OK"+" to start again!");
				Window.w.dispose();
				Window.w = new Window();
			}
		}
	}
	
	
	public static Zombie getZombie(String type,Game gm, int lane){
		Zombie z = new Zombie(gm,lane);
		switch(type){
		case "BasicZombie" : z = new BasicZombie(gm,lane);
							break;
		case "YellowHeadZombie" : z = new YellowHeadZombie(gm,lane);
							break;
		}
		return z;
	}
}
