import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * A caption filter that differentiates captions when new users take over
 * @author LukeStacey
 *
 */
public class FinishedCaptionFilter implements CaptionFilter {

	/**
	 * Main method of this class, turns a list of captions and mutations into a list of captions
	 * @param mutations all the new modifications to the caption
	 * @param captions The already stored past candidate edits
	 * @return candidate edits
	 */
	public List<Caption> filter(List<Mutation> mutations, List<Caption> captions) {
		Map<String, UserInfo> uinfos = this.generateUserInfo(mutations, captions);
		System.out.println("User info generated");
		Iterator<UserInfo> iter = uinfos.values().iterator();
		List<Caption> candidates = new ArrayList<Caption>();
		
		System.out.println("At iterators");
		while(iter.hasNext()) {
			UserInfo current = iter.next();
			System.out.println(" start time: " + current.getStart());
			System.out.println("end: " + current.getEnd());
			System.out.println(current.getFinalCap());
			System.out.println(current.getUUID());
			boolean uncontested = true;
			System.out.println("Getting second iterator");
			Iterator<UserInfo> iter2 = uinfos.values().iterator();
			while(iter2.hasNext()) {
				UserInfo contender = iter2.next();
				if (contender.getStart() < current.getEnd() && contender.getEnd() > current.getEnd()) {
					uncontested = false;
					break;
				}
			}
			
			if(uncontested) {
				candidates.add(current.getFinalCap());
			}
		}
		
		return candidates;
	}
	/**
	 * Creates a mapping from Strings to users
	 * @param mutations list of recent edits
	 * @param captions list of previous captions
	 * @return mapping from Strings to Users
	 */
	public Map<String, UserInfo> generateUserInfo(List<Mutation> mutations, List<Caption> captions) {
		Map<String, UserInfo> uinfos = new HashMap<String, UserInfo>();
		
		for(int i = 0; i < captions.size(); i++) {
			Mutation mut = mutations.get(i);
			Caption cap = captions.get(i);
			if(!uinfos.containsKey(mut.getA())) {
				UserInfo uinfo = new UserInfo(mut.getA());
				uinfo.setFinalCap(cap);
				uinfo.setStart(cap.getT());
				uinfo.setEnd(cap.getT());
				uinfos.put(mut.getA(), uinfo);
			}
			
			else {
				UserInfo uinfo = uinfos.get(mut.getA());
				uinfo.setEnd(mut.getT());
				uinfo.setFinalCap(cap);
			}
		}
		
		return uinfos;
	}
	/**
	 * Data structure for information stored on users
	 * @author LukeStacey
	 *
	 */
	class UserInfo {
		String uuid;
		long start;
		long end;
		Caption finalCap;
		/**
		 * Constructor
		 * @param uuid User ID
		 */
		public UserInfo(String uuid) {
			this.uuid = uuid;
		}
		/**
		 * getter for ID
		 * @return ID
		 */
		public String getUUID() {
			return this.uuid;
		}
		/**
		 * setter for ID
		 * @param uuid new value for ID
		 */
		public void setUUID(String uuid) {
			this.uuid = uuid;
		}
		/**
		 * getter for start
		 * @return start
		 */
		public long getStart() {
			return start;
		}
		/**
		 * setter for start
		 * @param start new value for start
		 */
		public void setStart(long start) {
			this.start = start;
		}
		/**
		 * getter for end
		 * @return end
		 */
		public long getEnd() {
			return this.end;
		}
		/**
		 * setter for end
		 * @param end new value for end
		 */
		public void setEnd(long end) {
			this.end = end;
		}
		/**
		 * getter for final caption
		 * @return final caption
		 */
		public Caption getFinalCap() {
			return this.finalCap;
		}
		/**
		 * setter for final caption
		 * @param cap new value for final caption
		 */
		public void setFinalCap(Caption cap) {
			this.finalCap = cap;
		}
	}

}
