package prooo;

import pro.MyNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class LeaTranGen extends TransHelper {
    public LeaTranGen(int max, HashMap<Integer, MyNode> nodeHashMap) {
        super(max, nodeHashMap);
    }
    
    @Override // 源点，path的邻接矩阵，标记数组
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
                    if (pathMap[v][i] < getCost()[v][i]) {
                        getCost()[v][i] = 1;
                        getCost()[i][v] = 1;
                    }
                    w = i;
                    if (pathMap[start][w] == getInf()) {
                        pathMap[start][w] = 1;
                        pathMap[w][start] = 1;
                        if (pathMap[w][start] < getCost()[w][start]) {
                            getCost()[w][start] = 1;
                            getCost()[start][w] = 1;
                        }
                    }
                    queue.add(w);
                }
            }
        }
    }
    
    public int getLeastTrans(int i, int i1) {
        if (i1 == i) {
            return 0;
        }
        int x = getValueToOrder().get(i);
        int y = getValueToOrder().get(i1);
        //System.out.println("x : " + x + " y : " + y);
        return getLeastMap()[x][y] - 1; // 换乘次数减一，最后一次不用换乘
    }
}
