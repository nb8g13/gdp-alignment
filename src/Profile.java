import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Profile {
	
	Profile left;
	Profile right;
	List<Sequence> alignments;
	
	public Profile() {
		this.alignments = new ArrayList<Sequence>();
	}
	
	public Profile(Sequence seq) {
		this();
		this.alignments.add(seq);
	}
		
	
	public Profile(Profile p1, Profile p2, List<Sequence> alignments) {
		this.left = p1;
		this.right = p2;
		this.alignments = alignments;
	}
	
	public List<Sequence> getSequences() {
		return alignments;
	}
	
	//Not very efficient...
	public String toString() {
		return Arrays.toString(this.alignments.toArray());
	}
	
	public void sortSequences() {
		this.alignments.sort(new SequenceCompare());
	}
}
