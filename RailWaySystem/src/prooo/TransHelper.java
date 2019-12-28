package prooo;

import pro.MyNode;
import pro.MyPath;

import java.util.HashMap;
import java.util.Set;

import static java.lang.Integer.min;

public abstract class TransHelper {
    private final int max;
    private final int inf;
    private int mapSize;
    private int[][] cost; // 最重要的矩阵，是该类的核心矩阵
    private int[][] leastMap; // cost计算之后的图
    private HashMap<Integer, Integer> valueToOrder; // 结点值映射到序号
    private HashMap<Integer, Integer> orderToValue; // cost的序号到结点值
    
    TransHelper(int max, HashMap<Integer, MyNode> nodeHashMap) {
        this.max = max;
        inf = 99999999;
        cost = new int[max][max]; // 总共只有120个不同的结点
        initCost();
        leastMap = new int[max][max];
        mapSize = 0;
        valueToOrder = new HashMap<>();
        orderToValue = new HashMap<>();
    }
    
    private void initCost() {
        //初始化cost矩阵
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < i; j++) {
                cost[i][j] = inf;
                cost[j][i] = inf;
            }
        }
    }
    
    public void resetCost(Set<MyPath> pathSet) {
        initCost();
        for (MyPath path : pathSet) {
            pathToMap(path);
        }
    } // remove之后使用:重构cost矩阵s
    
    public void addCost(MyPath path) {
        pathToMap(path);
    } // 利用path建图
    
    public void pathToMap(MyPath myPath) {
        int[][] pathMap = new int[max][max];
        for (int i = 0; i < myPath.size(); i++) {
            // 完成结点值到0-120区段的映射
            int node = myPath.getNode(i); // 结点值
            if (!valueToOrder.containsKey(node)) {
                valueToOrder.put(node, mapSize);
                orderToValue.put(mapSize, node);
                mapSize++;
            }
        }
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < i; j++) {
                pathMap[i][j] = inf;
                pathMap[j][i] = inf;
            }
        }
        
        for (int i = 0; i < myPath.size() - 1; i++) {
            int x = valueToOrder.get(myPath.getNode(i));
            int y = valueToOrder.get(myPath.getNode(i + 1));
            pathMap[x][y] = 1;
            //System.out.println("pathmap:" + x + "," + y);
            pathMap[y][x] = 1;
        }
        // 对path生成的邻接矩阵进行求最短路径
        travelBfs(pathMap, myPath); // 直接在这一步更新cost矩阵
    } // 将指定路径添加到cost中，初始化path的邻接矩阵
    
    // 对大图求最短路径
    // 执行完这个函数，cost中保存了想要的信息
    public void floyd() {
        leastMap = cost.clone();
        for (int via = 0; via < mapSize; via++) {
            for (int start = 0; start < mapSize; start++) {
                if (leastMap[start][via] == inf) {
                    continue;
                }
                int t = leastMap[start][via]; // 利用对称性
                for (int des = 0; des < start; des++) {
                    if (leastMap[via][des] == inf) {
                        continue;
                    }
                    leastMap[start][des] = min(leastMap[start][des],
                            t + leastMap[via][des]);
                    leastMap[des][start] = leastMap[start][des];
                }
            }
        }
    }
    
    private void travelBfs(int[][] pathMap, MyPath path) {
        int[] vis = new int[mapSize];
        for (int i = 0; i < path.size(); i++) {
            // 把结果合并到大图中
            int start = valueToOrder.get(path.getNode(i));
            minDis(start, pathMap, vis); // 一次遍历某一点所在的连通分支
            for (int j = 0; j < mapSize; j++) {
                vis[j] = 0;
            }
        }
    }
    
    public void minDis(int start, int[][] disMap, int[] vis) {
    
    }
    
    public void minDis(int[][] pathMap) {
    
    }
    
    public void clear() {
        mapSize = 0;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < i; j++) {
                cost[j][i] = inf;
            }
        }
        valueToOrder.clear(); // 结点值映射到序号
        orderToValue.clear(); // cost的序号到结点值
    }
    
    // 继承关系函数
    int getInf() {
        return inf;
    }
    
    int[][] getCost() {
        return cost;
    }
    
    int[][] getLeastMap() {
        return leastMap;
    }
    
    int getMapSize() {
        return mapSize;
    }
    
    void setMapSize(int getMapSize) {
        this.mapSize = getMapSize;
    }
    
    HashMap<Integer, Integer> getValueToOrder() {
        return valueToOrder;
    }
    
    HashMap<Integer, Integer> getOrderToValue() {
        return orderToValue;
    }
    
    int getMax() {
        return max;
    }
}
