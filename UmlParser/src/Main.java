import com.oocourse.uml2.interact.AppRunner;

public class Main {
    private static final String input = "src/1.txt";
    private static final String output = "src/myout.txt";
    
    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(MyUmlInteraction.class);
        appRunner.run(args);
    }
}
