package umlsequence;

import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashSet;
import java.util.Set;

public class MyUmlLifeLine {
    private String id;
    private String name;
    // 保存发送到这个对象的消息
    private Set<String> messageSet;
    
    public MyUmlLifeLine(String id, String name) {
        this.id = id;
        this.name = name;
        messageSet = new HashSet<>();
    }
    
    public void addMessage(UmlMessage message) {
        // 这是incoming消息
        messageSet.add(message.getId());
    }
    
    public int getIncomingMessageCount() {
        return messageSet.size();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
