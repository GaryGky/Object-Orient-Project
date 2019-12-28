package prooo;

import pro.MyNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class LeaTicGen extends TransHelper {
    
    public LeaTicGen(int max, HashMap<Integer, MyNode> nodeHashMap) {
        super(max, nodeHashMap);
    }
    
    public void minDis(int start, int[][] pathMap, int[] vis) {
        int v;
        int w;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            v = queue.poll();
            vis[v] = 1;
            // 遍历v的邻接点
            for (int i = 0; i < getMapSize(); i++) {
                if (pathMap[v][i] == 1 && vis[i] == 0) {
                    w = i;
                    if (pathMap[v][i] < getCost()[v][i]) {
                        getCost()[v][i] = 3;
                        getCost()[i][v] = 3;
                    }
                    if (pathMap[start][w] == getInf()) {
                        pathMap[start][w] = pathMap[start][v] + 1;
                        pathMap[w][start] = pathMap[start][v] + 1;
                        // 对cost矩阵进行更新实现合并
                        // 注意这里涉及到换乘的问题
                        if (pathMap[w][start] + 2 < getCost()[w][start]) {
                            // w和start为两个换乘点
                            getCost()[w][start] = pathMap[w][start] + 2;
                            getCost()[start][w] = pathMap[w][start] + 2;
                        }
                    }
                    queue.add(w);
                }
            }
        }
    }
    
    public int getLeaTic(int i, int i1) {
        if (i == i1) {
            return 0;
        }
        int x = getValueToOrder().get(i);
        int y = getValueToOrder().get(i1);
        return getCost()[x][y] - 2;
    } // 最后一次换乘是多余的
}
