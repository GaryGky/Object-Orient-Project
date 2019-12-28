package pro;

import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathContainer;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;

import java.util.HashMap;
import java.util.Set;

public class MyPathContainer implements PathContainer {
    private int count;
    private HashMap<Integer, MyPath> hashMapToPath;
    private HashMap<MyPath, Integer> hashMapToId;
    private HashMap<Integer, Integer> hashMapDisCnt;
    
    public MyPathContainer() {
        count = 0;
        hashMapDisCnt = new HashMap<>(250);
        hashMapToId = new HashMap<>(250);
        hashMapToPath = new HashMap<>(250);
    }
    
    private void addDisNode(MyPath myPath) {
        for (Integer i : myPath) {
            if (!hashMapDisCnt.containsKey(i)) {
                // 如果不存在则直接加入
                hashMapDisCnt.put(i, 1);
            } else {
                //如果存在则数量加一
                Integer tmp = hashMapDisCnt.get(i) + 1;
                hashMapDisCnt.put(i, tmp);
            }
        }
    } // 添加不同的结点
    
    private void subDisNode(MyPath myPath) {
        for (Integer i : myPath) {
            if (hashMapDisCnt.containsKey(i)) {
                int cnt = hashMapDisCnt.get(i) - 1;
                if (cnt == 0) {
                    hashMapDisCnt.remove(i);
                } else {
                    hashMapDisCnt.put(i, cnt);
                }
            }
        }
    } // 清除不同的结点
    
    public Set<MyPath> getAllPaths() {
        return hashMapToId.keySet();
    }
    
    @Override
    public int size() {
        return hashMapToPath.size();
    }
    
    @Override
    public boolean containsPath(Path path) {
        return hashMapToId.containsKey(path);
    }
    
    @Override
    public boolean containsPathId(int i) {
        return hashMapToPath.containsKey(i);
    }
    
    @Override
    public Path getPathById(int i) throws PathIdNotFoundException {
        if (!containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        } else {
            return hashMapToPath.get(i);
        }
    }
    
    @Override
    public int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            return hashMapToId.get(path);
        }
        throw new PathNotFoundException(path);
    }
    
    @Override
    public int addPath(Path path) throws PathNotFoundException
    {
        if (path != null && path.isValid()) {
            if (containsPath(path)) {
                return getPathId(path);
            } else {
                // 列表中不包含这个Path
                MyPath myPath = (MyPath) path;
                count++;
                hashMapToId.put(myPath, count);
                hashMapToPath.put(count, myPath);
                addDisNode(myPath);
                return getPathId(path);
            }
        } else {
            return 0;
        }
    }
    
    @Override
    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        MyPath myPath = (MyPath) path;
        Integer pathId = hashMapToId.get(myPath);
        hashMapToId.remove(myPath);
        hashMapToPath.remove(pathId);
        subDisNode(myPath);
        return pathId;
    }
    
    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        }
        MyPath myPath = hashMapToPath.get(i);
        Integer pathId = hashMapToId.get(myPath);
        hashMapToId.remove(myPath);
        hashMapToPath.remove(pathId);
        subDisNode(myPath);
    }
    
    @Override
    public int getDistinctNodeCount() {
        return hashMapDisCnt.size();
    }
}