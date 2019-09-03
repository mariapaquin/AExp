public class NestedWhile {
    public void m(int a, int b) {
        b = 1;
        while (b == 1) {
            while (b != 2) {
                b = 2;
            }
        }
        b = 3;
    }
}
