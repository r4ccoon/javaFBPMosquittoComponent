package simulator.network;

import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;
import simulator.components.Mosquitto;

public class Simulator extends Network {

    @Override
    protected void define() throws Exception {
        connect(component("mos", Mosquitto.class), port("OUT"), component("Write", WriteToConsole.class), port("IN"));
        initialize("r1/state", component("mos"), port("TOPIC"));
        initialize("TRUE", component("mos"), port("CONTENT"));

    }

    public static void main(final String[] argv) throws Exception {
        new Simulator().go();
    }
}
