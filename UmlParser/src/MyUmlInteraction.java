import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import umlclass.ClassQuery;
import umlsequence.SeqQuery;
import umlstate.StateQuery;

import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlGeneralInteraction {
    private ClassQuery classQuery;
    private StateQuery stateQuery;
    private SeqQuery seqQuery;
    
    public MyUmlInteraction(UmlElement... elements) {
        classQuery = new ClassQuery(elements);
        stateQuery = new StateQuery(elements);
        seqQuery = new SeqQuery(elements);
    }
    
    @Override
    public int getClassCount() {
        return classQuery.getClassCount();
    }
    
    @Override
    public int getClassOperationCount(
            String s, OperationQueryType operationQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getClassOperationCount(s, operationQueryType);
    }
    
    @Override
    public int getClassAttributeCount(
            String s, AttributeQueryType attributeQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getClassAttributeCount(s, attributeQueryType);
    }
    
    @Override
    public int getClassAssociationCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getClassAssociationCount(s);
    } // 关联了多少个对象
    
    @Override
    public List<String> getClassAssociatedClassList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getClassAssociatedClassList(s);
    } // 关联到哪些类
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getClassOperationVisibility(s, s1);
    }
    
    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return classQuery.getClassAttributeVisibility(s, s1);
    }
    
    @Override
    public String getTopParentClass(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getTopParentClass(s);
    }
    
    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classQuery.getImplementInterfaceList(s);
    }
    
    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassDuplicatedException, ClassNotFoundException {
        return classQuery.getInformationNotHidden(s);
    }
    
    @Override
    public int getParticipantCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return seqQuery.getParticipantCount(s);
        
    }
    
    @Override
    public int getMessageCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return seqQuery.getMessageCount(s);
    }
    
    @Override
    public int getIncomingMessageCount(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return seqQuery.getIncomingMessageCount(s, s1);
    }
    
    @Override
    public void checkForUml002() throws UmlRule002Exception {
        //不能含有重名成员
        classQuery.checkForUml002();
    }
    
    @Override
    public void checkForUml008() throws UmlRule008Exception {
        // 不能含有循环继承
        classQuery.checkForUml008();
    }
    
    @Override
    public void checkForUml009() throws UmlRule009Exception {
        // 接口不能重复继承
        classQuery.checkForUml009();
    }
    
    @Override
    public int getStateCount(String s) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateQuery.getStateCount(s);
    }
    
    @Override
    public int getTransitionCount(String s) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateQuery.getTransitionCount(s);
    }
    
    @Override
    public int getSubsequentStateCount(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateQuery.getSubsequentStateCount(s, s1);
    }
}
