package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


@ComponentDescription("Generate stream of packets from I/O file")
@OutPort(value = "OUT", description = "Generated packets", type = HashMap.class)
@InPort(value = "LINES", description = "list of list of words", type = ArrayList.class)

public class CalculateSpread extends Component {
    private InputPort lines;
    private OutputPort outport;

    @Override
    protected void execute() {
        Packet rp = lines.receive();
        if (rp == null) {
            return;
        }
        lines.close();

        ArrayList<String[]> listOfWordsList = (ArrayList<String[]>)rp.getContent();
        drop(rp);

        HashMap<String, Integer> Days = new HashMap<String, Integer>();

        for(int i = 0; i < listOfWordsList.size();i++){
            String[] line = listOfWordsList.get(i);

            int minTemp, maxTemp;
            if(line.length > 0){
                String key = line[0].trim();

                try {
                    maxTemp = Integer.parseInt(line[1].replace("*", "").trim());
                    minTemp = Integer.parseInt(line[2].replace("*", "").trim());
                } catch (Exception e){
                    //System.out.println("fault in parseint temp " + i);
                    continue;
                }

                // add to return values array
                Days.put(key, (maxTemp - minTemp));
            }
        }

        Packet pOut = create(Days);
        outport.send(pOut);
    }

    @Override
    protected void openPorts() {
        lines = openInput("LINES");
        outport = openOutput("OUT");
    }
}
