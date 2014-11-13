package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


@ComponentDescription("----")
@InPort(value = "LINES", description = "TreeMap words", type = HashMap.class)

public class WriteMapToConsole extends Component {
    private InputPort lines;

    @Override
    protected void execute() {
        Packet rp = lines.receive();
        if (rp == null) {
            return;
        }
        lines.close();

        HashMap<String, Integer> map = (HashMap<String, Integer>)rp.getContent();
        drop(rp);

        for(Map.Entry<String, Integer> entry: map.entrySet()){
            System.out.print(entry.getKey() ) ;
            System.out.println(" " + entry.getValue());
        }
    }

    @Override
    protected void openPorts() {
        lines = openInput("LINES");
    }
}
