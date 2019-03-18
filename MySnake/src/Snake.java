import javax.swing.*;
import java.awt.EventQueue;
public class Snake extends JFrame {
	public Snake() {
		makeUI();
	}
	private void makeUI() {
		add(new Board());
		
		setResizable(false);
		pack();
		
		setLocationRelativeTo(null);
		
		setTitle("Snake");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
		JFrame b = new Snake();
		b.setVisible(true);
		}
		);

	}

}
