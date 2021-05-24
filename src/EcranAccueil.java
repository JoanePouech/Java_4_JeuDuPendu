import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EcranAccueil extends JPanel {
	
	//Variables affichage et transfert texte
	private JLabel titreAccueil = new JLabel ("Bienvenue dans le jeu du PENDU");
	private JTextArea texteAccueil;
	private ImageIcon image = new ImageIcon ("PageDeGarde.jpg");
	private JLabel imageAccueil = new JLabel (image);
	private String ligne = "";
	private String txt = "";

	// Constructeur
	public EcranAccueil() {
		try {
			try {
				BufferedReader lecteur = new BufferedReader(new FileReader("accueil.txt"));
				while ((ligne = lecteur.readLine()) != null) {
					txt += ligne + "\n";
				} 
				lecteur.close();
			} catch (FileNotFoundException e) {
				System.out.println("Fichier introuvable");
			}
		} catch (IOException e) {
			System.out.println("IOException");
		}
		titreAccueil.setFont(new Font("Comic sans MS",Font.BOLD,30));
		titreAccueil.setHorizontalAlignment(JLabel.CENTER);
		texteAccueil = new JTextArea(txt);
		texteAccueil.setEditable(false);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(titreAccueil, BorderLayout.NORTH);
		add(imageAccueil, BorderLayout.CENTER);
		add(texteAccueil, BorderLayout.SOUTH);		
	}	
}
