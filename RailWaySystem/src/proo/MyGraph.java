package proo;

import com.oocourse.specs3.models.Graph;
import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import pro.MyNode;
import pro.MyPath;
import pro.MyPathContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyGraph implements Graph {
    private MyPathContainer myPathContainer;
    private GraphHelper graphHelper;
    private HashMap<Integer, MyNode> nodeHashMap; // 结点值 -> Mynode
    private HashMap<Integer, Integer> unionFindMap; // 并查集:从结点映射到标号
    private boolean update;
    private int conBlockNum;
    
    public MyGraph() {
        nodeHashMap = new HashMap<>(300);
        unionFindMap = new HashMap<>(300);
        myPathContainer = new MyPathContainer();
        graphHelper = new GraphHelper(999999, 300, this.myPathContainer);
        update = false;
        conBlockNum = 0;
    }
    
    private MyNode getNode(int index) {
        if (nodeHashMap.containsKey(index)) {
            return nodeHashMap.get(index);
        } else {
            MyNode node = new MyNode(index);
            nodeHashMap.put(index, node);
            return node;
        }
    }
    
    private void updateMap() {
        update = true; // 设置标志为“已更新”
        unionFindMap.clear(); // dfs更新并查集
        travleDfs();
        try {
            graphHelper.fillMap(nodeHashMap);// 更新邻接矩阵、距离矩阵、最少票价矩阵
        } catch (PathIdNotFoundException e) {
            e.printStackTrace();
        }
        HashSet<Integer> valueSet = new HashSet<>(unionFindMap.values());
        conBlockNum = valueSet.size();
    }
    
    private void addToNodeMap(MyPath myPath) {
        for (int i = 0; i < myPath.size(); i++) {
            MyNode node = getNode(myPath.getNode(i)); // 更新nodemap
            if (i == 0) {
                node.addNode(getNode(myPath.getNode(i + 1))); // 添加邻接点
            } else if (i == myPath.size() - 1) {
                node.addNode(getNode(myPath.getNode(i - 1)));
            } else {
                node.addNode(getNode(myPath.getNode(i - 1)));
                node.addNode(getNode(myPath.getNode(i + 1)));
            }
        }
    }
    
    private void subFromNodeMap(MyPath myPath) {
        for (int i = 0; i < myPath.size(); i++) {
            MyNode node = getNode(myPath.getNode(i));
            if (i == 0) {
                node.deleteNode(getNode(myPath.getNode(i + 1)));
            } else if (i == myPath.size() - 1) {
                node.deleteNode(getNode(myPath.getNode(i - 1)));
            } else {
                node.deleteNode(getNode(myPath.getNode(i - 1)));
                node.deleteNode(getNode(myPath.getNode(i + 1)));
            }
        }
        clearAlone();
    }
    
    private void clearAlone() {
        nodeHashMap.entrySet().removeIf(integerMyNodeEntry ->
                integerMyNodeEntry.getValue().isAlone());
    } // 清除孤立点
    
    // 建立并查集
    private void travleDfs() {
        //TimableOutput.println("TravleDfs is called");
        int count = 1; // 并查集组号：从1开始编号
        for (Integer integer : nodeHashMap.keySet()) {
            dfs(nodeHashMap.get(integer), count);
            count++;
        }
        for (Integer integer : nodeHashMap.keySet()) {
            nodeHashMap.get(integer).unvisit();
        }
    }
    
    private void dfs(MyNode i, int count) {
        if (i.isVisit()) {
            return;
        }
        i.visited();
        unionFindMap.put(i.getValue(), count);
        //System.out.println(i.getValue() + " is add to unionFindMap " + count);
        MyNode nextNode = i;
        while (nextNode != null) {
            if (!nextNode.isVisit()) {
                dfs(nextNode, count);
            }
            nextNode = i.getFirst();
        }
    }
    
    public int getConnectedBlockNum() {
        if (!update) {
            updateMap();
        }
        return conBlockNum;
    }
    
    public Set<MyPath> getAllPaths() {
        return myPathContainer.getAllPaths();
    }
    
    public HashMap<Integer, MyNode> getNodeHashMap() {
        return this.nodeHashMap;
    }
    
    @Override
    public int size() {
        return myPathContainer.size();
    }
    
    @Override
    public boolean containsPath(Path path) {
        return myPathContainer.containsPath(path);
    }
    
    @Override
    public boolean containsPathId(int i) {
        return myPathContainer.containsPathId(i);
    }
    
    @Override
    public Path getPathById(int i) throws Exception {
        return myPathContainer.getPathById(i);
    }
    
    @Override
    public int getPathId(Path path) throws Exception {
        return myPathContainer.getPathId(path);
    }
    
    @Override
    public int addPath(Path path) throws Exception {
        //如果添加了一条原来已经存在的路径
        //图结构不要变化
        if (myPathContainer.containsPath(path)) {
            return myPathContainer.addPath(path);
        } else {
            int pathid = myPathContainer.addPath(path);
            MyPath myPath = (MyPath) path;
            addToNodeMap(myPath);
            update = false;
            return pathid;
        }
    }
    
    @Override
    public int removePath(Path path) throws Exception {
        int rnt = myPathContainer.removePath(path);
        MyPath myPath = (MyPath) path;
        subFromNodeMap(myPath);
        update = false;
        return rnt;
    }
    
    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        MyPath myPath = (MyPath) myPathContainer.getPathById(i);
        myPathContainer.removePathById(i);
        subFromNodeMap(myPath);
        update = false;
    }
    
    @Override
    public int getDistinctNodeCount() {
        return myPathContainer.getDistinctNodeCount();
    }
    
    @Override
    public boolean containsNode(int i) {
        return nodeHashMap.containsKey(i);
    }
    
    @Override
    public boolean containsEdge(int i, int i1) {
        // 由邻接矩阵获得
        if (!update) {
            update = true;
            updateMap();
        }
        return graphHelper.isAdjacent(i, i1);
    }
    
    @Override
    public boolean isConnected(int i, int i1) throws NodeIdNotFoundException {
        // 由并查集获得
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else {
            if (!update) {
                updateMap();
            }
            return unionFindMap.get(i).equals(unionFindMap.get(i1));
        }
    }
    
    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        // 由距离矩阵获得
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (!update) {
                updateMap();
            }
            return graphHelper.getMinDis(i, i1);
        }
    }
}
