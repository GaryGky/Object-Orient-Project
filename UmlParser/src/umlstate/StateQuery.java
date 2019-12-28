package umlstate;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;

public class StateQuery implements UmlStateChartInteraction {
    private HashMap<String, StateMachine> id2StateMachine;
    private HashMap<String, StateMachine> name2StateMachine;
    private HashMap<String, Integer> name2StateMachineRef;
    private HashMap<String, UmlRegion> id2Region;
    private HashMap<String, UmlTransition> id2Transition;
    
    public StateQuery(UmlElement... elements) {
        id2StateMachine = new HashMap<>();
        name2StateMachine = new HashMap<>();
        name2StateMachineRef = new HashMap<>();
        id2Region = new HashMap<>();
        id2Transition = new HashMap<>();
        setUpState(elements);
        setUpTransition();
    }
    
    private void setUpState(UmlElement... elements) {
        for (UmlElement element : elements) {
            String regionId = element.getParentId();
            String machineId = null;
            if (id2Region.get(regionId) != null) {
                machineId = id2Region.get(regionId).getParentId();
            }
            switch (element.getElementType()) {
                case UML_STATE_MACHINE:
                    StateMachine stateMachine = new StateMachine(
                            element.getId(), element.getName());
                    id2StateMachine.put(element.getId(), stateMachine);
                    name2StateMachine.put(element.getName(), stateMachine);
                    if (name2StateMachineRef.containsKey(element.getName())) {
                        name2StateMachineRef.put(element.getName(),
                                name2StateMachineRef.get(
                                        element.getName()) + 1);
                    } else {
                        name2StateMachineRef.put(element.getName(), 1);
                    }
                    break;
                case UML_STATE:
                    id2StateMachine.get(machineId).addState((UmlState) element);
                    break;
                case UML_PSEUDOSTATE:
                    id2StateMachine.get(machineId).addStartState(
                            (UmlPseudostate) element);
                    break;
                case UML_FINAL_STATE:
                    id2StateMachine.get(machineId).addFinalState(
                            (UmlFinalState) element);
                    break;
                case UML_TRANSITION:
                    id2Transition.put(element.getId(), (UmlTransition) element);
                    break;
                case UML_REGION:
                    id2Region.put(element.getId(), (UmlRegion) element);
                    break;
                default:
                    break;
            }
        }
    }
    
    private void setUpTransition() {
        for (UmlTransition transition : id2Transition.values()) {
            String regionId = transition.getParentId();
            String machineId = id2Region.get(regionId).getParentId();
            id2StateMachine.get(machineId).addTransition(transition);
        }
    }
    
    public int getSubsequentStateCount(String stateMachine, String state) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateDuplicatedException, StateNotFoundException {
        if (name2StateMachine.get(stateMachine) == null) {
            throw new StateMachineNotFoundException(stateMachine);
        } else if (name2StateMachineRef.get(stateMachine) > 1) {
            throw new StateMachineDuplicatedException(stateMachine);
        } else {
            return name2StateMachine.get(
                    stateMachine).getSubsequentStateCount(state);
        }
    }
    
    public int getStateCount(String stateMachine) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        if (name2StateMachine.get(stateMachine) == null) {
            throw new StateMachineNotFoundException(stateMachine);
        } else if (name2StateMachineRef.get(stateMachine) > 1) {
            throw new StateMachineDuplicatedException(stateMachine);
        } else {
            return name2StateMachine.get(stateMachine).getStateCount();
        }
    }
    
    public int getTransitionCount(String stateMachine) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        if (name2StateMachine.get(stateMachine) == null) {
            throw new StateMachineNotFoundException(stateMachine);
        } else if (name2StateMachineRef.get(stateMachine) > 1) {
            throw new StateMachineDuplicatedException(stateMachine);
        } else {
            return name2StateMachine.get(stateMachine).getTransitionCount();
        }
    }
}
