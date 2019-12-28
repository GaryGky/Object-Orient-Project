package umlstate;

import com.oocourse.uml2.models.common.ElementType;

public class MyFinalState extends MyUmlState {
    private ElementType type;
    
    MyFinalState(String id, String name) {
        super(id, name);
        type = ElementType.UML_FINAL_STATE;
    }
    
    public ElementType getType() {
        return type;
    }
}
