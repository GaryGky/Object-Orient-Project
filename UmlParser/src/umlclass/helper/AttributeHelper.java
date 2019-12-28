package umlclass.helper;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttributeHelper {
    private final String name;
    private HashMap<String, UmlAttribute> attribute; // id映射到自身所有属性
    private HashMap<String, UmlAttribute> expAttribute; // id映射到非private属性
    private HashMap<String, UmlAttribute> parentAttr; // id映射到所有父类属性
    private HashMap<String, UmlAttribute> parentExpAttr; // id映射到所有父类非private属性
    // 属性id称映射到“属性类”
    private HashMap<String, AttributeClassInformation> attrClassMap;
    private Set<UmlAttribute> expAttrSet; // 保存所有非private的属性
    private List<AttributeClassInformation> infoList;// 用于保存类是否隐藏
    private HashMap<String, Integer> attrNameToNum;
    //private HashMap<String, UmlAttribute> nameToAttr;
    private HashMap<String, Visibility> nameToVis;
    
    public AttributeHelper(String name) {
        this.name = name;
        infoList = new LinkedList<>();
        attrNameToNum = new HashMap<>();
        //nameToAttr = new HashMap<>();
        expAttrSet = new HashSet<>();
        attrClassMap = new HashMap<>();
        parentAttr = new HashMap<>();
        parentExpAttr = new HashMap<>();
        expAttribute = new HashMap<>();
        attribute = new HashMap<>();
        updateNameToAttr = false;
        updateInfoList = false;
        nameToVis = new HashMap<>();
    }
    
    public void addAttribute(UmlAttribute umlAttribute) {
        attribute.put(umlAttribute.getId(),
                umlAttribute);
        if (umlAttribute.getVisibility() != Visibility.PRIVATE) {
            //System.out.println(umlAttribute);
            expAttribute.put(umlAttribute.getId(), umlAttribute);
        }
    }
    
    public void addAttribute(Map<? extends String, ? extends UmlAttribute> m) {
        for (UmlAttribute attribute : m.values()) {
            if (attribute.getVisibility() != Visibility.PRIVATE) {
                parentExpAttr.put(attribute.getId(), attribute);
            }
            parentAttr.put(attribute.getId(), attribute);
        }
    }
    
    public void setAttrClassMap() {
        // 父类用
        for (UmlAttribute attribute : attribute.values()) {
            AttributeClassInformation acl = new AttributeClassInformation(
                    attribute.getName(), this.name);
            attrClassMap.put(attribute.getId(), acl);
        }
    } // 建立“类是否违背信息隐藏原则”的信息
    
    public void setAttrClassMap(Map<? extends String,
            ? extends AttributeClassInformation> map) {
        // 先把父类的属性加进去
        this.attrClassMap.putAll(map);
        // 然后把自身的属性加进去
        setAttrClassMap();
    }
    
    private void setInfoList() {
        // 产生所有非private的属性
        expAttrSet.addAll(expAttribute.values());
        expAttrSet.addAll(parentExpAttr.values());
        for (UmlAttribute attribute : expAttrSet) {
            infoList.add(attrClassMap.get(attribute.getId()));
        }
    } // 查询非private的属性
    
    private void setAttrNameToNum() {
        for (UmlAttribute attribute : parentAttr.values()) {
            if (!attrNameToNum.containsKey(attribute.getName())) {
                attrNameToNum.put(attribute.getName(), 1);
            } else {
                int tmp = attrNameToNum.get(attribute.getName());
                attrNameToNum.put(attribute.getName(), tmp + 1);
            }
            nameToVis.put(attribute.getName(), attribute.getVisibility());
        }
        
        for (UmlAttribute attribute : attribute.values()) {
            if (!attrNameToNum.containsKey(attribute.getName())) {
                attrNameToNum.put(attribute.getName(), 1);
            } else {
                int tmp = attrNameToNum.get(attribute.getName());
                attrNameToNum.put(attribute.getName(), tmp + 1);
            }
            nameToVis.put(attribute.getName(), attribute.getVisibility());
        }
    } // 查询类内属性名称
    
    public HashMap<String, UmlAttribute> getParentAttr() {
        return parentAttr;
    }
    
    public HashMap<String, AttributeClassInformation> getAttrClassMap() {
        return attrClassMap;
    }
    
    private boolean updateInfoList;
    
    public List<AttributeClassInformation> getInfoList() {
        if (!updateInfoList) {
            updateInfoList = true;
            setInfoList();
        }
        return infoList;
    }
    
    public HashMap<String, Integer> getAttrNameToNum() {
        if (!updateNameToAttr) {
            setAttrNameToNum();
            updateNameToAttr = true;
        }
        return attrNameToNum;
    }
    
    private boolean updateNameToAttr;
    
    public HashMap<String, Visibility> getNameToVis() {
        return nameToVis;
    }
    
    public HashMap<String, UmlAttribute> getAttribute() {
        return attribute;
    }
}
