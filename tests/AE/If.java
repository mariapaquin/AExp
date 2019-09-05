package AE;

public class If {
    public void m(int a, int b, int c, int d) {
        if (a>0) {
            a = b*c + d;
            a++;
        }

        a = b*c + a;
    }

}
