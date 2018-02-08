import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * Class for profiles
 * @author LukeStacey
 *
 */
public class Profile {
	
	Profile left;
	Profile right;
	List<Sequence> alignments;
	/**
	 * Default Constructor
	 */
	public Profile() {
		this.alignments = new ArrayList<Sequence>();
	}
	/**
	 * Constructor
	 * @param seq sequence to go in alignments
	 */
	public Profile(Sequence seq) {
		this();
		this.alignments.add(seq);
	}
		
	/**
	 * Constructor
	 * @param p1 left child
	 * @param p2 right child
	 * @param alignments list of sequences
	 */
	public Profile(Profile p1, Profile p2, List<Sequence> alignments) {
		this.left = p1;
		this.right = p2;
		this.alignments = alignments;
	}
	/**
	 * getter for sequences
	 * @return sequence list
	 */
	public List<Sequence> getSequences() {
		return alignments;
	}
	
	/**
	 * Converts this object into a string
	 */
	public String toString() {
		return Arrays.toString(this.alignments.toArray());
	}
	/**
	 * Reorders the sequences by their indexes
	 */
	public void sortSequences() {
		this.alignments.sort(new SequenceCompare());
	}
}
