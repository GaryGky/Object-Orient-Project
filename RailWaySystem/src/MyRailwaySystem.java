import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;
import pro.MyPath;
import proo.MyGraph;
import prooo.LeaTicGen;
import prooo.LeaTranGen;
import prooo.LeaUpsetGen;
import prooo.TransHelper;

import static java.lang.Integer.max;

public class MyRailwaySystem implements RailwaySystem {
    private MyGraph myGraph;
    private LeaTicGen leaTic;
    private LeaTranGen leaTrans;
    private LeaUpsetGen leaUpset;
    
    public MyRailwaySystem() {
        myGraph = new MyGraph();
        leaTic = new LeaTicGen(300, myGraph.getNodeHashMap());
        leaTrans = new LeaTranGen(300, myGraph.getNodeHashMap());
        leaUpset = new LeaUpsetGen(300, myGraph.getNodeHashMap());
    }
    
    @Override
    public int getLeastTicketPrice(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            return leaTic.getLeaTic(i, i1);
        }
    }
    
    @Override
    public int getLeastTransferCount(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            return leaTrans.getLeastTrans(i, i1);
        }
        
    }
    
    @Override
    public int getUnpleasantValue(Path path, int fromindex, int toindex) {
        int result = 0;
        for (int i = fromindex; i < toindex; i++) {
            int v0 = path.getUnpleasantValue(path.getNode(i));
            int v1 = path.getUnpleasantValue(path.getNode(i + 1));
            int v2 = path.getUnpleasantValue(path.getNode(i + 2));
            result += max(v0, max(v1, v2));
        }
        return result;
    }
    
    @Override
    public int getLeastUnpleasantValue(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        return leaUpset.getLeastUnpleasant(i, i1);
    }
    
    @Override
    public int getConnectedBlockCount() {
        return myGraph.getConnectedBlockNum();
    }
    
    @Override
    public boolean containsNode(int i) {
        return myGraph.containsNode(i);
    }
    
    @Override
    public boolean containsEdge(int i, int i1) {
        return myGraph.containsEdge(i, i1);
    }
    
    @Override
    public boolean isConnected(int i, int i1) throws NodeIdNotFoundException {
        return myGraph.isConnected(i, i1);
    }
    
    @Override
    public int getShortestPathLength(int i, int i1) throws
            NodeIdNotFoundException, NodeNotConnectedException {
        return myGraph.getShortestPathLength(i, i1);
    }
    
    @Override
    public int size() {
        return myGraph.size();
    }
    
    @Override
    public boolean containsPath(Path path) {
        return myGraph.containsPath(path);
    }
    
    @Override
    public boolean containsPathId(int i) {
        return myGraph.containsPathId(i);
    }
    
    @Override
    public Path getPathById(int i) throws Exception {
        return myGraph.getPathById(i);
    }
    
    @Override
    public int getPathId(Path path) throws Exception {
        return myGraph.getPathId(path);
    }
    
    @Override
    public int addPath(Path path) throws Exception {
        final int nodeid = myGraph.addPath(path);
        MyPath myPath = (MyPath) path;
        leaUpset.addCost(myPath);
        leaUpset.floyd();
        leaTrans.addCost(myPath);
        leaTrans.floyd();
        leaTic.addCost(myPath);
        leaTic.floyd();
        
        return nodeid;
    }
    
    private void updateHelper(TransHelper helper) {
        helper.clear();
        helper.resetCost(myGraph.getAllPaths());
        helper.floyd();
    }
    
    @Override
    public int removePath(Path path) throws Exception {
        final int rnt = myGraph.removePath(path);
        updateHelper(leaTic);
        updateHelper(leaTrans);
        updateHelper(leaUpset);
        return rnt;
    }
    
    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        myGraph.removePathById(i);
        updateHelper(leaTic);
        updateHelper(leaTrans);
        updateHelper(leaUpset);
    }
    
    @Override
    public int getDistinctNodeCount() {
        return myGraph.getDistinctNodeCount();
    }
}
