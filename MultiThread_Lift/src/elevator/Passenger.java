package elevator;

import com.oocourse.elevator3.PersonRequest;
import enums.DownUp;
import java.util.Arrays;
import java.util.List;

public class Passenger {
    private final List floorsA = Arrays.asList(
            -3, -2, -1, 1, 15, 16, 17, 18, 19, 20);
    private final List floorsB = Arrays.asList(
            -2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    private final List floorsC = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15);
    // 装饰模式,为请求增加方向这个属性
    private int fromFloor;
    private int toFloor;
    private int personId;
    private DownUp direction;
    ////直达标志////
    private boolean straightA;
    private boolean straightB;
    private boolean straightC;
    
    public Passenger(PersonRequest request) {
        if (request == null) {
            return;
        }
        fromFloor = request.getFromFloor();
        toFloor = request.getToFloor();
        personId = request.getPersonId();
        straightA = straightB = straightC = false;
        setStraight();
        setDirection();
    }
    
    public Passenger(int personId, int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.personId = personId;
        straightA = straightB = straightC = false;
        setStraight();
        setDirection();
    }
    
    public void setStraight() {
        if (floorsA.contains(fromFloor) && floorsA.contains(toFloor)) {
            straightA = true;
            return;
        }
        if (floorsB.contains(fromFloor) && floorsB.contains(toFloor)) {
            straightB = true;
            return;
        }
        if (floorsC.contains(fromFloor) && floorsC.contains(toFloor)) {
            straightC = true;
        }
    }
    
    public void setDirection() {
        if (fromFloor == 3) {
            if (toFloor < 3) {
                direction = DownUp.Down;
            } else {
                if (toFloor >= 14) {
                    direction = DownUp.Up;
                } else {
                    if (!floorsC.contains(toFloor)) {
                        direction = DownUp.Down;
                    } else {
                        direction = DownUp.Up;
                    }
                }
            }
        } else if (toFloor == 3) {
            if (fromFloor <= 1) {
                direction = DownUp.Up;
            } else if (fromFloor == 2) {
                direction = DownUp.Down;
            } else if (fromFloor == 14) {
                direction = DownUp.Up;
            } else {
                if (fromFloor > 3) {
                    direction = DownUp.Down;
                } else {
                    direction = DownUp.Up;
                }
            }
        } else {
            if (fromFloor > toFloor) {
                direction = DownUp.Down;
            } else {
                direction = DownUp.Up;
            }
        }
    }
    
    public DownUp getDirection() {
        return direction;
    }
    
    public int getToFloor() {
        return toFloor;
    }
    
    public int getFromFloor() {
        return fromFloor;
    }
    
    public int getPersonId() {
        return personId;
    }
    
    public boolean isStraightA() {
        return straightA;
    }
    
    public boolean isStraightB() {
        return straightB;
    }
    
    public boolean isStraightC() {
        return straightC;
    }
    
    @Override
    public String toString() {
        return personId + "-FROM-" + fromFloor + "-TO-" + toFloor;
    }
    
}
