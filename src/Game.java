import java.awt.ActiveEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;


public class Game extends JLayeredPane implements MouseMotionListener{
	public Game game;
	int mouseX , mouseY;
	public int x;
	public int y;
	
	JLabel sunScoreboard;
	Image bgImage;
    Image peashooterImage;
    Image purplepeashooter;
    Image sunflowerImage;
    Image peaImage;
    Image purplepea;
    Image normalZombieImage;
    Image yellowheadzombie;
    Image intro;
    ArrayList<ArrayList<Zombie>> laneZombies;
    ArrayList<ArrayList<Pea>> lanePeas;
    ArrayList<Money> money;
	Object active = Object.None;
	Collider[] collide;
	
	Timer sunProducer;
	Timer moveTime;
	Timer redrawTime;
	Timer zombie;
	public Game(JLabel sunScoreboard){
		setSize(1024,800);
        setLayout(null);
        addMouseMotionListener(this);
        this.sunScoreboard = sunScoreboard;
        setSunScore(500);
        bgImage  = new ImageIcon(this.getClass().getResource("images/mainBG.png")).getImage();
        
        peashooterImage = new ImageIcon(this.getClass().getResource("images/plants/peashooter.gif")).getImage();
        purplepeashooter = new ImageIcon(this.getClass().getResource("images/plants/freezepeashooter.gif")).getImage();
        sunflowerImage = new ImageIcon(this.getClass().getResource("images/plants/sunflower.gif")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("images/pea.png")).getImage();
        purplepea = new ImageIcon(this.getClass().getResource("images/freezepea.png")).getImage();

        normalZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie1.png")).getImage();
        yellowheadzombie = new ImageIcon(this.getClass().getResource("images/zombies/zombie2.png")).getImage();

        laneZombies = new ArrayList<>();
        laneZombies.add(new ArrayList<>()); //line 1
        laneZombies.add(new ArrayList<>()); //line 2
        laneZombies.add(new ArrayList<>()); //line 3
        laneZombies.add(new ArrayList<>()); //line 4
        laneZombies.add(new ArrayList<>()); //line 5

        lanePeas = new ArrayList<>();
        lanePeas.add(new ArrayList<>()); //line 1
        lanePeas.add(new ArrayList<>()); //line 2
        lanePeas.add(new ArrayList<>()); //line 3
        lanePeas.add(new ArrayList<>()); //line 4
        lanePeas.add(new ArrayList<>()); //line 5
        
        
        
        redrawTime = new Timer(25,(ActionEvent e) -> {
            repaint();
        });
        redrawTime.start();
        moveTime = new Timer(45,(ActionEvent e) -> move());
        moveTime.start();
        money = new ArrayList<>();
        sunProducer = new Timer(4000,(ActionEvent e) -> {
            Random rnd = new Random();
            Money m = new Money(this,rnd.nextInt(800)+100,0,rnd.nextInt(250)+400);
            money.add(m);
            add(m,new Integer(1));
        });
        sunProducer.start();
        
        collide = new Collider[45];
        for (int i = 0; i < 45; i++) {
            Collider a = new Collider();
            a.setLocation(44 + (i%9)*100,109 + (i/9)*120);
            a.setAction(new PlantAction((i%9),(i/9)));
            collide[i] = a;
            add(a,new Integer(0));
        }
        
        
    zombie = new Timer(3000, (ActionEvent e) -> {
    	Random rand = new Random();
    	int t = rand.nextInt(5);
    	Zombie z = null;
    	String type = rand.nextBoolean() ? "BasicZombie" : "YellowHeadZombie";
    	for (int i = 0; i<5;i++){
    		z = Zombie.getZombie("BasicZombie", Game.this, t);
    		if (level == 1){
    			z.health = 2500;
    			z = Zombie.getZombie(type,Game.this,t);
    		}
    	}
    	laneZombies.get(t).add(z);
    });   
    zombie.start();
	}
	private int sunScore;

    public int getSunScore() {
        return sunScore;
    }

    public void setSunScore(int sunScore) {
        this.sunScore = sunScore;
        sunScoreboard.setText(String.valueOf(sunScore)+" USD");
    }
    
    
    
    public void move(){ // handle every single movement
    	for (int i = 0; i < 5 ; i++) {
            for(Zombie z : laneZombies.get(i)){
                z.move();
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                p.move();
            }

        }

        for (int i = 0; i < money.size() ; i++) {
            money.get(i).move();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
        g.drawImage(bgImage,0,0,null);
        
        for (int i = 0; i < 45; i++) {
            Collider c = collide[i];
            if(c.plant != null){
                Plant p = c.plant;
                if(p instanceof Peashooter){
                    g.drawImage(peashooterImage,60 + (i%9)*100,129 + (i/9)*120,null);
                }
                if(p instanceof PurplePeashooter){
                    g.drawImage(purplepeashooter,60 + (i%9)*100,129 + (i/9)*120,null);
                }
                if(p instanceof Sunflower){
                    g.drawImage(sunflowerImage,60 + (i%9)*100,129 + (i/9)*120,null);
                }
            }
        }
        
        for (int i = 0; i < 5; i++){
        	
        	for(Zombie z : laneZombies.get(i)){
                if(z instanceof BasicZombie){
                    g.drawImage(normalZombieImage,z.x,109+(i*120),null);
                }
                if(z instanceof YellowHeadZombie){
                	g.drawImage(yellowheadzombie,z.x,109+(i*120),null);
                }
            }
        	
        	for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                if(p instanceof PurplePea){
                    g.drawImage(purplepea, p.x, 140 + (i * 120), null);
                }else {
                    g.drawImage(peaImage, p.x, 140 + (i * 120), null);
                }
            }
        }
}


    class PlantAction implements ActionListener{

		int x,y;
		public PlantAction(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		@Override
    	public void actionPerformed(ActionEvent e) {
        	if(active == Object.Sunflower){
            	if(getSunScore()>=50) {
                	collide[x + y * 9].setPlant(new Sunflower(Game.this, x, y));
                	setSunScore(getSunScore()-50);
            	}
        	}
        	if(active == Object.Peashooter){
            	if(getSunScore() >= 100) {
                	collide[x + y * 9].setPlant(new Peashooter(Game.this, x, y));
                	setSunScore(getSunScore()-100);
            	}
        	}

        	if(active == Object.PurplePeashooter){
            	if(getSunScore() >= 175) {
                	collide[x + y * 9].setPlant(new PurplePeashooter(Game.this, x, y));
                	setSunScore(getSunScore()-175);
            	}
        	}
    	}
    }
    
		@Override
		public void mouseDragged(MouseEvent e) {

    	}

    	@Override
    	public void mouseMoved(MouseEvent e) {
        	mouseX = e.getX();
        	mouseY = e.getY();
    	}
    	static int progress = 0;
    	public static int level = 0;
    	public static void checkLevel(int n){
    		progress = progress + n;
    		System.out.println("Number of zombie(s) die: "+ progress);
    		if (progress > 15){
    			if(level < 2){
    			JOptionPane.showMessageDialog(null,"More zombies are coming");
    			Window.w.dispose();
    			level++;
        		System.out.println("Level: "+level);
    			Window.w = new Window();
    			}
    			else {
    			JOptionPane.showMessageDialog(null,"THANK YOU FOR PLAYING");
    			System.exit(0);
    			}
    			progress = 0;
    		}
    	}
	}














