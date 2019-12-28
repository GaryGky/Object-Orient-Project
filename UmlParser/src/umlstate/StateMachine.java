package umlstate;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class StateMachine {
    private String id;
    private String name;
    private HashMap<String, Integer> name2StateRef; // 一个状态被引用的次数
    private HashMap<String, MyUmlState> name2MyState; // 状态名称映射到状态
    private HashMap<String, MyUmlState> id2MyState; // id映射到状态
    // id映射到起始状态
    private final String pseudoStateName;// 作为pseudo的id
    private UmlPseudostate pseudostate;
    // id映射到终止状态
    private UmlFinalState finalState;
    private final String finalStateName;// 作为finalstate的id
    private HashMap<String, UmlTransition> id2Transition; // id映射到迁移
    // id 映射到各种状态：initial，final，state
    private HashMap<String, UmlElement> id2State;
    private Set<String> nameOfFinal;
    private boolean update;
    
    public StateMachine(String id, String name) {
        this.id = id;
        this.name = name;
        update = false;
        name2MyState = new HashMap<>();
        name2StateRef = new HashMap<>();
        id2MyState = new HashMap<>();
        id2Transition = new HashMap<>();
        id2State = new HashMap<>();
        nameOfFinal = new HashSet<>();
        pseudoStateName = "initialState";
        finalStateName = "finalState";
    }
    
    void addState(UmlState state) {
        id2State.put(state.getId(), state);
        MyUmlState myUmlState = new MyUmlState(state.getId(), state.getName());
        name2MyState.put(myUmlState.getName(), myUmlState);
        id2MyState.put(state.getId(), myUmlState);
        if (!name2StateRef.containsKey(state.getName())) {
            name2StateRef.put(state.getName(), 1);
        } else {
            name2StateRef.put(state.getName(),
                    name2StateRef.get(state.getName()) + 1);
        }
    }
    
    void addStartState(UmlPseudostate state) {
        MyUmlState myUmlState;
        if (pseudostate == null) {
            myUmlState = new MyPseudoState(pseudoStateName, state.getName());
            id2MyState.put(pseudoStateName, myUmlState);
            pseudostate = state;
            id2State.put(state.getId(), state);
        } else {
            myUmlState = id2MyState.get(pseudoStateName);
            id2State.put(state.getId(), state);
        }
        addName2Num(myUmlState, state.getName());
    }
    
    void addFinalState(UmlFinalState state) {
        MyUmlState myUmlState;
        if (finalState == null) {
            myUmlState = new MyFinalState(finalStateName, state.getName());
            id2MyState.put(finalStateName, myUmlState);
            finalState = state;
            id2State.put(state.getId(), state);
        } else {
            myUmlState = id2MyState.get(finalStateName);
            id2State.put(state.getId(), state);
        }
        nameOfFinal.add(state.getName());
        addName2Num(myUmlState, state.getName());
    }
    
    private void addName2Num(MyUmlState myUmlState, String name) {
        name2MyState.put(myUmlState.getName(), myUmlState);
        if (!name2StateRef.containsKey(name)) {
            name2StateRef.put(name, 1);
        } else {
            name2StateRef.put(name,
                    name2StateRef.get(name) + 1);
        }
    }
    
    void addTransition(UmlTransition transition) {
        id2Transition.put(transition.getId(), transition);
        UmlElement source = id2State.get(transition.getSource());
        UmlElement target = id2State.get(transition.getTarget());
        MyUmlState source1;
        source1 = getMyUmlState(source);
        MyUmlState target1 = getMyUmlState(target);
        source1.addSubState(target1);
    }
    
    private MyUmlState getMyUmlState(UmlElement source) {
        MyUmlState source1;
        if (source instanceof UmlPseudostate) {
            source1 = id2MyState.get(pseudoStateName);
        } else if (source instanceof UmlFinalState) {
            source1 = id2MyState.get(finalStateName);
        } else {
            source1 = id2MyState.get(source.getId());
        }
        return source1;
    }
    
    protected void setSubState() {
        // 用bfs建立后继关系
        for (MyUmlState state : name2MyState.values()) {
            bfs(state);
        }
    }
    
    private void bfs(MyUmlState start) {
        // 一次性找到所有start可达的状态
        HashMap<String, MyUmlState> subState = new HashMap<>();
        Set<String> vis = new HashSet<>(name2MyState.size());
        Queue<MyUmlState> queue = new LinkedList<>();
        queue.add(start);
        while (queue.size() != 0) {
            MyUmlState state = queue.poll();
            vis.add(state.getId());
            for (MyUmlState state1 : state.getId2SubState().values()) {
                if (!vis.contains(state1.getId())) {
                    queue.add(state1);
                }
                subState.put(state1.getId(), state1);
            }
        }
        start.addSubState(subState);
    }
    
    int getSubsequentStateCount(String name) throws
            StateNotFoundException, StateDuplicatedException {
        if (name2StateRef.get(name) == null) {
            // 抛出未找到异常
            throw new StateNotFoundException(this.name, name);
        } else if (name2StateRef.get(name) > 1) {
            // 抛出重复异常
            throw new StateDuplicatedException(this.name, name);
        } else {
            if (nameOfFinal.contains(name)) {
                return 0;
            }
            if (!update) {
                update = true;
                setSubState();
            }
            return name2MyState.get(name).getSubsequentStateCount();
        }
    }
    
    int getStateCount() {
        return id2MyState.size();
    }
    
    int getTransitionCount() {
        return id2Transition.size();
    }
}
