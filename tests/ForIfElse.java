public class ForIfElse {
    public void m(int a){
        for (int i = 0; i < a; i++) {
            if (i == 0) {
                a = 1;
            } else {
                a = 2;
            }
        }
        a = 3;
    }
}
