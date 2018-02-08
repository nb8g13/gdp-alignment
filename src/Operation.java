/**
 * Interface for all the different operations that an edit can be
 * @author LukeStacey
 *
 */
public interface Operation {
	public Caption performOperation(String text, Edit edit, double reputation);
}
