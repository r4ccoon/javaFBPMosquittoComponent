package simulator.network;

import com.jpmorrsn.fbp.engine.*;
import simulator.components.Helper;

@InPorts({
        @InPort(value = "CAR"),
        @InPort(value = "ROBOTNAME"),
        @InPort(value = "PROCESSTIME", type = Integer.class),
        @InPort(value = "COMMAND", type = Boolean.class, optional = true),
})

@OutPorts({
        @OutPort(value = "COMMAND", type = Boolean.class, optional = true),
        @OutPort(value = "CAR", type = Integer.class),

        @OutPort(value = "STATELOG", type = Integer.class),
        @OutPort(value = "CARLOG", optional = true),
})

public class RRobot extends Component {
    private InputPort inPortCar;
    private InputPort inPortRobotName;
    private InputPort inProcessTime;

    private OutputPort outPortCar;
    private OutputPort outportcarlog;
    private OutputPort outportdata;
    private OutputPort outportstatelog;
    private OutputPort outportcommand;

    private Integer processTime;
    private String robotName;

    @Override
    protected void execute() throws Exception {
        Packet p = Helper.checkInput(inProcessTime);
        Packet p2 = Helper.checkInput(inPortRobotName);
        processTime = (Integer) p.getContent();
        robotName = (String) p2.getContent();
        drop(p);
        drop(p2);

        Packet pInCar;
        while ((pInCar = inPortCar.receive()) != null)
        {
            Integer carSerialNumber =  (Integer)pInCar.getContent();

            // send on to the power manager
            Packet outSwitch = create(processTime);
            outportstatelog.send(outSwitch);

            //
            System.out.println("car " + carSerialNumber + " is in robot " + robotName);

            // there is a car, let s sleep to do this robot job
            sleep(processTime);

            // drop so next car can come in
            drop(pInCar);

            // we will send signal "empty (true)" to the previous component
            Packet cmd = create(Boolean.TRUE);
            outportcommand.send(cmd);

            // we will send the thing to the next robot
            Packet out = create(carSerialNumber);
            outPortCar.send(out);

            //
            System.out.println("car " + carSerialNumber + " is Out from robot " + robotName);
            //drop(cmd);
        }

        Packet pInCommand;
        while ((pInCar = inPortCar.receive()) != null){
            
        }
    }

    @Override
    protected void openPorts() {
        inPortCar = openInput("CAR");
        inPortRobotName = openInput("ROBOTNAME");
        inProcessTime = openInput("PROCESSTIME");

        outportcommand = openOutput("COMMAND");
        outPortCar = openOutput("CAR");
        outportstatelog = openOutput("STATELOG");
        outportcarlog = openOutput("CARLOG");
    }
}
