import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;


@SuppressWarnings("serial")
public class Painel extends JPanel implements ActionListener {
    public Painel p = this;
    static final int SCREEN_WIDTH =600;
    static final int SCREEN_HEIGHT =600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 75;
    int x[] = new int[GAME_UNITS];
    int y[] = new int[GAME_UNITS];
    int partesdocorpo = 6;
    int macascomidas;
    int macaX;
    int macaY;
    char direcao = 'D';
    boolean correndo = false;
    Timer timer;
    Random random;
    File Clip = new File("nice.WAV");
    File clip3 = new File("Black.WAV");
    static Clip clip2;
    static Clip clip5;
    boolean playDmusic = false;
    boolean Retry = false;
  
 static boolean Pausado = false;
	
	Painel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		novaMaca();
		correndo = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
        @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);	
	}
	public void draw(Graphics g) {
		
		if(correndo) {
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0,i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			g.fillOval(macaX, macaY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< partesdocorpo;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} 
				else {
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+macascomidas, (SCREEN_WIDTH - metrics.stringWidth("Score: "+macascomidas))/2, g.getFont().getSize());
			
	        if (playDmusic == true) {
	        	g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
	            g.setFont(new Font("Ink Free",Font.BOLD,25));
	            g.drawString("Difficulty Mode", 490, 20);
	        }
		}
		else {
			Retry = true;
			gameOver(g);
		}
}
	public void novaMaca() {
		macaX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		macaY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 
	}
	public void mover() {
		for(int i = partesdocorpo;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direcao) {
		case 'C':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'B':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'E':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'D':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
        class Multi extends Thread{  
            @Override
            public void run(){
         
                if (correndo == false){
                 	try {
                 		PlayRSoundMusic(clip3);
                 	}
                 	catch (Exception e) {
                 		
                 	}
                 }
                 if((x[0] == macaX) && (y[0] == macaY)) {
                    partesdocorpo++;
                    macascomidas++;
                    novaMaca();
                    try {
                        PlaySound(Clip);
                    	} catch (LineUnavailableException ex) {
                    		Logger.getLogger(Painel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
           }
        }
	public void checkCollisao() {
            //If the head collides with the body
		for(int i = partesdocorpo;i>0;i--){
                    if((x[0] == x[i])&& (y[0] == y[i])){
                        correndo = false;
                    }
                }
            //If the head collides with the borders
                if(x[0] < 0){
                    correndo = false;
                }
                if(x[0] > SCREEN_WIDTH){
                    correndo = false;
                }
                if(y[0] < 0){
                    correndo = false;
                }
                if(y[0] > SCREEN_HEIGHT){
                    correndo = false;
                }
                if (!correndo){
                    timer.stop();
                }
	}
	public void gameOver(Graphics g) {
                try{
                    clip2.stop();
                } catch (Exception e){

                }
		//Score
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metric = getFontMetrics(g.getFont());
		g.drawString("Score: "+macascomidas, (SCREEN_WIDTH - metric.stringWidth("Score: "+macascomidas))/2, g.getFont().getSize());
		
		//GameOver
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        //Retry
        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        g.drawString("Retry?", 250, 180 );
        g.drawString("No More LSD :(", 220, 120 );
        
        //Difficulty
        if (playDmusic == true) {
        	g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            g.drawString("Difficulty Mode", 490, 20);
        }
	}
	public void pausa() {
		Painel.Pausado = true;
		timer.stop();
	}
	public void retomar() {
		Painel.Pausado = false;
		timer.start();
	}
	public void Dificuldade() {
		playDmusic = true;
		macascomidas = 0;
		partesdocorpo = 6;
		repaint();
		timer.restart();
		startGame();
	}
        public void Resetar(){
        	Retry = false;
            p.x = new int[GAME_UNITS];
            p.y = new int[GAME_UNITS];
            direcao = 'D';
            partesdocorpo = 6;
            macascomidas = 0;
            startGame();
        }
        static void PlaySound(File Sound) throws LineUnavailableException{
            try{
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(Sound));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength()/1000);
            } catch(Exception e){
                
            }
        }
        static void PlaySoundMusic(File Sound) throws LineUnavailableException{
            try{
                clip2 = AudioSystem.getClip();
                clip2.open(AudioSystem.getAudioInputStream(Sound));
                clip2.start();
                Thread.sleep(clip2.getMicrosecondLength()/1000);
            } catch(Exception e){
                
            }  
        }
        static void PlayRSoundMusic(File Sound) {
            try{
                clip5 = AudioSystem.getClip();
                clip5.open(AudioSystem.getAudioInputStream(Sound));
                clip5.start();
                Thread.sleep(clip5.getMicrosecondLength()/1000);
            } catch(Exception e){
                
            }
   }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(correndo) {
			mover();
                        Multi t1=new Multi();
                        t1.start();
			checkCollisao();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                if(direcao != 'D'){
                                    direcao = 'E';
                                }
                                break;
                            case KeyEvent.VK_RIGHT:
                                if(direcao != 'E'){
                                    direcao = 'D';
                                }
                                break;
                            case KeyEvent.VK_UP:
                                if(direcao != 'B'){
                                    direcao = 'C';
                                }
                                break;
                            case KeyEvent.VK_DOWN:
                                if(direcao != 'C'){
                                    direcao = 'B';
                                }
                                break;
                            case KeyEvent.VK_SPACE:
                            	if(Painel.Pausado) {
                            		retomar();
                            	} else {
                            		pausa();
                            	}
                            	break;
                            case KeyEvent.VK_ESCAPE:
                            	Dificuldade();
                            	break;
                            case KeyEvent.VK_ENTER:
                                Resetar();
                                break;
                        }
		}
	}
}
