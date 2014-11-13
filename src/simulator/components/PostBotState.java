package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@ComponentDescription("form message and topic to be passed on to mosquitto")
@OutPorts({
        @OutPort(value = "TOPIC", description = "topic to sent, in /robot_id/state/ or /robot_id/power/", type = String.class),
        @OutPort(value = "CONTENT", description = "content in state|car_id or power|car_id depends on the topic", type = String.class)
})

@InPorts({
        @InPort(value = "CARID", description = "the contents", type = String.class, optional = true),
        @InPort(value = "IN", description = "state", optional = true)
})

public class PostBotState extends PostBotPower {
    /*
    private String _robotId = "r1";
    private String _carId = "car1";
    private Boolean _state = Boolean.FALSE;

    private InputPort carId  ;
    private InputPort state  ;

    private OutputPort contentOut;
    private OutputPort topicOut;

    @Override
    protected void execute() throws Exception {
        _robotId = getName();
        _carId   = (String)  fillInput(carId, null);

        if(_carId == null)
            _carId = "_carId";

        // format and send state to mosquitto to send out
        Packet p;
        while((p = state.receive()) != null){
            _state = (Boolean) p.getContent();
            drop(p);

            topicOut.send(create("/" + _robotId ));
            contentOut.send(create(_state + "|" + _carId));

            System.out.println("state forwarded");
        }
    }

    @Override
    protected void openPorts() {
        carId   =  openInput("CARID");
        state   =  openInput("STATE");

        topicOut  = openOutput("TOPIC");
        contentOut  = openOutput("CONTENT");
    }

    public Object fillInput(InputPort inPort, Object defaultValue){
        Packet rp = inPort.receive();
        if (rp == null) {
            return defaultValue;
        }

        Object ret = rp.getContent();
        drop(rp);

        return ret;
    }
    */
}
