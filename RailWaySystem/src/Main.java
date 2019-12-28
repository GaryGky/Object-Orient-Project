import com.oocourse.specs3.AppRunner;
import pro.MyPath;

public class Main {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        AppRunner runner = AppRunner.newInstance(
                MyPath.class, MyRailwaySystem.class);
        runner.run(args);
        System.out.println("程序运行时间" + (System.currentTimeMillis() - start) / 1000 + "s");
    }
}

