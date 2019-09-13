public class EmptyFor {
    public void m(int a){
        int x = 1;
        for (int i = 0; i < a; i++) {
        }
        a = a * (a%a + a*a)/10;
    }
}
