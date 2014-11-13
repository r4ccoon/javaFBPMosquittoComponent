package simulator.components;

import com.jpmorrsn.fbp.engine.*;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * go to
 * https://github.com/r4ccoon/javaFBPMosquittoComponent
 * to get the PAHO mosquittos jar files (java libs that are needed)
 *
 * to install mosquitto, on osx-> brew install mosquitto
 * on windows and linux , http://mosquitto.org/download/ and follow the instructions
 */

@ComponentDescription("publish a message to the mosquitto server")

@InPorts({
    @InPort(value = "CLIENTID", description = "client id of the client, can put random string here", type = String.class, optional = true),
    @InPort(value = "CONTENT", description = "the contents", type = String.class),
    @InPort(value = "TOPIC", description = "topic", type = String.class)
})

public class Mosquitto extends Component{

    private InputPort clientId;
    private InputPort content;
    private InputPort topic;

    private OutputPort outport;

    // some default values, will be overridden later
    private String _topic        = "heartbeat";
    private String _content      = "beat";
    private String _clientId     = "client_id_";

    // some values that is not necessary to change
    private int qos             = 2;
    private String broker       = "tcp://localhost:1883";

    @Override
    protected void execute() throws Exception {

        //
        _clientId = (String) fillInput(clientId, _clientId);
        Packet pC;
        while ((pC = content.receive()) != null){
            Packet pT = checkInput(topic);

            _topic    = (String) pT.getContent();
            _content  = (String) pC.getContent();

            drop(pT);
            drop(pC);

            try {
                MemoryPersistence persistence = new MemoryPersistence();

                // connect to mqtt server, make sure that the server is on
                System.out.println("Connecting to broker: " + broker);
                MqttClient sampleClient = new MqttClient(broker, _clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                sampleClient.connect(connOpts);

                // set up topic and publishing the message
                System.out.println("Connected");
                System.out.println(_topic + " is Publishing message: " + _content);
                MqttMessage message = new MqttMessage(_content.getBytes());
                message.setQos(qos);
                sampleClient.publish(_topic, message);

                // disconnect from the server
                sampleClient.disconnect();
                System.out.println("Disconnected");

            } catch(MqttException me) {
                System.out.println("reason "+me.getReasonCode());
                System.out.println("msg "+me.getMessage());
                System.out.println("loc "+me.getLocalizedMessage());
                System.out.println("cause "+me.getCause());
                System.out.println("excerpt "+me);
                me.printStackTrace();
            }
        }
    }

    @Override
    protected void openPorts() {
        clientId = openInput("CLIENTID");
        content =  openInput("CONTENT");
        topic =  openInput("TOPIC");
    }

    public Packet checkInput(InputPort inPort) throws Exception {
        Packet rp = inPort.receive();
        if (rp == null) {
            return null;
        }

        return rp;
    }

    public Object fillInput(InputPort inPort, Object defaultValue){
        Packet rp = inPort.receive();
        if (rp == null) {
            return defaultValue;
        }

        Object ret = rp.getContent();
        drop(rp);

        return ret;
    }
}
