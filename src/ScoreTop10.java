
public class ScoreTop10 implements Comparable<ScoreTop10>{
	
	private String name ="";
	private int pts;
	private int nbMots;
	
	public ScoreTop10 (String name, int pts, int nbMots) {
		this.name = name;
		this.pts = pts;
		this.nbMots = nbMots;				
	}
	
		
	//Méthodes getters pour récupérer les variables
	public String getName () {
		return this.name;
	}
	public int getPts () {
		return this.pts;
	}
	public int getNbMots () {
		return this.nbMots;
	}
	
	//Méthodes setters pour modifier les variables
	public void setName (String name) {
		this.name = name;
	}
	public void setPts (int score) {
		this.pts = score;
	}
	public void setNbMots (int nbMots) {
		this.nbMots = nbMots;
	}
	
	//Méthode implémentée de l'interface Comparable
	public int compareTo(ScoreTop10 compareTop10) {
		int compareScore = compareTop10.getPts();
		return compareScore - this.pts;
	}
	

}
