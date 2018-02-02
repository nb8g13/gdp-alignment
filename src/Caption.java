
public class Caption {
	String text;
	double reputation;
	int pos = 0;
	
	public Caption(String original, double reputation) {
		this.text = original;
		this.reputation = reputation;
	}
	
	public String capsNoPunctuation() {
		String upper = text.toUpperCase();
		//Replace all sequences of whitespace with a single space
		upper = upper.replaceAll("\\s+", " ");
		upper = upper.replaceAll("[^A-Z0-9\\s]", "");
		return upper;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String str) {
		this.text = str;
	}
	
	public double getReputation() {
		return reputation;
	}
	
	public void setReputation(double reputation) {
		this.reputation = reputation;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
}
