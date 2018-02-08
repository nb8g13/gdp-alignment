/**
 * Class for storing sequences
 * @author LukeStacey
 *
 */
public class Sequence {
	
	String seq;
	int index;
	double rep = 0.0;
	/**
	 * Constructor
	 * @param seq the string it stores
	 */
	public Sequence(String seq) {
		this.seq = seq;
		index = -1;
	}
	/**
	 * Constructor
	 * @param seq string in the sequence
	 * @param index sequence index for sorting
	 */
	public Sequence(String seq,int index) {
		this.seq = seq;
		this.index=index;
	}
	/**
	 * Constructor
	 * @param seq string in the sequence
	 * @param index sequence index for sorting
	 * @param rep reputation for weighting
	 */
	public Sequence(String seq,int index,double rep) {
		this.seq = seq;
		this.index=index;
		this.rep=rep;
	}
	/**
	 * getter for the sequence
	 * @return string
	 */
	public String getSeq() {
		return seq;
	}
	/**
	 * Prepends a character to the sequence
	 * @param c character to be put on front of sequence
	 */
	public void prependSeq(char c) {
		this.seq = c + seq;
	}
	/**
	 * Converts this object to a string
	 */
	public String toString() {
		return this.getSeq();
	}
	/**
	 * getter for index
	 * @return index
	 */
	public int getIndex() {
		return this.index;
	}
	/** getter for reputation
	 * 
	 * @return reputation
	 */
	public double getRep() {
		return rep;
	}
}
