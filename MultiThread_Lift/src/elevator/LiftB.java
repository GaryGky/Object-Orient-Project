package elevator;

import enums.LiftStatus;
import scheduler.Scheduler;
import enums.LiftType;

import java.util.Arrays;
import java.util.List;

public class LiftB extends ElevatorBase {
    private final List floors = Arrays.asList(
            -2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    
    public LiftB(double moveTime, int capacity, Scheduler s, LiftType type) {
        super(moveTime, capacity, s, type);
        setFloors(floors);
    }
    
    @Override
    public void upDateDst(Passenger person) {
        if (getStatus() == LiftStatus.Up) {
            if (person.getToFloor() >= 15) {
                setDstFloor(15);
            } else {
                setDstFloor(Math.max(getDstFloor(), person.getToFloor()));
            }
        } else {
            if (person.getToFloor() <= -2) {
                setDstFloor(-2);
            } else {
                setDstFloor(Math.min(getDstFloor(), person.getToFloor()));
            }
        }
    }
}
