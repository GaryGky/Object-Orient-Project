package umlclass.helper;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssociateHelper {
    private HashMap<String, UmlElement> parentAsso; // id映射到父类的关联
    private HashMap<String, UmlElement> associate; // id -> element
    private HashMap<String, UmlClass> assoClass; // id映射到关联的类
    //关联到多少个END
    private List<UmlAssociationEnd> assoEndList;
    // 关联的对端有哪些“名字”
    // 注意不能使用集合：因为父类和子类可能同时关联到一个对象
    private List<String> assoList;
    private boolean updateList;
    // id映射到类自己的关联（不包括父类）
    private HashMap<String, UmlAssociationEnd> selfAssociate;
    
    public AssociateHelper() {
        parentAsso = new HashMap<>(300);
        assoList = new LinkedList<>();
        associate = new HashMap<>(300);
        assoClass = new HashMap<>(300);
        assoEndList = new LinkedList<>();
        updateList = false;
        selfAssociate = new HashMap<>(300);
    }
    
    public void addAssoEnd(UmlAssociationEnd end) {
        assoEndList.add(end);
        selfAssociate.put(end.getId(), end);
    }
    
    public void addParentAssoEnd(List<UmlAssociationEnd> ends) {
        assoEndList.addAll(ends);
    }
    
    // 注意这里是put id号
    public void addAssociate(UmlElement element) {
        //自关联情况
        if (element.getElementType() == ElementType.UML_CLASS) {
            UmlClass umlClass = (UmlClass) element;
            assoClass.put(umlClass.getId(), umlClass);
        }
        associate.put(element.getId(), element);
    }
    
    // 把父类的关联关系加进来
    public void addParentAssoEle(Map<? extends String,
            ? extends UmlElement> m) {
        parentAsso.putAll(m);
    }
    
    // 把父类关联到的类加进来
    public void addParentAssoClass(Map<? extends String,
            ? extends UmlClass> m) {
        assoClass.putAll(m);
    }
    
    public int getClassAssociationCount() {
        return assoEndList.size();
    }
    
    public List<String> getAssoList() {
        if (!updateList) {
            updateList = true;
            for (UmlClass umlClass : assoClass.values()) {
                assoList.add(umlClass.getName());
            }
        }
        return assoList;
    }
    
    public HashMap<String, UmlElement> getAssociate() {
        return associate;
    }
    
    public HashMap<String, UmlClass> getAssoClass() {
        return assoClass;
    }
    
    public HashMap<String, UmlElement> getParentAsso() {
        return parentAsso;
    }
    
    public List<UmlAssociationEnd> getAssoEndList() {
        return assoEndList;
    }
    
    public HashMap<String, UmlAssociationEnd> getSelfAssociate() {
        return selfAssociate;
    }
}
