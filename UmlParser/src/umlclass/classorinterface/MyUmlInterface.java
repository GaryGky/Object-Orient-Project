package umlclass.classorinterface;

import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyUmlInterface extends MyUmlClassOrInterface {
    private final String id;
    private final String name;
    private HashMap<String, UmlInterface> dirparents; // 直接父接口
    private HashMap<String, UmlInterface> children; // 子接口
    private HashMap<String, UmlInterface> parents; // 一个接口的向上的父接口
    private HashMap<String, UmlOperation> idToOperation; // id映射到接口定义的方法
    private HashMap<String, Integer> id2GenNum; // 保存一个接口实现的次数
    
    public MyUmlInterface(UmlInterface umlInterface) {
        dirparents = new HashMap<>();
        this.name = umlInterface.getName();
        this.id = umlInterface.getId();
        children = new HashMap<>();
        parents = new HashMap<>();
        idToOperation = new HashMap<>();
        id2GenNum = new HashMap<>();
    }
    
    public void addOperation(UmlOperation op) {
        idToOperation.put(op.getId(), op);
    }
    
    // 添加父接口中的所有父接口
    public void addParents(Map<? extends String, ? extends UmlInterface> map) {
        parents.putAll(map);
    }
    
    // 添加父接口的父接口
    public void addGenNum(HashMap<String, Integer> map) {
        for (String id : map.keySet()) {
            if (id2GenNum.containsKey(id)) {
                id2GenNum.put(id, id2GenNum.get(id) + map.get(id));
            } else {
                id2GenNum.put(id, map.get(id));
            }
        }
    }
    
    // 添加父接口
    public void setParents(UmlInterface umlInterface) {
        dirparents.put(umlInterface.getId(), umlInterface);
        if (id2GenNum.containsKey(umlInterface.getId())) {
            id2GenNum.put(umlInterface.getId(), id2GenNum.
                    get(umlInterface.getId()) + 1);
        } else {
            id2GenNum.put(umlInterface.getId(), 1);
        }
    }
    
    public HashMap<String, UmlInterface> getParents() {
        return parents;
    }
    
    public HashMap<String, UmlInterface> getDirparents() {
        return dirparents;
    }
    
    public void setChildren(UmlInterface child) {
        this.children.put(child.getId(), child);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public HashMap<String, Integer> getId2GenNum() {
        return id2GenNum;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUmlInterface)) {
            return false;
        }
        MyUmlInterface that = (MyUmlInterface) o;
        return getId().equals(that.getId());
    }
    
    @Override
    public Set<String> getParentsId() {
        Set<String> parents = new HashSet<>();
        for (UmlInterface i : dirparents.values()) {
            parents.add(i.getId());
        }
        for (UmlInterface i : this.parents.values()) {
            parents.add(i.getId());
        }
        return parents;
    }
}
