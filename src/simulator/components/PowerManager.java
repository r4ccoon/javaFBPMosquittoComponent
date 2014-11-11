package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@InPorts({
        @InPort(value = "SWITCH"),
        @InPort(value = "RANGEBEGIN"),
        @InPort(value = "RANGEEND"),
})

@OutPorts({
        @OutPort(value = "SENSORDATA", optional = true)
})

public class PowerManager extends Component {
    private InputPort inPortSwitch;
    private InputPort inPortRangeBegin;
    private InputPort inPortRangeEnd;

    private OutputPort outportdata;

    private Integer rangeBegin;
    private Integer rangeEnd;

    @Override
    protected void execute() throws Exception {
        Packet pRangeBegin = Helper.checkInput(inPortRangeBegin);
        Packet pRangeEnd = Helper.checkInput(inPortRangeEnd);

        rangeBegin = (Integer) pRangeBegin.getContent();
        rangeEnd = (Integer) pRangeEnd.getContent();

        drop(pRangeBegin);
        drop(pRangeEnd);

        Packet pInSwitch;
        while ((pInSwitch = inPortSwitch.receive()) != null)
        {
            long processTime = (Integer)pInSwitch.getContent();
            processTime = processTime * 1000000;
            drop(pInSwitch);

            long startTime = System.nanoTime();

            do
            {
                if(System.nanoTime() - startTime >= processTime){
                    //System.out.println("power finished");
                    break;
                }

                // generate random energy
                Integer randomEnergy = rangeBegin + (int) (Math.random() * rangeEnd);

                // we will send the data to the mqtt
                Packet out = create(randomEnergy);
                outportdata.send(out);

                //System.out.println("using power " + randomEnergy);

                drop(out);

                sleep(1000);
            } while (true);
        }

    }

    @Override
    protected void openPorts() {
        inPortSwitch = openInput("SWITCH");
        inPortRangeBegin = openInput("RANGEBEGIN");
        inPortRangeEnd = openInput("RANGEEND");

        outportdata = openOutput("SENSORDATA");
    }
}
