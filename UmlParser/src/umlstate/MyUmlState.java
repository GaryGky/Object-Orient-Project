package umlstate;

import java.util.HashMap;

public class MyUmlState {
    private String id;
    private String name;
    private HashMap<String, MyUmlState> id2SubState; // 该状态的后继状态
    
    MyUmlState(String id, String name) {
        this.id = id;
        this.name = name;
        id2SubState = new HashMap<>();
    }
    
    void addSubState(MyUmlState state) {
        // 一次加一条
        id2SubState.put(state.getId(), state);
    }
    
    void addSubState(HashMap<String, MyUmlState> map) {
        // 一次将所有后继状态的后继状态添加
        id2SubState.putAll(map);
    }
    
    int getSubsequentStateCount() {
        // 记录有多少个后继状态
        return id2SubState.size();
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    HashMap<String, MyUmlState> getId2SubState() {
        return id2SubState;
    }
}
