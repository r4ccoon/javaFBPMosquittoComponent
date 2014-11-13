package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@InPorts({
        @InPort(value = "CAR"),
        @InPort(value = "PROCESSTIME", type = Integer.class),
        @InPort(value = "COMMAND", type = Boolean.class, optional = true),
})

@OutPorts({
        @OutPort(value = "COMMAND", type = Boolean.class, optional = true),
        @OutPort(value = "CAR", type = Integer.class),

        @OutPort(value = "STATELOG", type = Integer.class, optional = true),
        @OutPort(value = "CARLOG", optional = true),
})

public class RRobot extends Component {
    private InputPort inPortCar;
    private InputPort inProcessTime;
    private InputPort inCommand;

    private OutputPort outPortCar;
    private OutputPort outportcarlog;
    private OutputPort outportdata;
    private OutputPort outportstatelog;
    private OutputPort outportcommand;

    private long processTime;
    private String robotName;
    private Integer carSerialNumber;

    private Boolean finished = false;

    @Override
    protected void execute() throws Exception {
        robotName = getName();

        Packet p = checkInput(inProcessTime);
        inProcessTime.close();
        processTime = (Integer) p.getContent() * 1000000;
        drop(p);
        Packet pInCar = null;
        Packet pInCommand;
        while ((pInCommand = inCommand.receive()) != null){
            Boolean cmd =  (Boolean)pInCommand.getContent();
            inCommand.close();
            drop(pInCommand);

            try {
                pInCar = inPortCar.receive();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(pInCar == null)
                continue;

            //while ((pInCar = inPortCar.receive()) != null)
            {
                carSerialNumber =  (Integer)pInCar.getContent();
                System.out.println("car " + carSerialNumber + " is in robot " + robotName);

                // there is a car, let s process it to do this robot job
                long startTime = System.nanoTime();
                finished = false;
                do {
                    // check if it s in processing timeframe
                    if(System.nanoTime() - startTime >= processTime){
                        System.out.println("car " + carSerialNumber + " is finished from robot " + robotName);
                        finished = true;
                        break;
                    }

                    // processing
                    // send on to the power manager
                    Packet outSwitch = create(processTime);
                    outportstatelog.send(outSwitch);
                    // every 1 sec

                        sleep(1000);

                }
                while (true);
            }

            if(cmd){
                if(carSerialNumber != null){
                    do
                    {
                        // wait for the car to finish processed
                        System.out.println("waiting car is being processed");
                    } while (!finished);

                    // drop packet car
                    drop(pInCar);

                    // we will send the thing to the next robot
                    Packet out = create(carSerialNumber);
                    outPortCar.send(out);

                    //
                    System.out.println("car " + carSerialNumber + " is Out from robot " + robotName);

                    // there is no car, ask from the previous robot
                    Packet askCar = create(Boolean.TRUE);
                    outportcommand.send(askCar);

                } else {
                    // there is no car, ask from the previous robot
                    Packet askCar = create(Boolean.TRUE);
                    outportcommand.send(askCar);
                }
            }
        }



        /*
        Packet pInCar;
        while ((pInCar = inPortCar.receive()) != null)
        {
            carSerialNumber =  (Integer)pInCar.getContent();

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
            Packet askCar = create(Boolean.TRUE);
            outportcommand.send(askCar);

            if(carSerialNumber != null){
                // we will send the thing to the next robot
                Packet out = create(carSerialNumber);
                outPortCar.send(out);
                //
                System.out.println("car " + carSerialNumber + " is Out from robot " + robotName);
            }
        }
        */
    }

    @Override
    protected void openPorts() {
        inProcessTime = openInput("PROCESSTIME");
        inCommand = openInput("COMMAND");
        inPortCar = openInput("CAR");

        outportcommand = openOutput("COMMAND");
        outPortCar = openOutput("CAR");
        outportstatelog = openOutput("STATELOG");
        outportcarlog = openOutput("CARLOG");
    }

    public Packet checkInput(InputPort inPort) throws Exception {
        Packet rp = inPort.receive();
        if (rp == null) {
            return null;
        }

        return rp;
    }
}
