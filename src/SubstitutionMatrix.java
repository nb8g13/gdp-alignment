import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
/**
 * Class for containing the substitution matrix
 * @author LukeStacey
 *
 */
public class SubstitutionMatrix {
	
	Map<Character, Integer> indexMap = new HashMap<Character, Integer>();
	double[][] subMatrix;
	/**
	 * Constructor
	 * @param str alphabet with length n
	 * @param subMatrix nxn 2d array subs matrix
	 */
	public SubstitutionMatrix(String str, double[][] subMatrix) {
		this.populateIndexMap(str);
		this.subMatrix = subMatrix;
		//this.delete = del;
	}
	
	//TODO: Change encoding to UTF-8
	// Method for reading substitution matrix from two configuration files, one containing the alphabet
	// and the other containing the substitution matrix itself 
	/**
	 * Constructor. Imports an alphabet and substitution matrix
	 * @param alphabetFile alpabet file's path
	 * @param matrixFile path of matrix to import
	 * @throws IOException if path goes nowhere
	 */
	public SubstitutionMatrix(String alphabetFile, String matrixFile) throws IOException {
		Path apath = Paths.get(alphabetFile);
		
		//Change to UTF
		Charset charset = Charset.forName("CP1252");
		
		// Maybe change this to something more obvious / maintainable for large alphabets
		List<String> strings = Files.readAllLines(apath, charset);
		this.populateIndexMap(strings.get(0));
		
		this.subMatrix = new double[strings.get(0).length()][strings.get(0).length()];
		
		Path mpath = Paths.get(matrixFile);
		
		CSVReader reader = new CSVReader(new FileReader(mpath.toFile()));
		List<String[]> lines = reader.readAll();
		Iterator<String[]> iter = lines.iterator();
		
		int i = 0;
		
		// May throw an index out of bounds error if matrix dimensions do not match up with alphabet size
		//TODO: Error check index out of bounds on sub matrix read
		while(iter.hasNext()) {
			String[] line = iter.next();
			
			for(int j = 0; j < strings.get(0).length(); j++) {
				this.subMatrix[i][j] = Double.parseDouble(line[j]);
			}
			
			i++;
		}
		
		reader.close();
	}
	/**
	 * builds the matrix's index map from a given string
	 * @param str the characters to put in the index map
	 */
	private void populateIndexMap(String str) {
		
		for (int i=0; i < str.length(); i++) {
			char curr = str.charAt(i);
			indexMap.put(curr, i);
		}
	}
	/**
	 * Returns sub score for 2 characters using the chars
	 * @param i first character
	 * @param j second character
	 * @return substitution cost for those chars
	 */
	public double getSubScore(char i, char j) {
		return this.subMatrix[charToIndex(i)][charToIndex(j)];
	}
	/**
	 * Returns sub score for 2 characters using the chars' IDs
	 * @param i first char's ID
	 * @param j second char's ID
	 * @return substitution cost for those chars
	 */
	public double getSubScore(int i, int j) {
		return this.subMatrix[i][j];
	}
	
	/**
	 * Takes a char and gets its position in the alphabet
	 * @param c char to find index of
	 * @return index of char c
	 */
	public int charToIndex(char c) {
		return indexMap.get(c);
	}
	/**
	 * getter for size of alphabet
	 * @return number of chars in alphabet
	 */
	public int alphabetSize() {
		return indexMap.size();
	}
	/**
	 * Adds the extension cost to all values in the subs matrix
	 * @param ext extension cost
	 */
	public void adjustByExtension(double ext) {
		for (int i = 0; i < this.subMatrix.length; i++) {
			for (int j = 0; j < this.subMatrix[0].length; j++) {
				this.subMatrix[i][j] += 2*ext;
			}
		}
	}
	/**
	 * Static constructor, builds a levenshtein distance subs matrix for any given alphabet
	 * @param alphabet alphabet for subs matrix
	 * @return substitution matrix with all values -1 apart from diagonal which is 0
	 */
	public static SubstitutionMatrix LEVENSHTEIN(String alphabet) {
		double[][] mat = new double[alphabet.length()][alphabet.length()];
		
		for(int i = 0; i < alphabet.length(); i++) {
			for (int j = 0; j < alphabet.length(); j++) {
				if(i == j) {
					mat[i][j] = 0.0;
				}
				
				else {
					mat[i][j] = -1.0;
				}
			}
		}
		
		return new SubstitutionMatrix(alphabet, mat);
	}
	
}
