package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.util.*;
import java.util.Comparator;

@ComponentDescription("Generate stream of packets from I/O file")
@OutPort(value = "OUT", description = "Generated packets", type = LinkedHashMap.class)
@InPort(value = "LINES", description = "list of list of words", type = HashMap.class)

public class SortMap extends Component {
    private InputPort lines;
    private OutputPort outport;

    @Override
    protected void execute() {
        Packet rp = lines.receive();
        if (rp == null) {
            return;
        }
        lines.close();

        HashMap<String, Integer> unsortedMap = (HashMap<String, Integer>)rp.getContent();
        drop(rp);

        ArrayList<String> keys = new ArrayList(unsortedMap.keySet());

        final HashMap<String, Integer> compared = unsortedMap;

        Collections.sort(keys, new Comparator() {
            @Override
            public int compare(Object left, Object right) {
                Integer leftValue = compared.get(left);
                Integer rightValue = compared.get(right);

                return leftValue.compareTo(rightValue);
            }
        });

        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (String k : keys) {
            result.put(k, compared.get(k));
        }

        Packet pOut = create(result);
        outport.send(pOut);
    }

    @Override
    protected void openPorts() {
        lines = openInput("LINES");
        outport = openOutput("OUT");
    }
}
