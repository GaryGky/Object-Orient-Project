import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pro.MyPath;

import static org.junit.Assert.*;

public class MyRailwaySystemTest {
    private MyRailwaySystem railwaySystem;
    private MyPath p1;
    private MyPath p2;
    private MyPath p3;
    private MyPath p4;
    private MyPath p5;
    private MyPath p6;
    
    @Before
    public void setUp() throws Exception {
        System.out.println("Before Test");
        railwaySystem = new MyRailwaySystem();
        p1 = new MyPath(1, 2, 3, 4);
        p2 = new MyPath(5, 6, 7, 8);
        p3 = new MyPath(9, 10, 11, 12);
        p4 = new MyPath(13, 14, 15, 16);
        p5 = new MyPath(17, 18, 19, 20);
        p6 = new MyPath(1, 5, 9, 13, 17);
        railwaySystem.addPath(p1);
        railwaySystem.addPath(p2);
        railwaySystem.addPath(p3);
        railwaySystem.addPath(p4);
        railwaySystem.addPath(p5);
        
    }
    @Test
    public void getLeastTransferCount() throws Exception {
        
        railwaySystem.addPath(p6);
        
        Assert.assertEquals(0, railwaySystem.getLeastTransferCount(1, 2));
        Assert.assertEquals(0, railwaySystem.getLeastTransferCount(1, 3));
        Assert.assertEquals(0, railwaySystem.getLeastTransferCount(1, 4));
        Assert.assertEquals(0, railwaySystem.getLeastTransferCount(4, 2));
        Assert.assertEquals(0, railwaySystem.getLeastTransferCount(3, 2));
        
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(1, 8));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(1, 12));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(1, 16));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(1, 20));
        
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(17, 8));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(17, 12));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(17, 16));
        Assert.assertEquals(1, railwaySystem.getLeastTransferCount(17, 4));
        
        Assert.assertEquals(2, railwaySystem.getLeastTransferCount(20, 4));
        Assert.assertEquals(2, railwaySystem.getLeastTransferCount(19, 4));
        Assert.assertEquals(2, railwaySystem.getLeastTransferCount(18, 4));
        Assert.assertEquals(2, railwaySystem.getLeastTransferCount(16, 4));
        Assert.assertEquals(2, railwaySystem.getLeastTransferCount(6, 4));
    }
    
    @Test
    public void getUnpleasantValue() {
    }
    
    @Test
    public void getLeastUnpleasantValue() {
    }
    
    @Test
    public void getConnectedBlockCount() {
    }
}