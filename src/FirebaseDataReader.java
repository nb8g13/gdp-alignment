import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
	boolean done = false;
	
	public FirebaseDataReader(ConsensusAlgorithm ca) {
		this.ca = ca;
	}
	
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
				.getReference("firepads/" + transcriptKey + "/" + captionKey + "/history");
		
		class MutationListener implements ValueEventListener {

			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println(arg0.getMessage());
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				System.out.println("data change triggered");
				
				List<Mutation> mutations = new ArrayList<Mutation>();
				Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
				Caption cap = new Caption("", 0.1);
				
				while(iter.hasNext()) {
					DataSnapshot childSnapshot = iter.next();
					mutations.add(createMutation(childSnapshot));
				}
				
				String winner = ca.getConsensus(mutations);
				System.out.println(winner);
				
				done = true;
				
				//Not testable yet...
				//Mutation mutation = ca.getConsensus(mutations);
				
				
				/*
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
				*/
				
			}
			
			public Mutation createMutation(DataSnapshot snapshot) {
				DataSnapshot o = snapshot.child("o");
				
				List<Operation> operations = new ArrayList<Operation>();
				
				Iterator<DataSnapshot> fbops = o.getChildren().iterator();
				
				while(fbops.hasNext()) {
					DataSnapshot opSnap = fbops.next();
					Object val = opSnap.getValue();
					
					if(val instanceof Long) {
						int valNum = ((Long) val).intValue();
						
						if (valNum < 0) {
							operations.add(new DeleteOperation(valNum*-1));
						}
						
						else {
							operations.add(new CursorOperation(valNum));
						}
					}
					
					else {
						String insertion = val.toString();
						operations.add(new InsertOperation(insertion));
					}
				}
				
				Edit edit = new Edit(operations);
				System.out.println(edit);
				
				String author = snapshot.child("a").getValue().toString();
				long time = (long) snapshot.child("t").getValue();
				
				Mutation mut = new Mutation(edit, author, time);
				
				System.out.println(mut);
				
				return mut;
			}
		}
		
		MutationListener ml = new MutationListener();
		
		ref.addListenerForSingleValueEvent(ml);
		System.out.println("listener added");
	}
}
