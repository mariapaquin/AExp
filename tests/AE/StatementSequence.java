package AE;

public class StatementSequence {
    public void m(int a, int b, int c, int d) {
        a = b+c;
        b = a-d;
        c = b+c;
        d = a-d;
    }
}
