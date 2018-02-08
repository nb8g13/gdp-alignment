
public class CapsNoPunctuation implements CaptionCleaner {
	@Override
	public String clean(Caption cap) {
		String upper = cap.getText().toUpperCase();
		//Replace all sequences of whitespace with a single space
		upper = upper.replaceAll("\\s+", " ");
		upper = upper.replaceAll("[^A-Z0-9\\s]", "");
		return upper;
	}
}
