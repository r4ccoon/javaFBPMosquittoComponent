package simulator.components;

import java.util.ArrayList;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InPorts;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutPorts;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * Component to collate two or more streams of packets, based on a list of
 * control field lengths
 */
@ComponentDescription("Collate two or more streams, based on a list of control field lengths ")
@OutPorts({ @OutPort(value = "CAR") })
@InPorts({ @InPort(value = "INIT"), @InPort(value = "STATE") })
public class CarGenerator extends Component {

	static final String copyright = " ";

	private OutputPort outport;

	private InputPort inportiip;
	private InputPort inportstate;

	private int counter = 0;
	private boolean firsttime = true;

	@Override
	protected void execute() {

		Packet ip;

		if (firsttime) {
			ip = inportiip.receive();
			counter = Integer.parseInt((String) ip.getContent());

			Packet out = create(counter + "");
			outport.send(out);

			drop(ip);

			firsttime = false;

			counter--;
		}

		boolean isFree = false;

		if (counter > 0) {
			while ((ip = inportstate.receive()) != null) {
				isFree = (Boolean) ip.getContent();
				if (isFree) {
					Packet out = create(counter + "");
					outport.send(out);

					drop(ip);

					counter--;
				}
			}
		}
	}

	@Override
	protected void openPorts() {

		inportiip = openInput("INIT");
		inportstate = openInput("STATE");

		outport = openOutput("CAR");

	}

}