public class DoWhile {

    public void m(int a) {
        a = a*a;
        do {
            a++;
        } while (a < 10);
        a = a+a;
        System.out.println(a);
    }
}
