import java.util.HashSet;

public class EmptyEnhancedFor {
    HashSet<String> strings = new HashSet<>();

    public void m(int a, int b, int c) {
        for (String str: strings) {
        }
        System.out.println(c);
    }
}
