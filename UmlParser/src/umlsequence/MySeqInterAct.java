package umlsequence;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;

public class MySeqInterAct {
    private String id;
    private String name;
    private HashMap<String, UmlMessage> id2Messege; // 统计有多少条消息
    private HashMap<String, MyUmlLifeLine> id2MyLifeline; // 统计有多少个对象
    private HashMap<String, MyUmlLifeLine> name2Mylifeline; // 名称映射到lifeline
    private HashMap<String, Integer> name2LifeLineRef; // 名称映射到次数
    private HashMap<String, UmlLifeline> id2Lifeline;
    
    MySeqInterAct(String id, String name) {
        this.id = id;
        this.name = name;
        id2MyLifeline = new HashMap<>();
        id2Messege = new HashMap<>();
        name2LifeLineRef = new HashMap<>();
        name2Mylifeline = new HashMap<>();
        id2Lifeline = new HashMap<>();
        update = false;
    }
    
    void addLifeLine(UmlLifeline lifeline) {
        // 添加LifeLine
        MyUmlLifeLine myUmlLifeLine = new MyUmlLifeLine(lifeline.getId(),
                lifeline.getName());
        id2MyLifeline.put(lifeline.getId(), myUmlLifeLine);
        id2Lifeline.put(lifeline.getId(), lifeline);
        name2Mylifeline.put(lifeline.getName(), myUmlLifeLine);
        if (!name2LifeLineRef.containsKey(lifeline.getName())) {
            name2LifeLineRef.put(lifeline.getName(), 1);
        } else {
            name2LifeLineRef.put(lifeline.getName(),
                    name2LifeLineRef.get(myUmlLifeLine.getName()) + 1);
        }
    }
    
    void addEndpoint(UmlEndpoint endpoint) {
        MyUmlLifeLine myUmlLifeLine = new MyUmlLifeLine(
                endpoint.getId(), endpoint.getName());
        id2MyLifeline.put(endpoint.getId(), myUmlLifeLine);
    }
    
    void addMessage(UmlMessage message) {
        // 添加消息
        // 同时为lifeline添加消息
        // 消息可能比对象先出现
        id2Messege.put(message.getId(), message);
    }
    
    protected void updateMessage() {
        for (UmlMessage message : id2Messege.values()) {
            String target = message.getTarget();
            id2MyLifeline.get(target).addMessage(message);
        }
    }
    
    int getParticipantCount() {
        return id2Lifeline.size();
    }
    
    int getMessageCount() {
        return id2Messege.size();
    }
    
    private boolean update;
    
    int getIncomingMessageCount(String llName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        if (name2LifeLineRef.getOrDefault(llName, 0) == 0) {
            throw new LifelineNotFoundException(this.name, llName);
        } else if (name2LifeLineRef.get(llName) > 1) {
            throw new LifelineDuplicatedException(this.name, llName);
        } else {
            if (!update) {
                update = true;
                updateMessage();
            }
            return name2Mylifeline.get(llName).getIncomingMessageCount();
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
}
