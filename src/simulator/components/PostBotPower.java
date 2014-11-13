package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@ComponentDescription("form message and topic to be passed on to mosquitto")
@OutPorts({
        @OutPort(value = "TOPIC", description = "topic to sent, in /robot_id/state/ or /robot_id/power/", type = String.class),
        @OutPort(value = "CONTENT", description = "content in state|car_id or power|car_id depends on the topic", type = String.class)
})

@InPorts({
        @InPort(value = "CARID", description = "the contents", type = String.class, optional = true),
        @InPort(value = "IN", description = "sensor data")
})

public class PostBotPower extends Component {
    protected String _robotId = "r1";
    protected String _carId = "car1";
    protected Object _power;

    protected InputPort carId  ;
    protected InputPort power  ;

    protected OutputPort contentOut;
    protected OutputPort topicOut;

    @Override
    protected void execute() throws Exception {
        _robotId = getName();
        _carId   = (String)  fillInput(carId, null);

        if(_carId == null)
            _carId = "_carId";

        // format and send power to mosquitto to send out
        Packet po;
        while ((po = power.receive()) != null){
            _power = po.getContent();
            drop(po);

            topicOut.send(create( _robotId ));
            contentOut.send(create(_power + "|" + _carId));

            System.out.println("power forwarded");
        }
    }

    @Override
    protected void openPorts() {
        carId   =  openInput("CARID");
        power   =  openInput("IN");

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
}
