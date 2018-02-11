import java.util.List;

public class MutationReadingTest {
	public static void main(String[] args) {
		MutationReader mr = new MutationReader();
		List<Mutation> mutations = mr.getMutations("tmp//history.json");
		
		for(int i = 0; i < mutations.size(); i++) {
			System.out.println(mutations.get(i));
			System.out.println(mutations.get(i).getO());
		}
	}
}
