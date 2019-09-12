package AE;

public class StatementSequence {
    public void m(int a, int b, int c, int d, int e) {
        b = a*d;
        a = a+1;
        b = a*d;
        b = a*d;
        a = b*c;
        b = a*d;
        e = b*c;
//        d = a*d;
    }
}
