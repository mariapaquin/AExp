public class For {

    public void m(int a){
        a = 1;
        a = a*a + 1;
        for (int i = 0; i < a; i++) {
            a = a*a + 2;
        }
        a = a*a + 3;
    }
}
