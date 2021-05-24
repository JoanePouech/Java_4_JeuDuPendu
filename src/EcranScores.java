import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EcranScores extends JPanel implements Observateur {
	
	//Variables Gestion des scores
	private int scoreMin;
	private int scorePartie;
	private int motsPartie;
	private String nouveauNom = "Toto";
	private ScoreTop10 tableauScores[] = new ScoreTop10[10];
	private File fichierScores = new File ("scores.txt");
	//Variables Affichage 
	private JPanel top10Panel = new JPanel();
	private JLabel affichageScores[] = new JLabel[10];
	private int taillePolice;
	private JLabel affichageImage = new JLabel (new ImageIcon("pendu7.jpg"));
	private JPanel scores = new JPanel();
	
	//Constructeur
	public EcranScores () {
		initScores();
		sortScores();
		top10Panel.setLayout(new GridLayout(10,1,50,30));
		for (int i=0 ; i<affichageScores.length ; i++) {
			top10Panel.add(affichageScores[i]);
		}
		this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		this.setLayout(new BorderLayout());
		this.add(top10Panel, BorderLayout.WEST);
		this.add(affichageImage, BorderLayout.EAST);
	}
	
	//Méthode pour initialiser le tableau de scores
	public void initScores() {
		if (fichierScores.exists()) {
			try {
				Scanner lectureScores = new Scanner (new File("scores.txt"));
				for (int i=0 ; i<tableauScores.length ; i++) {
					tableauScores[i] = new ScoreTop10 (lectureScores.next(),lectureScores.nextInt(),lectureScores.nextInt());
					affichageScores[i] = new JLabel();
				}		
				lectureScores.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			String initNom = "....";
			int initPts = 0;
			int initMots = 0;
			for (int i=0 ; i<tableauScores.length ; i++) {
				tableauScores[i] = new ScoreTop10 (initNom, initPts, initMots);
				affichageScores[i] = new JLabel();
			}
		}
	}
	
	//Methode pour classer et mettre à jour les scores
	public void sortScores () {
		Arrays.sort(tableauScores);
		taillePolice = 30;		
		for (int i=0 ; i<tableauScores.length ; i++) {
			affichageScores[i].setFont(new Font("Comis sans MS",Font.PLAIN,taillePolice));
			if (tableauScores[i].getNbMots()<= 1) affichageScores[i].setText(tableauScores[i].getName()+" -> "+tableauScores[i].getPts()+" Pts ("+tableauScores[i].getNbMots()+" mot)");
			else affichageScores[i].setText(tableauScores[i].getName()+" -> "+tableauScores[i].getPts()+" Pts ("+tableauScores[i].getNbMots()+" mots)");
			taillePolice -= 2;
		}
		scoreMin = tableauScores[9].getPts();
	}
	
	//Méthode pour écrire les scores dans un fichier
	public void ecritScores () {
		try {
			PrintStream ecritureScores = new PrintStream ("scores.txt");
			for (int i=0 ; i<tableauScores.length ; i++) {
				ecritureScores.println(tableauScores[i].getName()+"\t"+tableauScores[i].getPts()+"\t"+tableauScores[i].getNbMots());
			}
			ecritureScores.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}	
	
	//Méthode pour lire les scores dans le fichier
	public void lectScores () {
		try {
			Scanner lectureScores = new Scanner (new File("scores.txt"));
			for (int i=0 ; i<tableauScores.length ; i++) {
				tableauScores[i].setName(lectureScores.next());
				tableauScores[i].setPts(lectureScores.nextInt());
				tableauScores[i].setNbMots(lectureScores.nextInt());
			}		
			lectureScores.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Méthode pour récupérer le score minimum du tableau
	public int getScoreMin () {
		return scoreMin;
	}
	
	//Méthode pour mettre à jour le score de la partie finie
	public void setScorePartie (int scorePartie) {
		this.scorePartie = scorePartie;
	}

	//Méthode pour mettre à jour le nombre de mots de la partie finie
	public void setMotsPartie(int motsPartie) {
		this.motsPartie = motsPartie;		
	}

	//Méthode implémentée de l'interface Observateur, pour ajouter un nouveau score
	public void update(boolean nouveauScore) {
		if (scorePartie > scoreMin) {
			lectScores();
			nouveauNom = JOptionPane.showInputDialog(null, "Quel est votre nom?", "Nouveau Meilleur Score!!!", JOptionPane.PLAIN_MESSAGE);
			tableauScores[9].setName(nouveauNom);
			tableauScores[9].setPts(scorePartie);
			tableauScores[9].setNbMots(motsPartie);
			sortScores();
			ecritScores();
		}		
	}
	

}
