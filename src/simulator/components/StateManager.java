package simulator.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Regulating state of pipeline")

@InPorts({ @InPort(value = "STATE1", description = "State of the Robot 1", type = Boolean.class),
	@InPort(value = "STATE2", description = "State of the Robot 2", type = Boolean.class),
	@InPort(value = "STATE3", description = "State of the Robot 3", type = Boolean.class),
	@InPort(value = "STATE4", description = "State of the Robot 4", type = Boolean.class)})

@OutPorts({ @OutPort(value = "MOVE1", description = "Command to move car at the Robot 1", type = Boolean.class),
	@OutPort(value = "MOVE2", description = "Command to move car at the Robot 2", type = Boolean.class),
	@OutPort(value = "MOVE3", description = "Command to move car at the Robot 3", type = Boolean.class),
	@OutPort(value = "MOVE4", description = "Command to move car at the Robot 4", type = Boolean.class)})

@OutPort("GENERATE")
public class StateManager  extends Component {
	
	private InputPort state1Port, state2Port, state3Port, state4Port;
	private OutputPort generatePort, move1port, move2port, move3port, move4port;
	
	@Override
	protected void execute() throws Exception {
		
		Boolean state = false;
		Boolean state1 = false;
		Boolean state2 = false;
		Boolean state3 = false;
		Boolean state4 = false;
		
		Packet state1Packet;
		Packet state2Packet;
		Packet state3Packet;
		Packet state4Packet;
		
	    while ((state1Packet = state1Port.receive()) != null) {
	    	state1 = (Boolean) state1Packet.getContent();
	    	drop(state1Packet);
	    	state = getState(state1, state2, state3, state4);
	    	
	    	Packet statePacket = create(state);
			if (!generatePort.isClosed()) {
				generatePort.send(statePacket);
			}
			if(state){
				if (!move1port.isClosed()) {
					move1port.send(statePacket);
				}
				if (!move2port.isClosed()) {
					move2port.send(statePacket);
				}
				if (!move3port.isClosed()) {
					move3port.send(statePacket);
				}
				if (!move4port.isClosed()) {
					move4port.send(statePacket);
				}
			}
	    }
	    while ((state2Packet = state2Port.receive()) != null) {
	    	state2 = (Boolean) state2Packet.getContent();
	    	drop(state2Packet);
	    	state = getState(state1, state2, state3, state4);
	    	
	    	Packet statePacket = create(state);
			if (!generatePort.isClosed()) {
				generatePort.send(statePacket);
			}
			if(state){
				if (!move1port.isClosed()) {
					move1port.send(statePacket);
				}
				if (!move2port.isClosed()) {
					move2port.send(statePacket);
				}
				if (!move3port.isClosed()) {
					move3port.send(statePacket);
				}
				if (!move4port.isClosed()) {
					move4port.send(statePacket);
				}
			}
	    }
	    while ((state3Packet = state3Port.receive()) != null) {
	    	state3 = (Boolean) state3Packet.getContent();
	    	drop(state3Packet);
	    	state = getState(state1, state2, state3, state4);
	    	
	    	Packet statePacket = create(state);
			if (!generatePort.isClosed()) {
				generatePort.send(statePacket);
			}
			if(state){
				if (!move1port.isClosed()) {
					move1port.send(statePacket);
				}
				if (!move2port.isClosed()) {
					move2port.send(statePacket);
				}
				if (!move3port.isClosed()) {
					move3port.send(statePacket);
				}
				if (!move4port.isClosed()) {
					move4port.send(statePacket);
				}
			}
	    }
	    while ((state4Packet = state4Port.receive()) != null) {
	    	state4 = (Boolean) state4Packet.getContent();
	    	drop(state4Packet);
	    	state = getState(state1, state2, state3, state4);
	    	
	    	Packet statePacket = create(state);
			if (!generatePort.isClosed()) {
				generatePort.send(statePacket);
			}
			if(state){
				if (!move1port.isClosed()) {
					move1port.send(statePacket);
				}
				if (!move2port.isClosed()) {
					move2port.send(statePacket);
				}
				if (!move3port.isClosed()) {
					move3port.send(statePacket);
				}
				if (!move4port.isClosed()) {
					move4port.send(statePacket);
				}
			}
	    }
	}

	@Override
	protected void openPorts() {
		state1Port = openInput("STATE1");
		state2Port = openInput("STATE2");
		state3Port = openInput("STATE3");
		state4Port = openInput("STATE4");
		
		generatePort = openOutput("GENERATE");
		move1port = openOutput("MOVE1");
		move1port = openOutput("MOVE1");
		move1port = openOutput("MOVE1");
		move1port = openOutput("MOVE1");
		
	}
	
	private Boolean getState(Boolean state1, Boolean state2, Boolean state3, Boolean state4){
		return(state1 && state2 && state3 && state4);
	}

}
