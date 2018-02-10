import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Result;

//Confused not finished captions

/**
 * Main control class for reading in mutations for a caption, reading in the mutations, performing the alignment
 * and then updating the snapshot
 * @author LukeStacey
 *
 */
public class FirebaseDataReader {
	
	ConsensusAlgorithm ca;
	boolean done = false;
	/**
	 * Consturctor
	 * @param ca Consensus algorithm to be used
	 */
	public FirebaseDataReader(ConsensusAlgorithm ca) {
		this.ca = ca;
	}
	
	public void update(String transcriptKey, String captionKey) {
	
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/transcripts/" + transcriptKey + "/lock");
		
		ApiFuture<Void> promises = ref.setValueAsync(true);
		
		try {
			promises.get();
			waitForUsers(transcriptKey);
			System.out.println("Done waiting for users");
			String chkpt = getChkpt(transcriptKey, captionKey);
			System.out.println("Got the checkpoint");
			ApiFuture<List<Void>> asyncs = getMutationList(transcriptKey, captionKey, chkpt);
			asyncs.get();
			System.out.println("Worked out the best mutation");
			ApiFuture<Void> unlock = ref.setValueAsync(false);
			unlock.get();
			System.out.println("Took the lock off");
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public String getChkpt(String tkey, String ckey) {
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/captions/" + tkey +"/" + ckey + "/chkpt");
		
		class CHKPTGetter implements ValueEventListener {
			
			String chkpt;
			boolean done = false;
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println("Failed to retrieve check point");
				
			}

			@Override
			public void onDataChange(DataSnapshot snap) {
				chkpt = snap.getValue(String.class);
				this.done = true;
			}
			
			public boolean getDone() {
				return done;
			}
			
			public String getCHKPT(){
				return chkpt;
			}
		}
		
		CHKPTGetter cgetter = new CHKPTGetter();
		
		ref.addListenerForSingleValueEvent(cgetter);
		
		while(!cgetter.getDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cgetter.getCHKPT();
	}
	
	public void waitForUsers(String transcriptKey) {
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/transcripts/" + transcriptKey + "/users");
		
		
		class Waiter implements ValueEventListener {

			boolean empty = false;
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println("Error waiting for users to leave");
				
			}

			@Override
			public void onDataChange(DataSnapshot snap) {
				if(snap.getChildrenCount() == 0.0) {
					empty = true;
				}
			}
			
			public boolean isDone() {
				return empty;
			}
		}
		
		Waiter waiter = new Waiter();
		
		ref.addValueEventListener(waiter);
		
		while(!waiter.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ApiFuture<List<Void>> getMutationList(String transcriptKey, String captionKey, String chkpt) {
		
		System.out.println("checkpoint: " + chkpt);
		
		Query ref = FirebaseDatabase
				.getInstance()
				.getReference("firepads/" + transcriptKey + "/" + captionKey + "/history").orderByKey().startAt(chkpt);
		
		ref.keepSynced(true);
		
		class MutationListener implements ValueEventListener {
			
			ApiFuture<List<Void>> asyncs;
			boolean done = false;
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println(arg0.getMessage());
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				
				List<Mutation> mutations = new ArrayList<Mutation>();
				Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
				
				while(iter.hasNext()) {
					DataSnapshot childSnapshot = iter.next();
					mutations.add(createMutation(childSnapshot));
				}
				
				System.out.println("Generating history");
				
				//ClosureTest ct = new ClosureTest();
				//ct.doStuff();
				
				ReputationAssigner ra = new AverageReputation();
				HistoryGenerator hg = new CaptionHistoryGenerator(ra);
				List<Caption> captions = hg.getHistory(mutations);
				
				/*
				System.out.println("Getting the winner");
				String winner = ca.getConsensus(mutations, captions);
				System.out.println(winner);
				
				System.out.println("Awarding authors");
				ReputationAward awarder = new LevenshteinReward();
				awarder.award(winner, mutations, captions);
				
				System.out.println("Creating new checkpoint");
				Integer chkptNum = Integer.parseInt(chkpt, 16);
				chkptNum += mutations.size();
				String newChkpt = Integer.toHexString(chkptNum);
				
				System.out.println("Starting writes");
				List<ApiFuture<Void>> promises = new ArrayList<ApiFuture<Void>>();
				
				DatabaseReference chkptRef = FirebaseDatabase
						.getInstance()
						.getReference(("/captions/" + transcriptKey + "/" + captionKey + "/chkpt"));
				
				ApiFuture<Void> chkptPromise = chkptRef.setValueAsync(newChkpt);
				promises.add(chkptPromise);
				
				DatabaseReference historyRef = FirebaseDatabase
						.getInstance()
						.getReference("/firepads/" + transcriptKey + "/" + captionKey + "/history/" + newChkpt);
				
				ApiFuture<Void> authorPromise = historyRef.child("a").setValueAsync("admin");
				promises.add(authorPromise);
				ApiFuture<Void> timePromise = historyRef.child("t").setValueAsync(ServerValue.TIMESTAMP);
				promises.add(timePromise);
				ApiFuture<Void> insertPromise = historyRef.child("o").child("0").setValueAsync(winner);
				promises.add(insertPromise);
				
				ApiFuture<List<Void>> allPromises = ApiFutures.allAsList(promises);
				this.asyncs = allPromises;
				
				done = true;
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
				//System.out.println(edit);
				
				String author = snapshot.child("a").getValue().toString();
				long time = (long) snapshot.child("t").getValue();
				
				Mutation mut = new Mutation(edit, author, time);
				
				//System.out.println(mut);
				
				return mut;
			}
			
			public ApiFuture<List<Void>> getAsyncs() {
				return this.asyncs;
			}
			
			public boolean isDone() {
				return done;
			}
		}
		
		MutationListener ml = new MutationListener();
		
		ref.addListenerForSingleValueEvent(ml);
		
		while(!ml.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ml.getAsyncs();
	}
}
