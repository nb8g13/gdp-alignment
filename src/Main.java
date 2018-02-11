
public class Main {
	public static void main(String[] args) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
		Middleware middleware = new Middleware(alphabet, SubstitutionMatrix.LEVENSHTEIN(alphabet));
		middleware.updateBackend();
	}
}
