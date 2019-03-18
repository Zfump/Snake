import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.sound.sampled.*;
import java.io.File;


public class Board extends JPanel implements ActionListener {
	private final int BOARD_X = 900; // x dimension of the board
	private final int BOARD_Y = 900; // y dimension of the board
	private final int DOT_SIZE = 30; // will change when know size of dot
	private final int MAX_SIZE = 900; // max size of array so also max size of
										// snake
	private final int DELAY = 150; // delay in milliseconds for the timer

	private int x[] = new int[MAX_SIZE]; // sets max size for snake
	private int y[] = new int[MAX_SIZE];

	private int body; // initializes the body
	private int score;
	private int food_x; // initializes food x coordinate
	private int food_y; // initializes food y coordinate

	private boolean right = false; // true if snake is moving right
	private boolean left = true; // true if snake is moving left
	private boolean up = false; // true if snake is moving up
	private boolean down = false; // true if snake is moving down
	private boolean ingame = true; // checks if game is running

	private Timer timer; // makes a timer
	private Image segment; // image for body
	private Image head; // image for head
	private Image food; // image for food
	private Image Background;
	public Board() {
		MakeBoard();
	}

	private void MakeBoard() {
		addKeyListener(new Listener());
		setBackground(Color.black);
		setPreferredSize(new Dimension(BOARD_X, BOARD_Y));
		setFocusable(true);
		loadImages();
		startGame();

		hideFood();// hides the food

	}

	static private ImageIcon rotateImageIcon(ImageIcon picture, double angle) {
        // FOR YOU ...
        int w = picture.getIconWidth();
        int h = picture.getIconHeight();
        int type = BufferedImage.TYPE_INT_RGB;  // other options, see api
        BufferedImage image = new BufferedImage(h, w, type);
        Graphics2D g2 = image.createGraphics();
        double x = (h - w)/2.0;
        double y = (w - h)/2.0;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(angle, w/2.0, h/2.0);
        g2.drawImage(picture.getImage(), at, null);
        g2.dispose();
        picture = new ImageIcon(image);
 
        return picture;
    }
	private void startGame() {
		body = 3;
		score = 0;
		for (int i = 0; i < body; i++) { // makes the snake body 3 long
			x[i] = 300 - i * 30;
			y[i] = 300;
		}

		this.timer = new Timer(DELAY, this); // sets timer refresh rate
		timer.start(); // starts the timer

	}

	private void hideFood() {
		int x = (int) (Math.random() * 30) * DOT_SIZE;
		int y = (int) (Math.random() * 30) * DOT_SIZE;
		if (y <= BOARD_X/45) {
			hideFood();
		} else {
			food_x = x;
			food_y = y;
		}
	}

	private void loadImages() {
		ImageIcon segment = new ImageIcon("src/resources/snake_body.png"); // change
																	// //
																	// picture
		this.segment = segment.getImage();
		ImageIcon food = new ImageIcon("src/resources/apple.png"); // change //
																	// picture
		this.food = food.getImage();
		ImageIcon head = new ImageIcon("src/resources/snake_head.png");
		this.head = head.getImage();
		ImageIcon Background = new ImageIcon("src/resources/grass_background.jpg");
		this.Background = Background.getImage();
	}

	@Override
	public void paintComponent(Graphics i) {
		super.paintComponent(i);
		i.setColor(Color.white);
		i.drawRect(0, 0, BOARD_X, 20);
		i.fillRect(0, 0, BOARD_X, 20);
		populateBoard(i);
		printScore(i);
	}

	private void populateBoard(Graphics i) {
		i.drawImage(Background, 0, 0, this);
		if (ingame) {
			i.drawImage(food, food_x, food_y, this);

			for (int z = 0; z < body; z++) {
				if (z == 0) {
					ImageIcon head = new ImageIcon("src/resources/snake_head.png");
					if (right){
					i.drawImage(rotateImageIcon(head, 3*Math.PI/2).getImage(), x[z], y[z], this);
					}
					else if (left) {
						i.drawImage(rotateImageIcon(head, Math.PI/2).getImage(), x[z], y[z], this);
					}
					else if (up) {
						i.drawImage(rotateImageIcon(head, Math.PI).getImage(), x[z], y[z], this);
					}
					else {
						i.drawImage(this.head, x[z], y[z], this);
					}
				} else {
					i.drawImage(segment, x[z], y[z], this);
				}
				Toolkit.getDefaultToolkit().sync();
			}
		} else {
			gameover(i);
		}

	}

	private void gameover(Graphics i) {
		PlaySound(2);
		String lose = "Game Over";
		Font loss = new Font("Helvetica", Font.BOLD, BOARD_X / 20);
		FontMetrics metric = getFontMetrics(loss);

		i.setColor(Color.white);
		i.setFont(loss);
		i.drawString(lose, (BOARD_X / 2) - 150, BOARD_Y / 2);

	}

	private void printScore(Graphics i) {
		String score = Integer.toString(this.score);
		String output = ("Score: " + score);
		Font scores = new Font("Helvetica", Font.BOLD, BOARD_X/45);
		FontMetrics metric = getFontMetrics(scores);

		i.setColor(Color.white);
		i.setFont(scores);
		i.drawString(output, 20, 20);

	}

	private void move() {

		for (int z = body; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}

		if (left) {
			x[0] -= DOT_SIZE;
		}

		if (right) {
			x[0] += DOT_SIZE;
		}

		if (up) {
			y[0] -= DOT_SIZE;
		}

		if (down) {
			y[0] += DOT_SIZE;
		}
	}

	private void checkCollision() {

		for (int z = body; z > 0; z--) {

			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				ingame = false;
			}
		}

		if (y[0] >= BOARD_Y) {
			ingame = false;
		}

		if (y[0] < 20) {
			ingame = false;
		}

		if (x[0] >= BOARD_X) {
			ingame = false;
		}

		if (x[0] < 0) {
			ingame = false;
		}

		if (!ingame) {
			timer.stop();
		}
	}

	private void PlaySound(int i) {
		if (i == 1) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new File("src/resources/munch.wav")));
				clip.start();
			} catch (Exception exc) {
				exc.printStackTrace(System.out);

			}
		}
			else if (i == 2) {
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File("src/resources/death.wav")));
					clip.start();
				} catch (Exception exc) {
					exc.printStackTrace(System.out);

				}
			}
		}
	


	private void checkFood() {

		if ((x[0] == food_x) && (y[0] == food_y)) {
			PlaySound(1);
			body++;
			score++;
			hideFood();
		}
	}

	@Override
	public void actionPerformed(ActionEvent i) {
		if (ingame) {
			checkFood();
			checkCollision();

			move();
		}
		repaint();

	}

	private class Listener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT) && (!right)) {
				left = true;
				up = false;
				down = false;
			}

			if ((key == KeyEvent.VK_RIGHT) && (!left)) {
				right = true;
				up = false;
				down = false;
			}

			if ((key == KeyEvent.VK_UP) && (!down)) {
				up = true;
				right = false;
				left = false;
			}

			if ((key == KeyEvent.VK_DOWN) && (!up)) {
				down = true;
				right = false;
				left = false;
			}
		}
	}
}