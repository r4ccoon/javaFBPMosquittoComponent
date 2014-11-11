package simulator.network;

import com.jpmorrsn.fbp.components.Passthru;
import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;
import simulator.components.Garage;
import simulator.components.PowerManager;
import simulator.components.RCarGenerator;

public class RSimulatorNetwork extends Network {

    @Override
    protected void define() throws Exception {
        // car generator
        component("carGenerator", RCarGenerator.class);

        // robots
        component("robot1", RRobot.class);
        component("robot2", RRobot.class);
        component("robot3", RRobot.class);
        component("robot4", RRobot.class);

        initialize("robot1", component("robot1"), port("ROBOTNAME"));
        initialize("robot2", component("robot2"), port("ROBOTNAME"));
        initialize("robot3", component("robot3"), port("ROBOTNAME"));
        initialize("robot4", component("robot4"), port("ROBOTNAME"));

        // the proces time in ms
        initialize(3000, component("robot1"), port("PROCESSTIME"));
        initialize(4000, component("robot2"), port("PROCESSTIME"));
        initialize(5000, component("robot3"), port("PROCESSTIME"));
        initialize(6000, component("robot4"), port("PROCESSTIME"));

        component("powerManager1", PowerManager.class);
        component("powerManager2", PowerManager.class);
        component("powerManager3", PowerManager.class);
        component("powerManager4", PowerManager.class);

        // initialize range
        initialize( 1 , component("powerManager1"), port("RANGEBEGIN"));
        initialize(10 , component("powerManager1"), port("RANGEEND"));
        initialize(20 , component("powerManager2"), port("RANGEBEGIN"));
        initialize(100, component("powerManager2"), port("RANGEEND"));
        initialize(20 , component("powerManager3"), port("RANGEBEGIN"));
        initialize(100, component("powerManager3"), port("RANGEEND"));
        initialize(20 , component("powerManager4"), port("RANGEBEGIN"));
        initialize(100, component("powerManager4"), port("RANGEEND"));

        // car generator to 1st robot
        connect(component("carGenerator"), port("CAR"), component("robot1"), port("CAR"));

        // 1st robot to car generator
        // it use passthru because somehow error if not used
        component("Passthru", Passthru.class);
        initialize(Boolean.TRUE, component("Passthru"), port("IN"));
        connect(component("Passthru"), port("OUT"), component("carGenerator"), port("COMMAND"));

        connect(component("robot1"), port("COMMAND"), component("carGenerator"), port("COMMAND"));
        connect(component("robot2"), port("COMMAND"), component("robot1"), port("COMMAND"));
        connect(component("robot3"), port("COMMAND"), component("robot2"), port("COMMAND"));
        connect(component("robot4"), port("COMMAND"), component("robot3"), port("COMMAND"));

        // robots inter connections
        connect(component("robot1"), port("CAR"), component("robot2"), port("CAR"));
        connect(component("robot2"), port("CAR"), component("robot3"), port("CAR"));
        connect(component("robot3"), port("CAR"), component("robot4"), port("CAR"));
        connect(component("robot4"), port("CAR"), component("Finished", Garage.class), port("CAR"));

        // robots to power manager
        connect(component("robot1"), port("STATELOG"), component("powerManager1"), port("SWITCH"));
        connect(component("robot2"), port("STATELOG"), component("powerManager2"), port("SWITCH"));
        connect(component("robot3"), port("STATELOG"), component("powerManager3"), port("SWITCH"));
        connect(component("robot4"), port("STATELOG"), component("powerManager4"), port("SWITCH"));
    }

    public static void main(final String[] argv) throws Exception {
        new RSimulatorNetwork().go();
    }

}
