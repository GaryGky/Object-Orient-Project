package umlclass.helper;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.HashMap;

public class OprationHelper {
    private HashMap<String, HashMap<Visibility, Integer>> nameOpVisMap;
    private HashMap<String, UmlOperation> inOptMap; // 带传入参数的方法
    private HashMap<String, UmlOperation> returnOptMap; // 带返回值的方法
    private HashMap<String, UmlOperation> operation; // id映射到方法
    private boolean updateName;
    
    public OprationHelper() {
        nameOpVisMap = new HashMap<>();
        inOptMap = new HashMap<>();
        returnOptMap = new HashMap<>();
        operation = new HashMap<>();
        updateName = false;
    }
    
    // 添加方法的时候使用
    public void addOperation(UmlOperation opEle) {
        operation.put(opEle.getId(), opEle);
    }
    
    // 遍历参数的时候使用
    public void addOperation(UmlOperation operation, Direction direction) {
        switch (direction) {
            case IN:
                inOptMap.put(operation.getId(), operation);
                break;
            case INOUT:
                inOptMap.put(operation.getId(), operation);
                break;
            case OUT:
                inOptMap.put(operation.getId(), operation);
                break;
            case RETURN:
                returnOptMap.put(operation.getId(), operation);
                break;
            default:
                break;
        }
    }
    
    private void mapOpVis() {
        for (UmlOperation umlOperation : operation.values()) {
            String opName = umlOperation.getName();
            Visibility vis = umlOperation.getVisibility();
            if (!nameOpVisMap.containsKey(opName)) {
                HashMap<Visibility, Integer> map = new HashMap<>();
                map.put(vis, 1);
                nameOpVisMap.put(opName, map);
            } else {
                HashMap<Visibility, Integer> map = nameOpVisMap.get(opName);
                if (map.containsKey(vis)) {
                    map.put(vis, map.get(vis) + 1);
                } else {
                    map.put(vis, 1);
                }
            }
        }
    } // 建立其由方法名称到哈希表的映射
    
    public HashMap<String, UmlOperation> getOperation() {
        return operation;
    }
    
    public HashMap<String, HashMap<Visibility, Integer>> getNameOpVisMap() {
        if (!updateName) {
            mapOpVis();
            updateName = true;
        }
        return nameOpVisMap;
    }
    
    public HashMap<String, UmlOperation> getInOptMap() {
        return inOptMap;
    }
    
    public HashMap<String, UmlOperation> getReturnOptMap() {
        return returnOptMap;
    }
}
