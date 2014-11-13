package com.jpmorrsn.fbp.components;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;


/**
 * Component to read data from a file, generating a stream of packets. The file
 * name is specified as a String via an InitializationConnection.
 * This component converts the specified format (if one is specified) to Unicode.
 */
@ComponentDescription("Generate stream of packets from I/O file")
@OutPort(value = "OUT", description = "Generated packets", type = String.class)
@InPort(value = "SOURCE", description = "File name and optional format, separated by a comma", type = String.class)
// filename [, format ]
public class ReadFile extends Component {

  static final String copyright = "Copyright 2007, 2008, 2012, J. Paul Morrison.  At your option, you may copy, "
      + "distribute, or make derivative works under the terms of the Clarified Artistic License, "
      + "based on the Everything Development Company's Artistic License.  A document describing "
      + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. "
      + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

  private OutputPort outport;

  private InputPort source;

  @Override
  protected void execute() {
    Packet rp = source.receive();
    if (rp == null) {
      return;
    }
    source.close();

    String sf = (String) rp.getContent();
    String format = null;
    int i = sf.indexOf(",");
    if (i != -1) {
      format = sf.substring(i + 1);
      format = format.trim();
      sf = sf.substring(0, i);
    }

    try {
      drop(rp);
      FileInputStream in = new FileInputStream(new File(sf));
      BufferedReader b = null;
      if (format == null) {
        b = new BufferedReader(new InputStreamReader(in));
      } else {
        b = new BufferedReader(new InputStreamReader(in, format));
      }

      String s;
      while ((s = b.readLine()) != null) {
        Packet p = create(s);
        if (outport.isClosed()) {
          break;
        }

        outport.send(p);
      }
      b.close();
    } catch (IOException e) {
      System.out.println(e.getMessage() + " - file: " + sf + " - component: " + this.getName());
    }
  }

  @Override
  protected void openPorts() {

    outport = openOutput("OUT");

    source = openInput("SOURCE");

  }
}
