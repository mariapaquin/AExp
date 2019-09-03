import java.util.HashSet;

public class EnhancedFor {
    HashSet<String> strings = new HashSet<>();

    public void m(int a, int b, int c) {
        System.out.println(a);
        for (String str: strings) {
            System.out.println(b);
        }
        System.out.println(c);
    }
}
