package simulator.components;

import com.jpmorrsn.fbp.engine.*;

@InPorts({
        @InPort(value = "CAR"),
})

public class Garage extends Component {
    private InputPort inPortCar;
    @Override
    protected void execute() throws Exception {
        Packet pInCar;
        while ((pInCar = inPortCar.receive()) != null) {
            Integer carSerialNumber =  (Integer)pInCar.getContent();

            //
            System.out.println("car " + carSerialNumber + " is finished");

            // drop so next car can come in
            drop(pInCar);
        }
    }

    @Override
    protected void openPorts() {
        inPortCar = openInput("CAR");
    }
}
