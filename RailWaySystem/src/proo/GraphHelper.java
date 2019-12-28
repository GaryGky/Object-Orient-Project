package proo;

import com.oocourse.specs3.models.PathIdNotFoundException;
import pro.MyNode;
import pro.MyPathContainer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GraphHelper {
    private final int maxSize;// 最多能容纳的结点数
    private final int inf;
    private int mapSize; // 邻接矩阵和距离矩阵的大小
    private int[][] adjMap; // 邻接矩阵
    private int[][] disMap; // 距离矩阵 —— minDis
    private int[] vis;// 距离矩阵和最小票价矩阵共享
    private HashMap<Integer, Integer> valueToOrder; // 结点值映射到0-249序号
    private HashMap<Integer, Integer> orderToValue; // 序号映射到结点值
    
    GraphHelper(int inf, int maxSize, MyPathContainer pathContainer) {
        this.maxSize = maxSize;
        adjMap = new int[maxSize][maxSize];
        disMap = new int[maxSize][maxSize];
        vis = new int[maxSize];
        valueToOrder = new HashMap<>(maxSize);
        orderToValue = new HashMap<>(maxSize);
        //leaTicMap = new int[maxSize][maxSize];
        //traNoPath = new int[maxSize][maxSize];
        this.mapSize = 0;
        this.inf = inf;
        //this.myPathContainer = pathContainer;
    }
    
    void fillMap(HashMap<Integer, MyNode> nodeHashMap)
            throws PathIdNotFoundException {
        mapInit();// 初始化三个数据结构
        mapSize = mapValue(nodeHashMap.keySet()); // mapvalue返回映射段的长度
        fillAdj(nodeHashMap);
        fillDis();
        //fillLeastTicket(nodeHashMap);
    }
    
    private int mapValue(Set<Integer> valueSet) {
        int cnt = 0;
        for (Integer value : valueSet) {
            valueToOrder.put(value, cnt);
            orderToValue.put(cnt++, value);
        }
        return cnt;
    }
    
    private void fillAdj(HashMap<Integer, MyNode> nodeHashMap) {
        //TimableOutput.println("FillAdj is called");
        for (Integer key : nodeHashMap.keySet()) {
            MyNode myNode = nodeHashMap.get(key); // 获取到结点
            int cow = valueToOrder.get(key); // 获取到邻接矩阵中的对应标号
            for (Integer value : myNode.getAdjNode()) {
                int row = valueToOrder.get(value);
                adjMap[cow][row] = 1;
                disMap[cow][row] = 1; // 初始化距离矩阵
                //leaTicMap[cow][row] = 1; // 初始化最少票价矩阵
            }
        }
    }
    
    private void fillDis() {
        //TimableOutput.println("fillDis is called");
        for (int i = 0; i < mapSize; i++) {
            disMap[i][i] = 0;
        }
        
        for (int i = 0; i < mapSize; i++) {
            minDis(i); // 一次遍历某一点所在的连通分支
            for (int j = 0; j < mapSize; j++) {
                vis[j] = 0;
            }
        }
    }
    
    private void minDis(int start) {
        int v;
        int w;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            v = queue.poll();
            vis[v] = 1;
            // 遍历v的邻接点
            for (int i = 0; i < mapSize; i++) {
                if (disMap[v][i] == 1 && vis[i] == 0) {
                    w = i;
                    if (disMap[start][w] == inf) {
                        disMap[start][w] = disMap[start][v] + 1;
                        disMap[w][start] = disMap[start][v] + 1;
                    }
                    queue.add(w);
                }
            }
        }
    }
    
    private void mapInit() {
        valueToOrder.clear();
        orderToValue.clear();
        for (int i = 0; i < maxSize; i++) {
            vis[i] = 0;
            for (int j = 0; j < maxSize - i; j++) {
                adjMap[i][j] = 0;
                adjMap[j][i] = 0;
                disMap[i][j] = inf;
                disMap[j][i] = inf;
            }
        }
    }
    
    boolean isAdjacent(int x, int y) {
        // contains Edge
        int i;
        int i1;
        try {
            i = valueToOrder.get(x);
            i1 = valueToOrder.get(y);
        } catch (NullPointerException e) {
            return false;
        }
        return adjMap[i][i1] == 1;
    }
    
    int getMinDis(int x, int y) {
        int i;
        int i1;
        i = valueToOrder.get(x);
        i1 = valueToOrder.get(y);
        return disMap[i][i1];
    }
}
