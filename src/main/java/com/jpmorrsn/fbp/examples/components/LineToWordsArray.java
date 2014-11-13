package com.jpmorrsn.fbp.examples.components;

import com.jpmorrsn.fbp.engine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Component to read data from a file, generating a stream of packets. The file
 * name is specified as a String via an InitializationConnection.
 * This component converts the specified format (if one is specified) to Unicode.
 */
@ComponentDescription("Generate stream of packets from I/O file")
@OutPort(value = "OUT", description = "Generated packets", type = ArrayList.class)
@InPort(value = "LINES", description = "line of words", type = String.class)

// filename [, format ]
public class LineToWordsArray extends Component {
    private OutputPort outport;
    private InputPort inport;

    @Override
    protected void execute() {
        Packet pIn;


        ArrayList<String[]> arrayOfLines = new ArrayList<String[]>();
        while ((pIn = inport.receive()) != null) {
            String w = (String) pIn.getContent();
            drop(pIn);

            // Get words for this record
            String[] words = w.split(" ");

            // filter not valid words
            ArrayList<String> validWords = new ArrayList<String>();
            for(int i = 0; i< words.length;i++)
            {
                if(words[i].equals(""))
                    continue;

                validWords.add(words[i].trim());
            }

            arrayOfLines.add(validWords.toArray(new String[validWords.size()]));
        }

        // Send words as individual packets
        Packet pOut = create(arrayOfLines);
        outport.send(pOut);
    }

    @Override
    protected void openPorts() {
        inport = openInput("LINES");
        outport = openOutput("OUT");
    }
}

