package com.jpmorrsn.fbp.examples.networks;


import com.jpmorrsn.fbp.components.ReadFile;
import com.jpmorrsn.fbp.engine.Network;
import com.jpmorrsn.fbp.examples.components.*;

import java.io.File;

public class Weather extends Network {
    @Override
    protected void define() {
        connect(component("Read", ReadFile.class), port("OUT"), component("LineToWordsArray", LineToWordsArray.class), port("LINES"));
        connect(component("LineToWordsArray"), port("OUT"), component("FilteredArray", FilteredArray.class), port("LINES"));
        connect(component("FilteredArray"), port("OUT"), component("CalculateSpread", CalculateSpread.class), port("LINES"));
        connect(component("CalculateSpread"), port("OUT"), component("SortMap", SortMap.class), port("LINES"));
        connect(component("SortMap"), port("OUT"), component("WriteMapToConsole", WriteMapToConsole.class), port("LINES"));

        initialize("weather.dat".replace("/", File.separator), component("Read"), port("SOURCE"));
        initialize("0,1,2", component("FilteredArray"), port("INDEXES"));
    }

    public static void main(final String[] argv) throws Exception {
        new Weather().go();
    }
}
