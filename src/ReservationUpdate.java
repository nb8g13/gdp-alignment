/**
 * Created by Pro on 04/02/2018.
 */
public class ReservationUpdate {

    int location;
    int updateValue;
    String action;

    Boolean isComplete;

    public ReservationUpdate(int location, int updateValue, String action) {
        this.location = location;
        this.updateValue = updateValue;
        this.action = action;
        isComplete = false;
    }

    public int getLocation() {
        return location;
    }

    public int getUpdateValue() {
        return updateValue;
    }

    public String getAction() {
        return action;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Boolean getComplete() {
        return isComplete;
    }
}
