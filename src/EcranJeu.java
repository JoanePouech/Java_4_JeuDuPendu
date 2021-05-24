import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EcranJeu extends JPanel implements Observable {

	//Variables clavier
	private JPanel clavierPanel = new JPanel ();
	private JButton lettres[] = new JButton[26];
	private String valeurLettres[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	//Variables score
	private JPanel gauche = new JPanel();
	private int mots = 0; //nb de mots
	private JLabel affichageMots = new JLabel ("Nombre de mots trouvés : "+mots, JLabel.CENTER);
	private int scorePartie = 0;//score de la partie en cours
	private int scoreMot = 0;//score du mot en cours
	private JLabel affichageScore = new JLabel ("Votre score actuel est de : "+scorePartie, JLabel.CENTER);
	//Variables image pendu	
	private JLabel imageLabel = new JLabel ();
	private ImageIcon imagePendu[] = new ImageIcon[8];  
	//Variables Jeu
	private String motAtrouver = "RETEST";
	private char lettresAtrouver[];
	private char lettresVisibles[];	
	private String motVisible = "";
	private JLabel affichageMotJeu = new JLabel (motVisible, JLabel.CENTER);
	private String lettreClic;
	private boolean erreur = false;
	private int nbErreurs = 0;
	BufferedReader lecteurMot;
	private char lettreAccent[] = {'À','Á','Ä','Â','Ç','È','É','Ê','Ë','Ì','Í','Î','Ï','Ò','Ó','Ô','Ö','Ù','Ú','Û','Ü','Ý'};
	private char lettreSansAccent[] = {'A','A','A','A','C','E','E','E','E','I','I','I','I','O','O','O','O','U','U','U','U','Y'};
	//Variables changement d'écran
	private boolean retourAccueil = false;
	Observateur obs;
	
	//Constructeur
	public EcranJeu () {
		//Initialisation du mot à trouver
		initMot();
		//Affichage droite
		initImage();
		imageLabel = new JLabel(imagePendu[nbErreurs]);
		imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
		//Affichage gauche
		initClavier();	
		gauche.setLayout(new GridLayout(4,1));
		gauche.add(affichageMots);
		gauche.add(affichageScore);
		affichageMotJeu.setFont(new Font("Comic sans MS", Font.BOLD, 25));
		affichageMotJeu.setForeground(Color.BLUE);
		gauche.add(affichageMotJeu);
		gauche.add(clavierPanel);
		gauche.setSize(new Dimension (imagePendu[0].getIconWidth(),imagePendu[0].getIconHeight()));
		//Affichage général
		Box horiz = Box.createHorizontalBox();
		horiz.add(gauche);
		horiz.add(imageLabel);
		add(horiz);
	}
	
	//Méthode pour construire le clavier
	public void initClavier () {
		clavierPanel.setPreferredSize(new Dimension (400,120));
		for (int i=0 ; i<lettres.length ; i++) {
			lettres[i] = new JButton (valeurLettres[i]);
			lettres[i].setPreferredSize(new Dimension(50,25));
			lettres[i].addActionListener(new actionTouche ());
			clavierPanel.add(lettres[i]);
		}
		/* PAS NECESSAIRE - Suffit d'imposer la taille de clavierPanel et l'arrangement est automatique
		clavierPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints ();
		gbc.insets = new Insets(2,2,2,2);
		for (int i=0 ; i<=6 ; i++) {
			gbc.gridx = i;
			gbc.gridy = 0;
			clavierPanel.add(lettres[i], gbc);
		}
		for (int i=7 ; i<=13 ; i++) {
			gbc.gridx = i-7;
			gbc.gridy = 1;
			clavierPanel.add(lettres[i], gbc);
		}
		for (int i=14 ; i<=20 ; i++) {
			gbc.gridx = i-14;
			gbc.gridy = 2;
			clavierPanel.add(lettres[i], gbc);
		}
		for (int i=21 ; i<=25 ; i++) {
			gbc.gridx = i-20;
			gbc.gridy = 3;
			clavierPanel.add(lettres[i], gbc);
		}	*/	
	}
	
	//Méthode pour initialiser le tableau d'images
	public void initImage () {
		for (int i=0 ; i<8 ; i++) {
			imagePendu[i] = new ImageIcon ("pendu"+i+".jpg");
		}
	}
	
	//Méthode pour initialiser le mot à trouver
	public void initMot () {
		try {
			lecteurMot = new BufferedReader (new FileReader ("dictionnaire.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		LineNumberReader lnr = new LineNumberReader (lecteurMot);
		int ligne = (int) (Math.random()*336529);
		try {
			for (int i=0 ; i<ligne ; i++) lnr.readLine();
			motAtrouver = lnr.readLine().toUpperCase();
			lecteurMot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lettresAtrouver = motAtrouver.toCharArray();
		for (int i=0 ; i<lettreAccent.length ; i++) {
			motAtrouver = motAtrouver.replace(lettreAccent[i], lettreSansAccent[i]);
		}
		lettresVisibles = new char[lettresAtrouver.length];
		for (int i=0 ; i<motAtrouver.length() ; i++) {
			lettresVisibles[i] = '*';		
		}
		motVisible = new String (lettresVisibles);
		affichageMotJeu.setText(motVisible);
	}
	
	//Méthode pour réinitialiser en partie l'écran (partie perdue et mot complet)
	public void reinit () {
		initMot();
		nbErreurs = 0;
		imageLabel.setIcon(imagePendu[nbErreurs]);
		for (int i=0 ; i<lettres.length ; i++) {
			lettres[i].setEnabled(true);;
		}
	}
	
	//Méthode pour le jeu
	public void deroulementJeu (String lettreClic) {
		if (motAtrouver.contains(lettreClic)) erreur = false;
		else erreur = true;
		//si la lettre est fausse
		if (erreur == true && nbErreurs<7) {
			nbErreurs ++;
			imageLabel.setIcon(imagePendu[nbErreurs]);
		}
		//si la lettre est fausse et le max d'erreurs atteint
		if (erreur == true && nbErreurs>=7) {
			String phrasePerdu = "Vous avez perdu! Le mot était "+motAtrouver+".";
			JOptionPane.showConfirmDialog(null, phrasePerdu, "", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
			retourAccueil = true;
			updateObservateur();
			retourAccueil = false;
			reinit();
			mots = 0; 
			affichageMots.setText("Nombre de mots trouvés : "+mots);
			scorePartie = 0;
			scoreMot = 0;
			affichageScore.setText("Votre score actuel est de : "+scorePartie);
		}
		//si la lettre est bonne
		if (erreur == false) {
			for (int i=0 ; i<lettresAtrouver.length ; i++) {
				for (int j=0 ; j<lettreAccent.length ; j++) {
					if ((lettreClic.charAt(0) == lettresAtrouver[i])||(lettreClic.charAt(0)== lettreSansAccent[j] && lettresAtrouver[i]==lettreAccent[j])) {
						lettresVisibles[i] = lettresAtrouver[i];
						motVisible = new String (lettresVisibles);
						affichageMotJeu.setText(motVisible);
					}
				}
			}
		}
		//si le mot est complet
		if (!motVisible.contains("*")) {
			calculScore();
			String phraseGagne = "Bravo, vous avez gagné! Vous marquez "+scoreMot+" pts.\n Cliquez sur OK pour le mot suivant";
			JOptionPane.showConfirmDialog(null, phraseGagne, "", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
			reinit();			
		}
	}
	
	//Méthode pour le calcul du Score
	public void calculScore () {
		if (nbErreurs == 0) scoreMot = 100;
		if (nbErreurs == 1) scoreMot = 50;
		if (nbErreurs == 2) scoreMot = 35;
		if (nbErreurs == 3) scoreMot = 25;
		if (nbErreurs == 4) scoreMot = 15;
		if (nbErreurs == 5) scoreMot = 10;
		if (nbErreurs == 6) scoreMot = 5;
		scorePartie += scoreMot;
		affichageScore.setText("Votre score actuel est de : "+scorePartie);
		mots ++;
		affichageMots.setText("Nombre de mots trouvés : "+mots);
	}
	
	//Méthode pour récupérer le score de la partie finie
	public int getScorePartie () {
		return scorePartie;
	}
	
	//Méthode pour récupérer le nombre de mots de la partie finie
	public int getMots () {
		return this.mots;
	}
	
	//Classe interne pour les actions des boutons clavier
 	class actionTouche implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			((JButton)e.getSource()).setEnabled(false);
			lettreClic = e.getActionCommand();
			deroulementJeu(lettreClic);
		}		
	}
 	
 	//Méthode implémentée de l'interface Observable
	public void addObservateur(Observateur obs) {
		this.obs = obs;		
	}
	
	//Méthode implémentée de l'interface Observable
	public void updateObservateur() {
		obs.update(this.retourAccueil);		
	}

}
