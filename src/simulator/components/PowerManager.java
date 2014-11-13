package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@InPorts({
        @InPort(value = "SWITCH"),
        @InPort(value = "RANGEBEGIN"),
        @InPort(value = "RANGEEND"),
        @InPort(value = "PROCESSTIME"),
})

@OutPorts({
        @OutPort(value = "SENSORDATA", optional = true),
        @OutPort(value = "STATE", optional = true)
})

public class PowerManager extends Component {
    private InputPort inPortSwitch;
    private InputPort inPortRangeBegin;
    private InputPort inPortRangeEnd;
    private InputPort inProcessTime;

    private OutputPort outportdata;
    private OutputPort outportstate;

    @Override
    protected void execute() throws Exception {
        Packet pRangeBegin = checkInput(inPortRangeBegin);
        Packet pRangeEnd = checkInput(inPortRangeEnd);

        Integer rangeBegin = (Integer) pRangeBegin.getContent();
        Integer rangeEnd = (Integer) pRangeEnd.getContent();

        Packet pp = inProcessTime.receive();
        long processTime = (Integer)pp.getContent();
        processTime = processTime * 1000000;

        drop(pp);
        drop(pRangeBegin);
        drop(pRangeEnd);

        Packet pInSwitch;
        while ((pInSwitch = inPortSwitch.receive()) != null)
        {
            drop(pInSwitch);

            long startTime = System.nanoTime();

            // we will send the data to the mqtt
            outportstate.send(create(true));

            do
            {
                if(System.nanoTime() - startTime >= processTime){
                    System.out.println("power finished");
                    break;
                }

                // generate random energy
                Integer randomEnergy = rangeBegin + (int) (Math.random() * rangeEnd);

                // we will send the data to the mqtt
                outportdata.send(create(randomEnergy));

                System.out.println("using power " + randomEnergy);
                Thread.sleep(1000);
            } while (true);

            // we will send the data to the mqtt
            outportstate.send(create(false));
        }

    }

    @Override
    protected void openPorts() {
        inPortSwitch = openInput("SWITCH");
        inProcessTime = openInput("PROCESSTIME");
        inPortRangeBegin = openInput("RANGEBEGIN");
        inPortRangeEnd = openInput("RANGEEND");

        outportdata = openOutput("SENSORDATA");
        outportstate = openOutput("STATE");
    }

    public Packet checkInput(InputPort inPort) throws Exception {
        Packet rp = inPort.receive();
        if (rp == null) {
            return null;
        }

        return rp;
    }
}
