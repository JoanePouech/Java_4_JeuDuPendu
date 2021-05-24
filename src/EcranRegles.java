import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EcranRegles extends JPanel {
	
	//Variables affichage et transfert texte
	JLabel titreRegles = new JLabel ("Le jeu du PENDU");
	JTextArea texteRegles;
	String ligne = "";
	String txt = "";

	// Constructeur
	public EcranRegles() {
		try {
			try {
				BufferedReader lecteur = new BufferedReader(new FileReader("regles.txt"));
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
		texteRegles = new JTextArea(txt);
		texteRegles.setEditable(false);
		titreRegles.setFont(new Font("Comic sans MS",Font.BOLD,30));
		titreRegles.setHorizontalAlignment(JLabel.CENTER);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(titreRegles, BorderLayout.NORTH);
		this.add(texteRegles, BorderLayout.CENTER);		
	}
}
