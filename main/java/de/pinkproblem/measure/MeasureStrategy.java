package de.pinkproblem.measure;

import android.content.Context;

/**
 * Created by Iris on 23.06.2015.
 */
public abstract class MeasureStrategy {

    Context context;
    String[] columnNames;
    //description
    String strategyTag;

    public MeasureStrategy(Context c) {
        context = c;
    }

    public abstract String getLine(int rssi);

    public String getHeaderLine() {
        String res = "";
        for (int i = 0; i < columnNames.length; i++) {
            res += columnNames[i];
            res += ";";
        }
        return res +"\n";
    }
}
