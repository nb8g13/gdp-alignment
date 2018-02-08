import java.util.Comparator;
import java.util.List;

//TODO: Rewrite so only the upper half of the matrix is stored
// This will require changes to most for loops and removeRow/cleanDM
/**
 * A class for storing the similarities between profiles
 * @author LukeStacey
 *
 * @param <T> Means it can be compared and sorted etc
 */
public class DistanceMatrix<T extends Comparable<T>> {
	
	List<List<T>> matrix;
	T selfValue;
	/**
	 * Constructor
	 * @param matrix the matrix of distances
	 */
	public DistanceMatrix(List<List<T>> matrix) {
		this.matrix = matrix;
	}
	/**
	 * Constructor that also specifies the selfValue for comparator
	 * @param matrix the matrix of distances
	 * @param selfValue value of comparing things to themselves
	 */
	public DistanceMatrix(List<List<T>> matrix, T selfValue) {
		this(matrix);
		this.selfValue = selfValue;
		cleanDM();
	}
	/**
	 * set the diagonal to selfValue
	 */
	private void cleanDM() {
		for (int i = 0; i < this.matrix.size(); i++) {
			this.matrix.get(i).set(i, this.selfValue);
		}
	}
	/**
	 * getter for the matrix
	 * @return matrix
	 */
	public List<List<T>> getMatrix() {
		return this.matrix;
	}
	
	/**
	 * Gets the position of the most similar two strings from the matrix
	 * @return
	 */
	public Value findMax() {
		Value mv = new Value(1,1, matrix.get(0).get(0));
		
		for (int i = 0; i < matrix.size(); i++) {
			for(int j = i; j < matrix.size(); j++) {
				if (mv.getValue().compareTo(matrix.get(i).get(j)) < 0) {
					mv = new Value(i, j, matrix.get(i).get(j));
				}
			}
		}
		
		return mv;
	}
	/**
	 * Removes the ith row and column from the matrix
	 * @param i the row/column to be removed, 0 indexed
	 */
	public void removeIndex(int i) {
		//System.out.println(this);
		matrix.remove(i);
		for (int j = 0; j < matrix.size(); j++) {
			List<T> row = matrix.get(j);
			row.remove(i);
		}
	}
	/**
	 * Attaches a row to the top and left of the distanceMatrix
	 * @param row the new row to be added
	 */
	public void addElement(List<T> row) {
		if (row.size() == this.matrix.size()) {
			row.add(0, this.selfValue);
		}
		
		for(int i = 0; i < this.matrix.size(); i++) {
			List<T> current = matrix.get(i);
			current.add(0, row.get(i+1));
		}
		
		this.matrix.add(0, row);
	}
	
	/**
	 * Converts the object to a string for printing
	 */
	public String toString() {
		String str = "";
		
		for (int i = 0; i < this.matrix.size(); i++) {
			List<T> row = this.matrix.get(i);
			for (int j = 0; j < row.size(); j++) {
				str = str + ", " + row.get(j);
			}
			
			str = str + "\n";
		}
		
		return str;
	}
	/**
	 * Data type for storing an element in the distancematrix
	 * @author LukeStacey
	 *
	 */
	class Value implements Comparator<Value> {
		int row;
		int col;
		T value;
		/**
		 * Constructor
		 * @param row the row of the item
		 * @param col the column of the item
		 * @param value the value in that index of the matrix
		 */
		public Value(int row, int col, T value) {
			this.row = row;
			this.col = col;
			this.value = value;
		}
		/**
		 * getter for row
		 * @return row
		 */
		public int getRow() {
			return row;
		}
		/**
		 * getter for column
		 * @return column
		 */
		public int getCol() {
			return col;
		}
		/**
		 * getter for value
		 * @return value
		 */
		public T getValue() {
			return value;
		}
		
		/**
		 * Makes the class comparable with their values so they can be sorted returns -1 if less than etc
		 * @param arg0 the first value for comparing
		 * @param arg1 the second value for comparing
		 * @return -1 if arg0<arg1 1 if arg1<arg0 and 0 if their the same
		 */
		public int compare(Value arg0, Value arg1) {
			
			int compVal = arg0.getValue().compareTo(arg1.getValue());
			return compVal;
		}
		/**
		 * Checks if this is the same as the given object
		 * @param o the object to compare this with
		 * @return true if they're the same, false otherwise
		 */
		public boolean equals(Object o) {
			
			if (this == o) {
				return true;
			}
			
			if (!(o instanceof DistanceMatrix.Value)) {
				return false;
			}
			
			@SuppressWarnings("unchecked")
			DistanceMatrix<T>.Value mv = (DistanceMatrix<T>.Value) o;
			
			return mv.compare(mv, this) == 0;
		}
	}
}
