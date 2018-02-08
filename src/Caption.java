/**
 * Caption class for storing text entries before they're ready for alignment.
 * @author LukeStacey
 *
 */
public class Caption {
	String text;
	double reputation;
	int pos = 0;
	long t = 0;
	/**
	 * Constructor, pos and t set to 0 by deafault
	 * @param original the caption itself in a string
	 * @param reputation the reputation of the person who made the caption
	 */
	public Caption(String original, double reputation) {
		this.text = original;
		this.reputation = reputation;
	}
	/**
	 * Constructor, pos set to 0 by default
	 * @param original the caption itself in a string
	 * @param reputation the reputation of the person who made the caption
	 * @param t the time the caption was given at
	 */
	public Caption(String original, double reputation, long t) {
		this(original, reputation);
		this.t = t;
	}
	/**
	 * Changes the text in the caption to be of the form accepted by aligners. No punctuation and all caps.
	 * @return The string with all the punctuation removed and made allcaps
	 */
	public String capsNoPunctuation() {
		String upper = text.toUpperCase();
		//Replace all sequences of whitespace with a single space
		upper = upper.replaceAll("\\s+", " ");
		upper = upper.replaceAll("[^A-Z0-9\\s]", "");
		return upper;
	}
	/**
	 * getter for text
	 * @return text
	 */
	public String getText() {
		return text;
	}
	/**
	 * setter for text
	 * @param str new value of text
	 */
	public void setText(String str) {
		this.text = str;
	}
	/**
	 * getter for reputation
	 * @return reputation
	 */
	public double getReputation() {
		return reputation;
	}
	/**
	 * setter for reputation
	 * @param reputation new value for reputation
	 */
	public void setReputation(double reputation) {
		this.reputation = reputation;
	}
	/**
	 * getter for position
	 * @return position
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * setter for position
	 * @param pos new value for position
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	/**
	 * getter for time
	 * @return time
	 */
	public long getT() {
		return t;
	}
	/**
	 * setter for time
	 * @param t new value for time
	 */
	public void setT(long t) {
		this.t = t;
	}
}
