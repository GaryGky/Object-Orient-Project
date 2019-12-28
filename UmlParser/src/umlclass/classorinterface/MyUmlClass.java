package umlclass.classorinterface;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlOperation;
import umlclass.helper.AssociateHelper;
import umlclass.helper.AttributeHelper;
import umlclass.helper.InterfaceHelper;
import umlclass.helper.OprationHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyUmlClass extends MyUmlClassOrInterface {
    private final String id;
    private final String name;
    private UmlClass parent; // 如果父类为null，则说明是顶级类
    private HashMap<String, UmlClass> child; // id 映射到子类
    private AssociateHelper associateHelper;
    private AttributeHelper attributeHelper;
    private OprationHelper oprationHelper;
    private InterfaceHelper interfaceHelper;
    
    public MyUmlClass(UmlClass umlClass) {
        id = umlClass.getId();
        name = umlClass.getName();
        parent = null;
        child = new HashMap<>();
        associateHelper = new AssociateHelper();
        attributeHelper = new AttributeHelper(this.name);
        oprationHelper = new OprationHelper();
        interfaceHelper = new InterfaceHelper(name);
    }
    
    public void setParent(UmlClass umlClass) {
        this.parent = umlClass;
    }
    
    public void setChild(UmlClass child) {
        this.child.put(child.getId(), child);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public UmlClass getParent() {
        return parent;
    }
    
    public void addAttribute(UmlAttribute attribute) {
        attributeHelper.addAttribute(attribute);
    }
    
    public void addAttribute(Map<? extends String, ? extends UmlAttribute> m) {
        attributeHelper.addAttribute(m);
    }
    
    public void addOperation(UmlOperation operation) {
        oprationHelper.addOperation(operation);
    }
    
    public void addOperation(UmlOperation operation, Direction direction) {
        oprationHelper.addOperation(operation, direction);
    }
    
    public void setAttrClassMap() {
        attributeHelper.setAttrClassMap();
    }
    
    public void setAttrClassMap(Map<? extends String,
            ? extends AttributeClassInformation> map) {
        attributeHelper.setAttrClassMap(map);
    }
    
    public HashMap<String, UmlAttribute> getAttribute() {
        return attributeHelper.getAttribute();
    }
    
    public Map<? extends String,
            ? extends AttributeClassInformation> getAttrClassMap() {
        return attributeHelper.getAttrClassMap();
    }
    
    public void addAssociate(UmlElement element) {
        associateHelper.addAssociate(element);
    }
    
    public void addAssoEnd(UmlAssociationEnd end) {
        associateHelper.addAssoEnd(end);
    }
    
    public void addParentAssoEnd(List<UmlAssociationEnd> ends) {
        associateHelper.addParentAssoEnd(ends);
    }
    
    public void addParentAssoEle(Map<?
            extends String, ? extends UmlElement> m) {
        associateHelper.addParentAssoEle(m);
    }
    
    public void addParentAssoClass(Map<? extends String,
            ? extends UmlClass> m) {
        associateHelper.addParentAssoClass(m);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUmlClass)) {
            return false;
        }
        MyUmlClass that = (MyUmlClass) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName());
    }
    
    @Override
    public Set<String> getParentsId() {
        Set<String> parents = new HashSet<>();
        if (parent != null) {
            parents.add(parent.getId());
        }
        return parents;
    }
    
    public void addIntereface(UmlInterface umlInterface) {
        interfaceHelper.addIntereface(umlInterface);
    }
    
    public void addMyInterface(MyUmlInterface myUmlInterface) {
        interfaceHelper.addMyInterface(myUmlInterface);
    }
    
    public void addParentIntereface(HashMap<String, UmlInterface> m) {
        interfaceHelper.addParentInt(m);
    }
    
    public void addParentMyInt(HashMap<String, MyUmlInterface> map) {
        interfaceHelper.addParentMyInt(map);
    }
    
    public Map<String, UmlOperation> getOperation() {
        return oprationHelper.getOperation();
    }
    
    public Map<String, UmlOperation> getReturnOptMap() {
        return oprationHelper.getReturnOptMap();
    }
    
    public Map<String, UmlOperation> getInOptMap() {
        return oprationHelper.getInOptMap();
    }
    
    public Map<String, UmlAttribute> getParentAttr() {
        return attributeHelper.getParentAttr();
    }
    
    public HashMap<String, HashMap<Visibility, Integer>> getNameOpVisMap() {
        return oprationHelper.getNameOpVisMap();
    }
    
    public Map<String, Integer> getAttrNameToNum() {
        return attributeHelper.getAttrNameToNum();
    }
    
    public HashMap<String, Visibility> getNameToVis() {
        return attributeHelper.getNameToVis();
    }
    
    public HashMap<String, UmlInterface> getInterfaceMap() {
        return interfaceHelper.getInterfaceMap();
    }
    
    public List<AttributeClassInformation> getInfoList() {
        return attributeHelper.getInfoList();
    }
    
    public int getClassAssociationCount() {
        return associateHelper.getClassAssociationCount();
    }
    
    public List<String> getAssoList() {
        return associateHelper.getAssoList();
    }
    
    public HashMap<String, UmlElement> getAssociate() {
        return associateHelper.getAssociate();
    }
    
    public HashMap<String, UmlClass> getAssoClass() {
        return associateHelper.getAssoClass();
    }
    
    public List<String> getImplementInterfaceList() {
        return interfaceHelper.getIntList();
    }
    
    public HashMap<String, UmlElement> getParentAsso() {
        return associateHelper.getParentAsso();
    }
    
    public HashMap<String, MyUmlInterface> getMyInterfaceMap() {
        return interfaceHelper.getMyInterfaceMap();
    }
    
    public List<UmlAssociationEnd> getAssoEndList() {
        return associateHelper.getAssoEndList();
    }
    
    public HashMap<String, UmlAssociationEnd> getSelfAssociate() {
        return associateHelper.getSelfAssociate();
    }
    
    public void addRealNum(HashMap<String, Integer> map) {
        interfaceHelper.addRealNum(map);
    }
    
    public HashMap<String, Integer> getId2RealNum() {
        return interfaceHelper.getId2RealNum();
    }
}
