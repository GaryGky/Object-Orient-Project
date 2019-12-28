package umlstate;

import com.oocourse.uml2.models.common.ElementType;

public class MyPseudoState extends MyUmlState {
    private ElementType type;
    
    MyPseudoState(String id, String name) {
        super(id, name);
        type = ElementType.UML_PSEUDOSTATE;
    }
    
    public ElementType getType() {
        return type;
    }
}
