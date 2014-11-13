package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.util.ArrayList;
import java.util.List;

@OutPort(value = "OUT", description = "Generated packets", type = ArrayList.class)

@InPorts({
        @InPort(value = "INDEXES", description = "index of the column you wanna get. separated by comma", type = String.class),
        @InPort(value = "LINES", description = "list of list of words", type = ArrayList.class)
})

public class FilteredArray extends Component{

    private OutputPort outport;

    private InputPort indexes;
    private InputPort lines;

    @Override
    protected void execute() {

        Packet rp = lines.receive();
        if (rp == null) {
            return;
        }
        lines.close();

        ArrayList<String[]> listOfWordsList = (ArrayList<String[]>)rp.getContent();
        drop(rp);

        // get the indexes that we want to process
        String whichColumnToTake;
        {
            Packet indexesPacket = indexes.receive();
            if (indexesPacket == null) {
                return;
            }
            indexes.close();

            whichColumnToTake = (String) indexesPacket.getContent();
            drop(indexesPacket);
        }

        ArrayList<String[]> returnValues = new ArrayList<String[]>();
        String[] wantedIndexes = whichColumnToTake.split(",");

        // filter the array for the wanted index only
        for(int i = 0; i < listOfWordsList.size();i++){
            String[] line = listOfWordsList.get(i);
            ArrayList<String> filteredLine = new ArrayList<String>();
            for(int j = 0; j < wantedIndexes.length; j++)
            {
                try {
                    int wantedIndex = Integer.parseInt(wantedIndexes[j].toString());
                    filteredLine.add(line[wantedIndex]);
                } catch (Exception e){
                    //System.out.println("failed parsing " + wantedIndexes[j]);
                    continue;
                }
            }

            returnValues.add(filteredLine.toArray(new String[filteredLine.size()]));
        }

        Packet pOut = create(returnValues);
        outport.send(pOut);
    }

    @Override
    protected void openPorts() {
        outport = openOutput("OUT");

        lines = openInput("LINES");
        indexes = openInput("INDEXES");
    }
}
