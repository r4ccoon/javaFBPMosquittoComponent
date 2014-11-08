package simulator.network;

import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;
import simulator.components.Mosquitto;

/**
 * example of how to use the mosquitto javafbp component
 */

public class MosquittoJavaFBPExample extends Network {

    @Override
    protected void define() throws Exception {
        connect(component("mos", Mosquitto.class), port("OUT"), component("Write", WriteToConsole.class), port("IN"));
        initialize("r1/state", component("mos"), port("TOPIC"));
        initialize("TRUE", component("mos"), port("CONTENT"));

        connect(component("mos2", Mosquitto.class), port("OUT"), component("Write", WriteToConsole.class), port("IN"));
        initialize("r1/power", component("mos"), port("TOPIC"));
        initialize("125", component("mos"), port("CONTENT"));
    }

    public static void main(final String[] argv) throws Exception {
        new MosquittoJavaFBPExample().go();
    }
}
