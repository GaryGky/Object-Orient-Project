package elevator;

import scheduler.Scheduler;
import enums.LiftType;
import java.util.Arrays;
import java.util.List;

public class LiftA extends ElevatorBase {
    private final List floors = Arrays.asList(
            -3, -2, -1, 1, 15, 16, 17, 18, 19, 20);
    
    public LiftA(double moveTime, int capacity, Scheduler s, LiftType type) {
        super(moveTime, capacity, s, type);
        setFloors(floors);
    }
}
