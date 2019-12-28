package elevator;

import com.oocourse.TimableOutput;
import enums.DownUp;
import enums.LiftStatus;
import enums.LiftType;
import scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ElevatorBase extends Thread {
    private final double doorTime = 200;
    private final double moveTime;
    private final int capacity;
    private List floors;
    private final List transFloors = Arrays.asList(1, 15);
    //////////////////////////////////
    private Scheduler scheduler;
    private Passenger mainPerson;
    private ArrayList<Passenger> waitList;
    private LiftStatus status;
    private int curFloor; // 电梯当前所在楼层
    private int dstFloor; // 电梯即将去往的楼层
    private LiftType type;
    
    public ElevatorBase(double moveTime,
                        int capacity, Scheduler s, LiftType type) {
        this.moveTime = moveTime;
        this.capacity = capacity;
        scheduler = s;
        this.type = type;
        waitList = new ArrayList<>();
        status = LiftStatus.Wait;
        mainPerson = null;
        curFloor = 1;
        dstFloor = 1;
    }
    
    @Override
    public void run() {
        while (true) {
            if (scheduler.isEnd()) {
                break;
            }
            try {
                sendPerson();
                status = LiftStatus.Wait;
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendPerson() throws InterruptedException {
        Passenger passenger = scheduler.getPerson(curFloor, status, type);
        if (passenger == null) {
            return;
        }
        mainPerson = passenger;
        setDirection();
        while (curFloor != dstFloor) {
            while (curFloor != dstFloor) {
                liftMove();
            }
            if (floors.contains(curFloor)) {
                Passenger p = pickPerson();
                checkArrive(p);
            }
        }
    }
    
    public void setDirection() {
        if (curFloor == mainPerson.getFromFloor()) {
            dstFloor = mainPerson.getToFloor();
            if (curFloor < mainPerson.getToFloor()) {
                status = LiftStatus.Up;
            } else {
                status = LiftStatus.Down;
            }
        } else if (curFloor < mainPerson.getFromFloor()) {
            status = LiftStatus.Up;
            dstFloor = mainPerson.getFromFloor();
        } else if (curFloor > mainPerson.getFromFloor()) {
            status = LiftStatus.Down;
            dstFloor = mainPerson.getFromFloor();
        }
        if (curFloor < dstFloor) {
            status = LiftStatus.Up;
        } else if (curFloor > dstFloor) {
            status = LiftStatus.Down;
        }
    }
    
    public void liftMove() throws InterruptedException {
        if (floors.contains(curFloor) || transFloors.contains(curFloor)) {
            Passenger person = pickPerson();
            checkArrive(person);
        }
        if (curFloor < dstFloor) {
            curFloor++;
            if (curFloor == 0) {
                curFloor++;
            }
        } else if (curFloor > dstFloor) {
            curFloor--;
            if (curFloor == 0) {
                curFloor--;
            }
        }
        sleep((long) moveTime);
        arrive();
    }
    
    public void arrive() throws InterruptedException {
        TimableOutput.println("ARRIVE-" + curFloor + "-" + type.getType());
        Passenger person;
        if (floors.contains(curFloor) || transFloors.contains(curFloor)) {
            // 如果当前到达楼层属于电梯可停靠楼层才能进行门操作和上下客
            person = pickPerson();
            checkArrive(person);
        }
    }
    
    public Passenger pickPerson() throws InterruptedException {
        if (isFull()) {
            return null;
        }
        Passenger passenger;
        passenger = scheduler.getPerson(curFloor, status, type);
        if (passenger != null) {
            // 如果能返回乘客，就把该乘客加入队列
            waitList.add(passenger);
            upDateDst(passenger);
            return passenger;
        }
        if (mainPerson != null && curFloor == mainPerson.getFromFloor() &&
                checkDir()) {
            // 如果不能加入乘客，但是到达了主乘客的起始楼层，返回主乘客
            passenger = mainPerson;
            waitList.add(passenger);
            upDateDst(mainPerson);
            mainPerson = null;
            return passenger;
        }
        return null;
    }
    
    public void checkArrive(Passenger passenger) throws InterruptedException {
        // 每到一层楼就要检查是否需要上下客
        if (curFloor != dstFloor) {
            // 如果没有到达
            if (checkOff() || passenger != null || checkTrans()) {
                setDoor(passenger);
            }
        } else {
            if (checkOff() || mainPerson != null ||
                    passenger != null || checkTrans()) {
                setDoor(passenger);
            }
        }
    }
    
    public boolean checkOff() {
        // 是否需要下电梯
        // 乘客在此换乘
        Iterator<Passenger> it = getWaitList().iterator();
        for (; it.hasNext(); ) {
            Passenger p = it.next();
            if (p.getToFloor() == getCurFloor()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkTrans() {
        Iterator<Passenger> piterator = getWaitList().iterator();
        if (getCurFloor() == 1 || getCurFloor() == 15) {
            for (; piterator.hasNext(); ) {
                Passenger p = piterator.next();
                if (!floors.contains(p.getToFloor())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setDoor(Passenger person) throws InterruptedException {
        open((int) doorTime);
        if (checkOff() || checkTrans()) {
            getOff();
        }
        if (person != null) {
            getOn(person);
        }
        if (person == null && dstFloor == curFloor &&
                mainPerson != null) {
            setDirection();
        }
        synchronized (scheduler) {
            close((int) doorTime);
            if (scheduler.isEnd()) {
                System.exit(0);
            }
        }
    }
    
    public void getOff() {
        Iterator<Passenger> passengerIterator = getWaitList().iterator();
        for (; passengerIterator.hasNext(); ) {
            Passenger p = passengerIterator.next();
            if (p.getToFloor() == getCurFloor()) {
                TimableOutput.println("OUT-" + p.getPersonId()
                        + "-" + getCurFloor() + "-" + type.getType());
                //DebugHelper.dPrintln(1, "乘客需求完成" + p.toString());
                scheduler.subPassengers();
                //DebugHelper.dPrintln(1, "剩余乘客数量" + scheduler.getPassengers());
                passengerIterator.remove();
            }
        }
        // 转乘
        Iterator<Passenger> piterator = getWaitList().iterator();
        if (getCurFloor() == 1 || getCurFloor() == 15) {
            for (; piterator.hasNext(); ) {
                Passenger p = piterator.next();
                if (!floors.contains(p.getToFloor())) {
                    Passenger passenger = new Passenger(
                            p.getPersonId(), curFloor, p.getToFloor());
                    TimableOutput.println("OUT-" + p.getPersonId() + "-"
                            + getCurFloor() + "-" + type.getType());
                    piterator.remove();
                    getScheduler().addPerson(passenger);
                    if (waitList.isEmpty() && mainPerson == null) {
                        dstFloor = curFloor;
                    }
                }
            }
        }
    } // 一次性下所有该楼层乘客
    
    public boolean checkDir() {
        if (status == LiftStatus.Up && mainPerson.getDirection() == DownUp.Up) {
            return true;
        } else if (status == LiftStatus.Down &&
                mainPerson.getDirection() == DownUp.Down) {
            return true;
        }
        return false;
    }
    
    public void open(int sleepTime) throws InterruptedException {
        // 开门——乘客下去
        TimableOutput.println("OPEN-" + curFloor + "-" + type.getType());
        sleep(sleepTime);
    }
    
    public void close(int sleepTime) throws InterruptedException {
        //sleepSometime
        sleep(sleepTime);
        Passenger person;
        while ((person = pickPerson()) != null) {
            upDateDst(person);
            getOn(person);
            //DebugHelper.dPrintln(1, "电梯加入指令" + person.toString());
        }
        // 关门前，将所有能捎带的乘客加入
        TimableOutput.println("CLOSE-" + curFloor + "-" + type.getType());
    }
    
    public void upDateDst(Passenger person) {
        // 更新目标楼层
        if (status == LiftStatus.Up) {
            dstFloor = Math.max(dstFloor, person.getToFloor());
            //DebugHelper.dPrintln(4, "此时最高楼层为：" + dstFloor);
        } else {
            dstFloor = Math.min(dstFloor, person.getToFloor());
            if (person.getToFloor() == 3) {
                dstFloor = Math.min(dstFloor, 1);
            }
            //DebugHelper.dPrintln(4, "此时最低楼层为： " + dstFloor);
        }
    }
    
    //////////////////////////////
    // 简单方法
    public void getOn(Passenger person) {
        TimableOutput.println("IN-" + person.getPersonId() +
                "-" + curFloor + "-" + type.getType());
    }
    
    public void setMainPerson(Passenger mainPerson) {
        this.mainPerson = mainPerson;
    }
    
    public boolean isFull() {
        return waitList.size() == capacity;
    }
    
    public int getDstFloor() {
        return dstFloor;
    }
    
    public void setDstFloor(int dstFloor) {
        this.dstFloor = dstFloor;
    }
    
    public int getCurFloor() {
        return curFloor;
    }
    
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    public ArrayList<Passenger> getWaitList() {
        return waitList;
    }
    
    public Passenger getMainPerson() {
        return mainPerson;
    }
    
    public void setFloors(List floors) {
        this.floors = floors;
    }
    
    public List getTransFloors() {
        return transFloors;
    }
    
    public LiftStatus getStatus() {
        return status;
    }
    
    public void setStatus(LiftStatus status) {
        this.status = status;
    }
}
