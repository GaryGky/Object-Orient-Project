package prooo;

import pro.MyNode;
import pro.MyPath;

import java.util.HashMap;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class LeaUpsetGen extends TransHelper {
    private final int utrans; // 换乘代价
    
    private int getUnpleasantValue(int n1, int n2) {
        int p1 = (int) Math.pow(4, (n1 % 5 + 5) % 5);
        int p2 = (int) Math.pow(4, (n2 % 5 + 5) % 5);
        return max(p1, p2);
    }
    
    public LeaUpsetGen(int max, HashMap<Integer, MyNode> nodeHashMap) {
        super(max, nodeHashMap);
        utrans = 32;
    }
    
    @Override
    public void minDis(int[][] pathMap) {
        for (int via = 0; via < getMapSize(); via++) {
            for (int start = 0; start < getMapSize(); start++) {
                if (pathMap[start][via] == getInf()) {
                    continue;
                }
                int t = pathMap[start][via]; // 利用对称性
                if (pathMap[start][via] + utrans < getCost()[start][via]) {
                    getCost()[start][via] = pathMap[start][via] + utrans;
                    getCost()[via][start] = getCost()[start][via];
                }
                for (int des = 0; des < start; des++) {
                    if (pathMap[via][des] == getInf()) {
                        continue;
                    }
                    pathMap[start][des] = min(pathMap[start][des],
                            t + pathMap[via][des]);
                    pathMap[des][start] = pathMap[start][des];
                    if (pathMap[des][start] + utrans < getCost()[des][start]) {
                        getCost()[start][des] = pathMap[start][des] + utrans;
                        getCost()[des][start] = getCost()[start][des];
                    }
                }
            }
        }
    }
    
    public void pathToMap(MyPath myPath) {
        int[][] pathMap = new int[getMax()][getMax()];
        for (int i = 0; i < myPath.size(); i++) {
            // 完成结点值到0-120区段的映射
            int node = myPath.getNode(i);
            if (!getValueToOrder().containsKey(node)) {
                getValueToOrder().put(node, getMapSize());
                getOrderToValue().put(getMapSize(), node);
                setMapSize(getMapSize() + 1);
            }
        }
        for (int i = 0; i < getMapSize(); i++) {
            pathMap[i][i] = 0; // 对角线元素的初始化
            for (int j = 0; j < i; j++) {
                pathMap[i][j] = getInf();
                pathMap[j][i] = getInf();
            }
        }
        for (int i = 0; i < myPath.size() - 1; i++) {
            int x = getValueToOrder().get(myPath.getNode(i));
            int y = getValueToOrder().get(myPath.getNode(i + 1));
            pathMap[x][y] = getUnpleasantValue(
                    myPath.getNode(i), myPath.getNode(i + 1));
            pathMap[y][x] = pathMap[x][y];
        }
        // 对path生成的邻接矩阵进行求最短路径
        minDis(pathMap);
    } // 将指定路径添加到cost中，初始化path的邻接矩阵
    
    public int getLeastUnpleasant(int i1, int i2) {
        if (i1 == i2) {
            return 0;
        }
        int x = getValueToOrder().get(i1);
        int y = getValueToOrder().get(i2);
        return getCost()[x][y] - 32;
    }
}
