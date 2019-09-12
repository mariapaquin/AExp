package AE;

public class If {
    public void m(int a, int b, int c) {
        int x2 = Debug.makeSymbolicInteger("x2");
		int x0 = Debug.makeSymbolicInteger("x0");
		a = x0;
        if (true) {
            b = b+1;
        }
        a = x2;
    }

}
