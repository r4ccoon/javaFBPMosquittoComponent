package simulator.network;

import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.Network;
import simulator.components.*;

public class SimulatorNetwork extends Network {

    @Override
    protected void define() throws Exception {
        // car generator
        component("carGenerator", CarGenerator.class);
        // state manager
        component("stateManager", StateManager.class);
        // robots
        component("robot1", Robot.class);
        component("robot2", Robot.class);
        component("robot3", Robot.class);
        component("robot4", Robot.class);

        // car generator to 1st robot
        initialize("10", component("carGenerator"), port("INIT"));
        connect(component("carGenerator"), port("CAR"), component("robot1"), port("CAR"));

        // robots inter connections
        connect(component("robot1"), port("CAR"), component("robot2"), port("CAR"));
        connect(component("robot2"), port("CAR"), component("robot3"), port("CAR"));
        connect(component("robot3"), port("CAR"), component("robot4"), port("CAR"));
        connect(component("robot4"), port("CAR"), component("Write", WriteToConsole.class), port("IN"));

        // state manager connections
        connect(component("stateManager"), port("GENERATE"), component("carGenerator"), port("STATE"));

        // robots to state manager
        connect(component("robot1"), port("STATE"), component("stateManager"), port("STATE1"));
        connect(component("robot2"), port("STATE"), component("stateManager"), port("STATE2"));
        connect(component("robot3"), port("STATE"), component("stateManager"), port("STATE3"));
        connect(component("robot4"), port("STATE"), component("stateManager"), port("STATE4"));

        // state manager to robots
        connect(component("stateManager"), port("MOVE1"), component("robot1"), port("COMMAND"));
        connect(component("stateManager"), port("MOVE2"), component("robot2"), port("COMMAND"));
        connect(component("stateManager"), port("MOVE3"), component("robot3"), port("COMMAND"));
        connect(component("stateManager"), port("MOVE4"), component("robot4"), port("COMMAND"));

        // robot to postbot
        component("postBot1", PostBotPower.class);
        component("postBot2", PostBotPower.class);
        component("postBot3", PostBotPower.class);
        component("postBot4", PostBotPower.class);
        // robot to postbots state data
        connect(component("robot1"), port("STATELOG"), component("postBot1"), port("STATE"));
        connect(component("robot2"), port("STATELOG"), component("postBot2"), port("STATE"));
        connect(component("robot3"), port("STATELOG"), component("postBot3"), port("STATE"));
        connect(component("robot4"), port("STATELOG"), component("postBot4"), port("STATE"));
        // robot to postbots send power data
        connect(component("robot1"), port("SENSORDATA"), component("postBot1"), port("POWER"));
        connect(component("robot2"), port("SENSORDATA"), component("postBot2"), port("POWER"));
        connect(component("robot3"), port("SENSORDATA"), component("postBot3"), port("POWER"));
        connect(component("robot4"), port("SENSORDATA"), component("postBot4"), port("POWER"));
        // car log
        connect(component("robot1"), port("CARLOG"), component("postBot1"), port("CARID"));
        connect(component("robot2"), port("CARLOG"), component("postBot2"), port("CARID"));
        connect(component("robot3"), port("CARLOG"), component("postBot3"), port("CARID"));
        connect(component("robot4"), port("CARLOG"), component("postBot4"), port("CARID"));

        // mosquittos..
        component("mos", Mosquitto.class);
        // mosquittos topic
        connect(component("postBot1"), port("TOPIC"), component("mos"), port("TOPIC"));
        connect(component("postBot2"), port("TOPIC"), component("mos"), port("TOPIC"));
        connect(component("postBot3"), port("TOPIC"), component("mos"), port("TOPIC"));
        connect(component("postBot4"), port("TOPIC"), component("mos"), port("TOPIC"));
        // mosquittos content
        connect(component("postBot1"), port("CONTENT"), component("mos"), port("CONTENT"));
        connect(component("postBot2"), port("CONTENT"), component("mos"), port("CONTENT"));
        connect(component("postBot3"), port("CONTENT"), component("mos"), port("CONTENT"));
        connect(component("postBot4"), port("CONTENT"), component("mos"), port("CONTENT"));
    }

    public static void main(final String[] argv) throws Exception {
        new SimulatorNetwork().go();
    }
}
