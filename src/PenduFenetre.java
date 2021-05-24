import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class PenduFenetre extends JFrame implements ActionListener, Observateur, Observable {
	
	//Instances des écrans et Variables
	private EcranJeu ecranJeu = new EcranJeu();
	private EcranScores ecranScores = new EcranScores();
	private EcranAccueil ecranAccueil = new EcranAccueil();
	private EcranRegles ecranRegles = new EcranRegles();
	private String choixEcran = "Accueil";
	private int scorePartie;
	private int motsPartie;
	private int scoreMin;
	private boolean nouveauScore = false;
	Observateur obs;
	
	//Variables Barre de Menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier"),
			apropos = new JMenu("À Propos");
	private JMenuItem nouveau = new JMenuItem("Nouveau"),
			scores = new JMenuItem("Scores"),
			accueil = new JMenuItem("Accueil"),
			regles = new JMenuItem("Règles");	
	
	//Constructeur
	public PenduFenetre () {
		this.setSize(1000, 800);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initMenu();
		this.setContentPane(ecranAccueil);
		this.setVisible(true);
		ecranJeu.addObservateur(this);
		this.addObservateur(ecranScores);
	}
	
	//Initialisation du Menu
	private void initMenu() {
		nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		nouveau.addActionListener(this);
		fichier.add(nouveau);
		scores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
		scores.addActionListener(this);
		fichier.add(scores);
		accueil.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_DOWN_MASK));
		accueil.addActionListener(this);
		fichier.add(accueil);
		regles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_DOWN_MASK));
		regles.addActionListener(this);
		apropos.add(regles);
		menuBar.add(fichier);
		menuBar.add(apropos);
		this.setJMenuBar(menuBar);		
	}
	
	//Méthode pour les actions du Menu
	public void actionPerformed(ActionEvent e) {
		choixEcran = e.getActionCommand();
		if (choixEcran.equals("Nouveau")) this.setContentPane(ecranJeu);
		else if (choixEcran.equals("Scores")) this.setContentPane(ecranScores);
		else if (choixEcran.equals("Règles")) this.setContentPane(ecranRegles);
		else this.setContentPane(ecranAccueil);
		revalidate();
	}

	//Méthode implémentée de l'interface Observateur
	public void update(boolean retourAccueil) {
		scorePartie = ecranJeu.getScorePartie();
		motsPartie = ecranJeu.getMots();
		scoreMin = ecranScores.getScoreMin();
		if (scorePartie > scoreMin) {
			nouveauScore = true;
			ecranScores.setScorePartie(scorePartie);
			ecranScores.setMotsPartie(motsPartie);
			this.setContentPane(ecranScores);
			updateObservateur();
		} else this.setContentPane(ecranAccueil);
		revalidate();
	}

	//Méthode implémentée de l'interface Observable
	public void addObservateur(Observateur obs) {
		this.obs = obs;		
	}

	//Méthode implémentée de l'interface Observable
	public void updateObservateur() {
		obs.update(this.nouveauScore);		
	}

}
