package simulator.components;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;


@InPorts({ @InPort(value = "CAR"),
	@InPort(value = "COMMAND", type = Boolean.class),
	@InPort(value = "FROM"),
	@InPort(value = "TO")})

@OutPorts({ @OutPort(value = "STATE", type = Boolean.class),
	@OutPort(value = "CAR"),
	/*@OutPort(value = "STATELOG", type = Boolean.class),
	@OutPort(value = "SENSORDATA"),
	@OutPort(value = "CARLOG")*/})

public class Robot extends Component {

	private OutputPort outportstate, outportcar/*, outportstatelog, outportdata, outportcarlog*/;

	private InputPort inportcar, inportcommand, inportfrom, inportto;

	private int from;
	private int to;
	
	@Override
	protected void execute() throws Exception {
		// TODO Auto-generated method stub
		
		Packet ip;
		ip = inportfrom.receive();
		from = Integer.parseInt((String)ip.getContent());
		drop(ip);
		
		ip = inportto.receive();
		to = Integer.parseInt((String)ip.getContent());
		drop(ip);
		
		Packet carpacket;
		while((carpacket = inportcar.receive())!=null){
			
			int car = Integer.parseInt((String)carpacket.getContent());
			drop(carpacket);
			
			Packet statepacket = create(false);
			outportstate.send(statepacket);
/*			
			statepacket = create(false);
			outportstatelog.send(statepacket);
			
			Packet energypacket;
			for(int i = 0; i<10;++i){
				energypacket = create(i+"");
				outportdata.send(energypacket);
			}
			carpacket = create(car+"");
			outportcarlog.send(carpacket);
			
			
			
			statepacket = create(true);
			outportstatelog.send(statepacket);
	*/		
			statepacket = create(true);
			outportstate.send(statepacket);
			
			Packet commandpacket;
			boolean freestate;
			while((commandpacket = inportcommand.receive())!=null){
				freestate = (Boolean)commandpacket.getContent();
				if(!freestate){
					drop(commandpacket);
					continue;
				}
				else{ //if true, then free, it can send the car forward
					drop(commandpacket);
					carpacket = create(car+"");
					outportcar.send(carpacket);
				}
			}
		}
	}

	@Override
	protected void openPorts() {
		// TODO Auto-generated method stub
		inportcar = openInput("CAR");
		inportcommand = openInput("COMMAND");
		inportto = openInput("TO");
		inportfrom = openInput("FROM");
		
		outportcar = openOutput("CAR");
/*		outportcarlog = openOutput("CARLOG");
		outportdata = openOutput("SENSORDATA");
*/
		outportstate = openOutput("STATE");
//		outportstatelog = openOutput("STATELOG");

	}

}
