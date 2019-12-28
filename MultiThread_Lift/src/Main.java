import elevator.LiftA;
import elevator.LiftB;
import elevator.LiftC;
import elevator.Passenger;
import scheduler.Scheduler;
import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import enums.LiftType;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Scheduler scheduler = new Scheduler();
        Thread liftA = new LiftA(400, 6, scheduler, LiftType.A);
        Thread liftB = new LiftB(500, 8, scheduler, LiftType.B);
        Thread liftC = new LiftC(600, 7, scheduler, LiftType.C);
        liftA.setName("A");
        liftB.setName("B");
        liftC.setName("C");
        liftA.start();
        liftB.start();
        liftC.start();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                scheduler.setInputEnd(true);
                break;
            } else {
                scheduler.addPerson(new Passenger(request));
                scheduler.addPassengers();
            }
        }
        elevatorInput.close();
    }
}
