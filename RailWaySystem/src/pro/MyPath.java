package pro;

import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class MyPath implements Path {
    private ArrayList<Integer> nodes;
    private ArrayList<Integer> disNodes; // 按照输入顺序记录不同的结点
    
    public MyPath(int... nodelist) {
        nodes = new ArrayList<>(100);
        disNodes = new ArrayList<>(100);
        for (Integer i : nodelist) {
            nodes.add(i);
            if (!disNodes.contains(i)) {
                disNodes.add(i);
            }
        }
    }
    
    @Override
    public int size() {
        return nodes.size();
    }
    
    @Override
    public int getNode(int i) {
        return nodes.get(i);
    }
    
    @Override
    public boolean containsNode(int i) {
        return nodes.contains(i);
    }
    
    public int getDistinctNodeCount() {
        return disNodes.size();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyPath)) {
            return false;
        }
        MyPath integers = (MyPath) o;
        return Objects.equals(nodes, integers.nodes);
    }
    
    @Override
    public boolean isValid() {
        return nodes.size() >= 2 && nodes.size() <= 1000;
    }
    
    @Override
    public int getUnpleasantValue(int nodeid) {
        if (containsNode(nodeid)) {
            return (int) Math.pow(4, (nodeid % 5 + 5) % 5);
        } else {
            return 0;
        }
    }
    
    @Override
    public int compareTo(Path o) {
        MyPath myPath = (MyPath) o;
        for (int i = 0; i < Math.min(myPath.size(), this.size()); i++) {
            if (nodes.get(i) != myPath.getNode(i)) {
                if (nodes.get(i) > myPath.getNode(i)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return this.size() - myPath.size();
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }
    
}
