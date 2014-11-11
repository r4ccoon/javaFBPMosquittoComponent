package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@ComponentDescription("form message and topic to be passed on to mosquitto")
@OutPorts({
        @OutPort(value = "TOPIC", description = "topic to sent, in /robot_id/state/ or /robot_id/power/", type = String.class),
        @OutPort(value = "CONTENT", description = "content in state|car_id or power|car_id depends on the topic", type = String.class)
})

@InPorts({
        @InPort(value = "ROBOTID", description = "client id of the client, can put random string here", type = String.class),
        @InPort(value = "CARID", description = "the contents", type = String.class),
        @InPort(value = "STATE", description = "state", type = Boolean.class),
        @InPort(value = "POWER", description = "power", type = Double.class)
})

public class PostBot extends Component {
    private String _robotId = "r1";
    private String _carId = "car1";
    private Double _power = 0.0d;
    private Boolean _state = Boolean.FALSE;

    private InputPort robotId;
    private InputPort carId  ;
    private InputPort power  ;
    private InputPort state  ;

    private OutputPort contentOut;
    private OutputPort topicOut;

    @Override
    protected void execute() throws Exception {
        _robotId = (String) Helper.checkInput(robotId).getContent();
        _carId   = (String) Helper.checkInput(carId).getContent();
        _power   = (Double) Helper.checkInput(power).getContent();
        _state   = (Boolean) Helper.checkInput(state).getContent();

        if(_carId == null)
            _carId = "_carId";

        // format and send state to mosquitto to send out
        if(_state != null){
            Packet topicPacket = create("/" + _robotId + "/state");
            topicOut.send(topicPacket);

            Packet statePacket = create(_state + "|" + _carId);
            contentOut.send(statePacket);
        }

        // format and send power to mosquitto to send out
        if(_power != null){
            Packet topicPacket = create("/" + _robotId + "/power");
            topicOut.send(topicPacket);

            Packet powerPacket = create(_power + "|" + _carId);
            contentOut.send(powerPacket);
        }
    }

    @Override
    protected void openPorts() {
        robotId =  openInput("ROBOTID");
        carId   =  openInput("CARID");
        power   =  openInput("STATE");
        state   =  openInput("POWER");

        topicOut  = openOutput("TOPIC");
        contentOut  = openOutput("CONTENT");
    }
}
