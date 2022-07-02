import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	
	Frame(){
		
		this.add(new Painel());
		this.setTitle("LSD Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
