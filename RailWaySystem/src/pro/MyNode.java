package pro;

import java.util.HashMap;
import java.util.Set;

public class MyNode {
    private int value;
    private HashMap<MyNode, Integer> nodeToRef; // 结点 -> 使用次数
    private boolean visit; // dfs使用
    private HashMap<Integer, Boolean> valueMap; // 保存邻接结点值，如果包含某节点，那么一定为true
    
    public Set<Integer> getAdjNode() {
        // 获取该节点的邻接结点
        return valueMap.keySet();
    }
    
    public MyNode(int value) {
        this.value = value;
        nodeToRef = new HashMap<>(250);
        visit = false;
        valueMap = new HashMap<>();
    }
    
    public void addNode(MyNode key) {
        if (nodeToRef.containsKey(key)) {
            nodeToRef.put(key, 1 + nodeToRef.get(key));
        } else {
            nodeToRef.put(key, 1);
            valueMap.put(key.getValue(), true);
        }
    }
    
    public void deleteNode(MyNode key) {
        int cnt = nodeToRef.get(key) - 1;
        if (cnt == 0) {
            nodeToRef.remove(key);
            valueMap.remove(key.getValue());
        } else {
            nodeToRef.put(key, cnt);
        }
    }
    
    public void visited() {
        visit = true;
    }
    
    public void unvisit() {
        visit = false;
    }
    
    public boolean isVisit() {
        return visit;
    }
    
    public MyNode getFirst() {
        Set<MyNode> keys = nodeToRef.keySet();
        for (MyNode node : keys) {
            if (!node.isVisit()) {
                return node;
            }
        }
        return null;
    } // 获取结点第一个没有vis过的值
    
    public int getValue() {
        return value;
    }
    
    public boolean isAlone() {
        return nodeToRef.size() == 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof MyNode)) {
            return false;
        } else {
            MyNode node = (MyNode) obj;
            return node.value == this.value;
        }
    }
}
