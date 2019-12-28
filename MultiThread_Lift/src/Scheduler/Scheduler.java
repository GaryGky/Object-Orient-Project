package scheduler;

import elevator.Passenger;
import enums.LiftStatus;
import enums.LiftType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scheduler {
    private final List floorsA = Arrays.asList(
            -3, -2, -1, 1, 15, 16, 17, 18, 19, 20);
    private final List floorsB = Arrays.asList(
            -2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    private final List floorsC = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15);
    private ArrayList<Passenger> reqList = new ArrayList<>();
    private boolean inputEnd;
    private int passengers = 0;
    
    public Scheduler() {
        inputEnd = false;
    }
    
    public synchronized Passenger getPerson(
            int curF, LiftStatus status, LiftType type)
            throws InterruptedException {
        if (reqList.isEmpty()) {
            if (status != LiftStatus.Wait) {
                notifyAll();
                return null;
            }
        }
        while (status == LiftStatus.Wait && !isWaitGet(type)) {
            //DebugHelper.dPrintln(1,"电梯陷入等待");
            wait();
            if (isEnd()) {
                System.exit(0);
            }
        }
        if (status == LiftStatus.Wait) {
            Passenger p = getPassenger(type);
            return p;
        } else {
            Passenger p = getPassenger(status, curF, type);
            return p;
        }
    }
    
    // 运行的时候取指令
    private synchronized Passenger getPassenger(
            LiftStatus status, int curF, LiftType type) {
        for (int i = 0; i < reqList.size(); i++) {
            Passenger person = reqList.get(i);
            if (type == LiftType.A) {
                if (person.isStraightB() || person.isStraightC()) {
                    continue;
                }
            } else if (type == LiftType.B) {
                if (person.isStraightC() || person.isStraightA()) {
                    continue;
                }
            } else if (type == LiftType.C) {
                if (person.isStraightA() || person.isStraightB()) {
                    continue;
                }
            }
            if (status.getIndex() == person.getDirection().getIndex() &&
                    curF == person.getFromFloor()) {
                Passenger p = reqList.get(i);
                reqList.remove(reqList.get(i));
                return p;
            }
        }
        return null;
    }
    
    // wait的时候加指令
    public synchronized Passenger getPassenger(LiftType type) {
        for (int i = 0; i < reqList.size(); i++) {
            Passenger p = reqList.get(i);
            if (type == LiftType.A) {
                if (p.isStraightC() || p.isStraightB()) {
                    continue;
                } else {
                    if (p.isStraightA() || floorsA.contains(p.getFromFloor())) {
                        reqList.remove(p);
                        return p;
                    }
                }
            } else if (type == LiftType.B) {
                if (p.isStraightA() || p.isStraightC()) {
                    continue;
                } else {
                    if (p.isStraightB() || floorsB.contains(p.getFromFloor())) {
                        reqList.remove(p);
                        return p;
                    }
                }
            } else if (type == LiftType.C) {
                if (p.isStraightA() || p.isStraightB()) {
                    continue;
                } else {
                    if (p.isStraightC() || floorsC.contains(p.getFromFloor())) {
                        reqList.remove(p);
                        return p;
                    }
                }
            }
        }
        return null;
    }
    
    private boolean isWaitGet(LiftType type) {
        // 等待的时候判断是否能取指令
        for (int i = 0; i < reqList.size(); i++) {
            Passenger p = reqList.get(i);
            if (type == LiftType.A) {
                if (p.isStraightC() || p.isStraightB()) {
                    continue;
                } else {
                    if (p.isStraightA() || floorsA.contains(p.getFromFloor())) {
                        return true;
                    }
                }
            } else if (type == LiftType.B) {
                if (p.isStraightA() || p.isStraightC()) {
                    continue;
                } else {
                    if (p.isStraightB() || floorsB.contains(p.getFromFloor())) {
                        return true;
                    }
                }
            } else if (type == LiftType.C) {
                if (p.isStraightA() || p.isStraightB()) {
                    continue;
                } else {
                    if (p.isStraightC() || floorsC.contains(p.getFromFloor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public synchronized void addPerson(Passenger passenger) {
        reqList.add(passenger);
        notifyAll();
        return;
    }
    
    public synchronized int getSize() {
        return reqList.size();
    }
    
    public synchronized boolean isEnd() {
        return passengers == 0 && inputEnd;
    }
    
    public synchronized void setInputEnd(boolean inputEnd) {
        this.inputEnd = inputEnd;
        notifyAll();
    }
    
    public synchronized void subPassengers() {
        passengers--;
    }
    
    public synchronized void addPassengers() {
        passengers++;
        notifyAll();
    }
    
    public int getPassengers() {
        return passengers;
    }
}

