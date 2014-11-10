package simulator.components;

import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

/**
 * Created by rpl on 10/11/14.
 */
public class Helper {

    public static Object checkInput(InputPort inPort) throws Exception {
        Packet rp = inPort.receive();
        if (rp == null) {
            return null;
        }

        inPort.close();
        return rp.getContent();
    }

    /**
     * return the packet content if not null, otherwise return the default value
     *
     * @param inPort
     * @param defaultValue
     * @return
     */
    public static Object fillInput(InputPort inPort, Object defaultValue){
        Packet rp = inPort.receive();
        if (rp == null) {
            return defaultValue;
        }

        inPort.close();
        return rp.getContent();
    }
}
