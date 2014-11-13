package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ComponentDescription("Generate stream of packets from I/O file")
@OutPort(value = "OUT", description = "Generated packets", type = HashMap.class)
@InPort(value = "LINES", description = "list of list of words", type = HashMap.class)

public class CalculateDifference extends Component {
    private InputPort lines;
    private OutputPort outport;

    @Override
    protected void execute() {
        Packet rp = lines.receive();
        if (rp == null) {
            return;
        }
        lines.close();

        HashMap<String, Integer> differences = (HashMap<String, Integer>) rp.getContent();
        drop(rp);

        for(Map.Entry<String, Integer> entry: differences.entrySet()){
            if(entry.getValue() < 0)
                entry.setValue(entry.getValue() * -1);
        }

        Packet pOut = create(differences);
        outport.send(pOut);
    }

    @Override
    protected void openPorts() {
        lines = openInput("LINES");
        outport = openOutput("OUT");
    }
}
