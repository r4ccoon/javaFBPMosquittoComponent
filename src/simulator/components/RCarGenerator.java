package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@ComponentDescription("create a car if it gets told to")

@InPort(value = "COMMAND" , type = Boolean.class)
@OutPorts({
        @OutPort(value = "CAR", type = Integer.class)
})
public class RCarGenerator extends Component {

    private OutputPort outPortCar;
    private InputPort inPortCommand;

    private int carCounter = 0;

    @Override
    protected void execute() throws Exception {

        Packet pInCommand;
        //pInCommand = inPortCommand.receive();
        while ((pInCommand = inPortCommand.receive()) != null)
        {
            Boolean command = (Boolean) pInCommand.getContent();
            drop(pInCommand);

            // if it gets the command to throw out a car
            if(command)
            {
                ++carCounter;
                Packet out = create(carCounter);
                outPortCar.send(out);
            }
        }
    }

    @Override
    protected void openPorts() {
        inPortCommand = openInput("COMMAND");
        outPortCar = openOutput("CAR");
    }
}
