package java8;

/**
 * @author linlazy
 */
public class MainAnonymousClass {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("Anonymous Class Thread run()"));
    }
}