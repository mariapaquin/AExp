package AE;

public class IfElse {
    public void m(int a, int b){
        a = b+1;

        if (a*a < 10) {
            b = a + b;
        } else {
            b = 1;
        }
        a = b+2;
    }
}
