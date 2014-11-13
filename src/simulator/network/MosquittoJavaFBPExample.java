package simulator.network;

import com.jpmorrsn.fbp.components.Passthru;
import com.jpmorrsn.fbp.engine.Network;
import simulator.components.Mosquitto;
import simulator.components.PostBotPower;
import simulator.components.PostBotState;
import simulator.components.PowerManager;

/**
 * example of how to use the mosquitto javafbp component
 */

public class MosquittoJavaFBPExample extends Network {

    @Override
    protected void define() throws Exception {
        component("mos", Mosquitto.class);
        for(int i = 1;i < 5; i++)
        {
            component("passThru" + i + "t", Passthru.class);
            component("passThru" + i + "c", Passthru.class);
            component("passThru" + i + "st", Passthru.class);
            component("passThru" + i + "sc", Passthru.class);

            component("powerManager" + i, PowerManager.class);
            component("/r" +i+ "/power", PostBotPower.class);
            component("/r" +i+ "/state", PostBotState.class);
            initialize(1, component("powerManager" + i), port("RANGEBEGIN"));
            initialize(10, component("powerManager" +i ), port("RANGEEND"));

            // robot to postbots send power data
            connect(component("powerManager" +i), port("SENSORDATA"), component("/r" +i+ "/power"), port("IN"));
            // robot to postbots send state data
            connect(component("powerManager" +i), port("STATE"),      component("/r" +i+ "/state"), port("IN"));

            // mosquitto connection
            connect(component("/r" +i+ "/power"), port("TOPIC"),   component("passThru" +i+ "t"), port("IN"));
            connect(component("/r" +i+ "/power"), port("CONTENT"), component("passThru" +i+ "c"), port("IN"));
            connect(component("passThru"  +i+ "t"), port("OUT"),   component("mos"), port("TOPIC"));
            connect(component("passThru"  +i+ "c"), port("OUT"),   component("mos"), port("CONTENT"));

            connect(component("/r"  +i+ "/state"), port("TOPIC"),   component("passThru"  +i+ "st"), port("IN"));
            connect(component("/r"  +i+ "/state"), port("CONTENT"), component("passThru"  +i+ "sc"), port("IN"));
            connect(component("passThru" +i+ "st"), port("OUT"),   component("mos"), port("TOPIC"));
            connect(component("passThru" +i+ "sc"), port("OUT"),   component("mos"), port("CONTENT"));
             // turn this things on
            initialize(true, component("powerManager" + i), port("SWITCH"));
            initialize(10000, component("powerManager" + i), port("PROCESSTIME"));
        } 
    }

    public static void main(final String[] argv) throws Exception {
        new MosquittoJavaFBPExample().go();
    }
}
