
public class CaptionTest {
	public static void main(String[] args) {
		Caption cap = new Caption("letting hello worl participants or employees within those firms", 1.0);
		
		//System.out.println(cap.capsNoPunctuation());
		System.out.println(cap.getText().substring(0,18));
		System.out.println(cap.getText().substring(18, 18+45));
	}
}
