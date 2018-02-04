import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FinishedCaptionFilter implements CaptionFilter {

	@Override
	public List<Caption> filter(List<Mutation> mutations, List<Caption> captions) {
		Map<String, UserInfo> uinfos = this.generateUserInfo(mutations, captions);
		System.out.println("User info generated");
		Iterator<UserInfo> iter = uinfos.values().iterator();
		List<Caption> candidates = new ArrayList<Caption>();
		
		System.out.println("At iterators");
		while(iter.hasNext()) {
			UserInfo current = iter.next();
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
	
	class UserInfo {
		String uuid;
		long start;
		long end;
		Caption finalCap;
		
		public UserInfo(String uuid) {
			this.uuid = uuid;
		}
		
		public String getUUID() {
			return this.uuid;
		}
		
		public void setUUID(String uuid) {
			this.uuid = uuid;
		}
		
		public long getStart() {
			return start;
		}
		
		public void setStart(long start) {
			this.start = start;
		}
		
		public long getEnd() {
			return this.end;
		}
		
		public void setEnd(long end) {
			this.end = end;
		}
		
		public Caption getFinalCap() {
			return this.finalCap;
		}
		
		public void setFinalCap(Caption cap) {
			this.finalCap = cap;
		}
	}

}
