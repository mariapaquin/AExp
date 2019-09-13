package AE;

public class DoWhile {

    public void m(int a) {
        a = 1+a;
        do {
            a=a*a;
        } while (a < 10 + a);
        System.out.println(a);
    }
}
