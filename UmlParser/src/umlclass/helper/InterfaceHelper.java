package umlclass.helper;

import com.oocourse.uml2.models.elements.UmlInterface;
import umlclass.classorinterface.MyUmlInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class InterfaceHelper {
    private String name;
    private HashMap<String, UmlInterface> interfaceMap; // id 映射到接口
    // id 映射到"我的接口"(其中包含了接口的父接口)
    private HashMap<String, MyUmlInterface> myInterfaceMap;
    private boolean update;
    private Set<String> interfaceSet;
    // 实现的接口名称
    private List<String> intFaceName;
    private HashMap<String, Integer> id2RealNum; // 一个类实现了接口id多少次
    
    public InterfaceHelper(String name) {
        this.name = name;
        interfaceMap = new HashMap<>();
        myInterfaceMap = new HashMap<>();
        interfaceSet = new HashSet<>();
        update = false;
        intFaceName = new LinkedList<>();
        id2RealNum = new HashMap<>();
    }
    
    public void addIntereface(UmlInterface umlInterface) {
        interfaceMap.put(umlInterface.getId(), umlInterface);
    } // 注意这里是put id号
    
    public void addMyInterface(MyUmlInterface myUmlInterface) {
        interfaceMap.putAll(myUmlInterface.getParents());
        interfaceMap.putAll(myUmlInterface.getDirparents());
        myInterfaceMap.put(myUmlInterface.getId(), myUmlInterface);
        String id1 = myUmlInterface.getId();
        if (id2RealNum.containsKey(id1)) {
            id2RealNum.put(id1, id2RealNum.get(id1) + 1);
        } else {
            id2RealNum.put(id1, 1);
        }
        for (String id : myUmlInterface.getId2GenNum().keySet()) {
            if (id2RealNum.containsKey(id)) {
                id2RealNum.put(id, id2RealNum.get(id) +
                        myUmlInterface.getId2GenNum().get(id));
            } else {
                id2RealNum.put(id, myUmlInterface.getId2GenNum().get(id));
            }
        }
    }
    
    public void addParentInt(HashMap<String, UmlInterface> m) {
        interfaceMap.putAll(m);
    }
    
    public void addParentMyInt(HashMap<String, MyUmlInterface> map) {
        for (MyUmlInterface myUmlInterface : map.values()) {
            addMyInterface(myUmlInterface);
        }
    }
    
    public void addRealNum(HashMap<String, Integer> map) {
        for (String id : map.keySet()) {
            if (map.get(id) > 1) {
                id2RealNum.put(id, map.get(id));
            }
        }
    }
    
    private void setImplementInterfaceSet() {
        for (MyUmlInterface myIntFace : myInterfaceMap.values()) {
            interfaceSet.add(myIntFace.getId());
            for (UmlInterface intFace : myIntFace.getParents().values()) {
                interfaceSet.add(intFace.getId());
            }
            for (UmlInterface intFace : myIntFace.getDirparents().values()) {
                interfaceSet.add(intFace.getId());
            }
        }
    }
    
    private void setImplementInterfaceList() {
        for (String intFaceId : interfaceSet) {
            intFaceName.add(interfaceMap.get(intFaceId).getName());
        }
    }
    
    public List<String> getIntList() {
        if (!update) {
            setImplementInterfaceSet();
            setImplementInterfaceList();
            update = true;
        }
        return intFaceName;
    }
    
    public HashMap<String, UmlInterface> getInterfaceMap() {
        return interfaceMap;
    }
    
    public HashMap<String, MyUmlInterface> getMyInterfaceMap() {
        return myInterfaceMap;
    }
    
    public HashMap<String, Integer> getId2RealNum() {
        if (!update) {
            setImplementInterfaceSet();
            setImplementInterfaceList();
            update = true;
        }
        return id2RealNum;
    }
}
