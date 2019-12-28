package umlsequence;

import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;

public class SeqQuery implements UmlCollaborationInteraction {
    private HashMap<String, Integer> name2InterActRef;
    private HashMap<String, MySeqInterAct> name2MyInterAct;
    private HashMap<String, MySeqInterAct> id2MyInterAct;
    
    public SeqQuery(UmlElement... elements) {
        name2InterActRef = new HashMap<>();
        name2MyInterAct = new HashMap<>();
        id2MyInterAct = new HashMap<>();
        setUpSeq(elements);
    }
    
    private void setUpSeq(UmlElement... elements) {
        for (UmlElement element : elements) {
            String parent = element.getParentId();
            switch (element.getElementType()) {
                case UML_INTERACTION:
                    MySeqInterAct interAct = new MySeqInterAct(
                            element.getId(), element.getName());
                    id2MyInterAct.put(interAct.getId(), interAct);
                    name2MyInterAct.put(element.getName(), interAct);
                    if (!name2InterActRef.containsKey(element.getName())) {
                        name2InterActRef.put(interAct.getName(), 1);
                    } else {
                        name2InterActRef.put(interAct.getName(),
                                name2InterActRef.get(interAct.getName()) + 1);
                    }
                    break;
                case UML_LIFELINE:
                    id2MyInterAct.get(parent).
                            addLifeLine((UmlLifeline) element);
                    break;
                case UML_MESSAGE:
                    id2MyInterAct.get(parent).addMessage((UmlMessage) element);
                    break;
                case UML_ENDPOINT:
                    id2MyInterAct.get(parent).addEndpoint((
                            UmlEndpoint) element);
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public int getParticipantCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2MyInterAct.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        } else if (name2InterActRef.get(s) > 1) {
            throw new InteractionDuplicatedException(s);
        } else {
            return name2MyInterAct.get(s).getParticipantCount();
        }
    }
    
    @Override
    public int getMessageCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2MyInterAct.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        } else if (name2InterActRef.get(s) > 1) {
            throw new InteractionDuplicatedException(s);
        } else {
            return name2MyInterAct.get(s).getMessageCount();
        }
    }
    
    @Override
    public int getIncomingMessageCount(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2MyInterAct.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        } else if (name2InterActRef.get(s) > 1) {
            throw new InteractionDuplicatedException(s);
        } else {
            return name2MyInterAct.get(s).getIncomingMessageCount(s1);
        }
    }
}
