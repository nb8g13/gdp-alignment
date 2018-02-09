/**
 * Interface for various ways to merge profiles
 * @author LukeStacey
 *
 */
public interface ProfileMerger {
	
	public Profile merge(Profile left, Profile right);
	
}
