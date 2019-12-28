package enums;

import java.util.ArrayList;

public enum LiftType {
    A("A"), B("B"), C("C");
    
    private ArrayList<Integer> floors;
    private String type;
    
    LiftType(String s) {
        type = s;
    }
    
    public String getType() {
        return type;
    }
}
