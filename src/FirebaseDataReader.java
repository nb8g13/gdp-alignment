import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Result;


//Main control class for reading in mutations for a caption, reading in the mutations, performing the alignment
//and then updating the snapshot
public class FirebaseDataReader {
	
	ConsensusAlgorithm ca;
	
	public void update(String transcriptKey, String captionKey) {
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/transcripts/" + transcriptKey +"/" + captionKey + "/unlocked");
		
		
		ref.setValueAsync("false", new DatabaseReference.CompletionListener() {

			@Override
			public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
				getMutationList(transcriptKey, captionKey);
			}
		});
	}
	
	public void getMutationList(String transcriptKey, String captionKey) {
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/firepads/" + transcriptKey + "/" + captionKey + "/history");
		
		
		class MutationListener implements ValueEventListener {

			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println(arg0.getMessage());
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				
				GenericTypeIndicator<List<Mutation>> gti = new GenericTypeIndicator<List<Mutation>>(){};
				List<Mutation> mutations = snapshot.getValue(gti);
				Mutation mutation = ca.getConsensus(mutations);
				
				//Work out what label to give to mutation
				Long newTotal = snapshot.getChildrenCount() + 1;
				String newKey = Long.toHexString(newTotal);
				
				DatabaseReference cref = ref.child(newKey);
				
				cref.setValueAsync(mutation, new DatabaseReference.CompletionListener()  {
					
					@Override
					public void onComplete(DatabaseError err, DatabaseReference snapshot) {
						DatabaseReference lockref = FirebaseDatabase
							.getInstance()
							.getReference("/transcripts/" + transcriptKey +"/" + captionKey + "/unlocked");
						
						lockref.setValueAsync("true");
					}
				});
				
			}
		}
		
		MutationListener ml = new MutationListener();
		
		ref.addListenerForSingleValueEvent(ml);
	}
}
