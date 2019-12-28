package elevator;

import scheduler.Scheduler;
import com.oocourse.TimableOutput;
import enums.LiftStatus;
import enums.LiftType;

import java.util.Arrays;
import java.util.List;

import static enums.LiftType.C;

public class LiftC extends ElevatorBase {
    private List floors = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15);
    
    public LiftC(double moveTime, int capacity, Scheduler s, LiftType type) {
        super(moveTime, capacity, s, type);
        setFloors(floors);
    }
    
    public void arrive() throws InterruptedException {
        TimableOutput.println("ARRIVE-" + getCurFloor() + "-" + C.getType());
        Passenger person;
        if (getMainPerson() != null) {
            if (getMainPerson().getFromFloor() == 3 &&
                    getMainPerson().getToFloor() <= 12) {
                if (getStatus() == LiftStatus.Up) {
                    setStatus(LiftStatus.Down);
                }
            }
        }
        if (floors.contains(getCurFloor()) ||
                getTransFloors().contains(getCurFloor())) {
            // 如果当前到达楼层属于电梯可停靠楼层才能进行门操作和上下客
            person = pickPerson();
            checkArrive(person);
        }
    }
    
    public void upDateDst(Passenger person) {
        // 更新目标楼层
        if (getCurFloor() == 3) {
            if (getStatus() == LiftStatus.Up) {
                setDstFloor(15);
            } else if (getStatus() == LiftStatus.Down) {
                setDstFloor(1);
            }
            return;
        }
        if (getStatus() == LiftStatus.Up) {
            if (person.getToFloor() >= 15) {
                setDstFloor(15);
            } else {
                setDstFloor(Math.max(getDstFloor(), person.getToFloor()));
            }
        } else {
            if (person.getToFloor() <= 1) {
                setDstFloor(1);
            } else {
                setDstFloor(Math.min(getDstFloor(), person.getToFloor()));
            }
        }
    }
}
