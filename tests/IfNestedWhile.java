public class IfNestedWhile {

    public void m(int a, int b) {
        b = 1;
        if (b == 1) {
            while (b != 2) {
                b = 2;
            }
        }
        b = 3;
    }
}
