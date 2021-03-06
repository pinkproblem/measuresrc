package de.pinkproblem.measure;

import android.content.Context;

/**
 * Created by Iris on 23.06.2015.
 */
public abstract class MeasureStrategy {

    public static final String separator = ";";

    Context context;
    String[] columnNames;
    //description
    String strategyTag;

    public MeasureStrategy(Context c) {
        context = c;
    }

    public abstract String getLine(int rssi);

    public String getLine(String address, int rssi) {
        return "";
    }

    //called on file close; for final calculation stuff
    public String getLines() {
        return "";
    }

    //if the strategy can do some kind of evaluation
    public double getResult() {
        return 0;
    }

    public String getHeaderLine() {
        String res = "";
        for (int i = 0; i < columnNames.length; i++) {
            res += columnNames[i];
            res += separator;
        }
        return res +"\n";
    }
}
