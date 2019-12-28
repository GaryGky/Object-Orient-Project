package umlclass;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import umlclass.classorinterface.MyUmlClass;
import umlclass.classorinterface.MyUmlClassOrInterface;
import umlclass.classorinterface.MyUmlInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

class Check {
    private HashMap<String, MyUmlClass> idToMyClass;
    private HashMap<String, UmlInterface> interfaceHashMap;
    private HashMap<String, UmlElement> idToElement;
    private HashMap<String, MyUmlInterface> idToMyInterface;
    private HashMap<String, MyUmlClassOrInterface> id2MyClassOrInterface;
    private int color;
    private HashMap<String, UmlClass> classHashMap;
    
    Check(HashMap<String, MyUmlClass> idToMyClass,
          HashMap<String, UmlInterface> interfaceHashMap,
          HashMap<String, UmlElement> idToElement,
          HashMap<String, MyUmlInterface> idToMyInterface,
          HashMap<String, UmlClass> classHashMap) {
        this.classHashMap = classHashMap;
        this.idToElement = idToElement;
        this.idToMyClass = idToMyClass;
        this.idToMyInterface = idToMyInterface;
        this.interfaceHashMap = interfaceHashMap;
        id2MyClassOrInterface = new HashMap<>();
    }
    
    void checkForUml002() throws UmlRule002Exception {
        //不能含有重名成员
        Set<AttributeClassInformation> aclSet = new HashSet<>(100);
        for (MyUmlClass myClass : idToMyClass.values()) {
            HashMap<String, Integer> name2Num = new HashMap<>();
            // 先建立好end的名称
            for (UmlAssociationEnd end : myClass.getSelfAssociate().values()) {
                addToAcl(aclSet, myClass, name2Num, end.getName());
            }
            // 把属性和end name进行比较
            for (UmlAttribute attribute : myClass.getAttribute().values()) {
                addToAcl(aclSet, myClass, name2Num, attribute.getName());
            }
        }
        if (aclSet.size() != 0) {
            throw new UmlRule002Exception(aclSet);
        }
    }
    
    private void addToAcl(Set<AttributeClassInformation> aclSet,
                          MyUmlClass myClass, HashMap<String, Integer> name2Num
            , String name) {
        if (name == null) {
            return;
        }
        if (!name2Num.containsKey(name)) {
            name2Num.put(name, 1);
        } else {
            name2Num.put(name, name2Num.get(name) + 1);
            AttributeClassInformation acl = new
                    AttributeClassInformation(
                    name, myClass.getName());
            aclSet.add(acl);
        }
    }
    
    void checkForUml008() throws UmlRule008Exception {
        Set<UmlClassOrInterface> cirCle = new HashSet<>();
        id2MyClassOrInterface.putAll(idToMyClass);
        id2MyClassOrInterface.putAll(idToMyInterface);
        loop(cirCle, interfaceHashMap);
        loop(cirCle, classHashMap);
        checkdig(cirCle);
        if (cirCle.size() != 0) {
            throw new UmlRule008Exception(cirCle);
        }
        color = 0;
    }
    
    private void checkdig(Set<UmlClassOrInterface> circle) {
        for (MyUmlClassOrInterface myUmlClassOrInterface
                : id2MyClassOrInterface.values()) {
            for (String parentId : myUmlClassOrInterface.getParentsId()) {
                if (parentId.equals(myUmlClassOrInterface.getId())) {
                    circle.add((UmlClassOrInterface)
                            idToElement.get(parentId));
                }
            }
        }
    } // 检查自环
    
    private void loop(Set<UmlClassOrInterface> cirCle,
                      Map<String, ? extends UmlClassOrInterface> map) {
        Set<String> vis = new HashSet<>();
        Integer tot = 1;
        for (UmlClassOrInterface classOrInterface :
                map.values()) {
            HashMap<Integer, Set<MyUmlClassOrInterface>>
                    color2Set = new HashMap<>();
            HashMap<MyUmlClassOrInterface, Integer> dfnMap = new HashMap<>();
            HashMap<MyUmlClassOrInterface, Integer> lowMap = new HashMap<>();
            Stack<MyUmlClassOrInterface> stack = new Stack<>();
            tarjan(classOrInterface, stack,
                    dfnMap, lowMap,
                    color2Set, tot, vis);
            for (Set<MyUmlClassOrInterface> set : color2Set.values()) {
                if (set.size() > 1) {
                    for (MyUmlClassOrInterface x : set) {
                        cirCle.add((UmlClassOrInterface)
                                idToElement.get(x.getId()));
                    }
                }
            }
        }
    }
    
    private void tarjan(UmlClassOrInterface start,
                        Stack<MyUmlClassOrInterface> stack,
                        HashMap<MyUmlClassOrInterface, Integer> dfnMap,
                        HashMap<MyUmlClassOrInterface, Integer> lowMap,
                        HashMap<Integer, Set<MyUmlClassOrInterface>> color2Set,
                        Integer tot, Set<String> vis) {
        if (vis.contains(start.getId())) {
            return;
        }
        MyUmlClassOrInterface myStart =
                id2MyClassOrInterface.get(start.getId());
        dfnMap.put(myStart, tot);
        lowMap.put(myStart, tot);
        stack.push(myStart);
        vis.add(start.getId());
        // id 映射到一个元素
        HashMap<String, UmlClassOrInterface> parents = new HashMap<>();
        if (start instanceof UmlClass) {
            UmlClass parent = idToMyClass.get(start.getId()).getParent();
            if (parent != null) {
                parents.put(parent.getId(), classHashMap.get(parent.getId()));
            }
        } else if (start instanceof UmlInterface) {
            parents.putAll(idToMyInterface.get(start.getId()).getDirparents());
        }
        for (UmlClassOrInterface next : parents.values()) {
            if (next == null) {
                continue;
            }
            MyUmlClassOrInterface myCorI = id2MyClassOrInterface
                    .get(next.getId());
            if (dfnMap.getOrDefault(myCorI, 0) == 0) {
                // 如果还没有走到这个点
                tarjan(next, stack, dfnMap, lowMap,
                        color2Set, tot + 1, vis);
                lowMap.put(myStart, Math.min(lowMap.getOrDefault(myStart,
                        999999999),
                        lowMap.getOrDefault(myCorI, 999999999)));
            } else if (stack.contains(myCorI)) {
                // 如果走到的点已经在栈中了，这表明出现了循环继承
                lowMap.put(myStart, Math.min(lowMap.get(myStart),
                        lowMap.get(myCorI)));
            }
        }
        if (lowMap.get(myStart).equals(dfnMap.get(myStart))) {
            Set<MyUmlClassOrInterface> circleSet = new HashSet<>();
            while (!stack.peek().equals(myStart)) {
                circleSet.add(stack.pop());
                color2Set.put(color, circleSet);
            }
            circleSet.add(stack.pop());
            color2Set.put(color, circleSet);
            color++;
        }
    }
    
    void checkForUml009() throws UmlRule009Exception {
        // 不能重复继承
        Set<UmlClassOrInterface> dupGenSet = new HashSet<>();
        // 类的接口实现关系
        for (MyUmlClass myClass : idToMyClass.values()) {
            for (String id : myClass.getId2RealNum().keySet()) {
                if (myClass.getId2RealNum().get(id) > 1) {
                    dupGenSet.add(classHashMap.get(myClass.getId()));
                }
            }
        }
        // 接口的继承关系
        for (MyUmlInterface i : idToMyInterface.values()) {
            for (String id : i.getId2GenNum().keySet()) {
                if (i.getId2GenNum().get(id) > 1) {
                    dupGenSet.add(interfaceHashMap.get(i.getId()));
                }
            }
        }
        
        if (dupGenSet.size() != 0) {
            throw new UmlRule009Exception(dupGenSet);
        }
    }
}
