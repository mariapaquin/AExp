public class DoWhile {

    public void m(int a) {
        a = a*a;
        do {
            a++;
        } while (a < 10 + a);
        System.out.println(a);
    }

    public void m(int a, int b, int c, int d) {
        a = b*c;
        b = a*d;
        c = b*c;
        d = a*d;
    }
}
