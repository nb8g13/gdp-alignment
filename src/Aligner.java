
public interface Aligner {
	/**
	 * Interface for different alignment algorithms
	 * @param str array of strings to be aligned
	 * @param reps array of the strings' respective reputations
	 * @return The list of aligned strings, order is preserved
	 */
	public String[] align(String[] str, double[] reps);
	
}
