public class IfElseNested {

    public void test(int a, int b){
        if (true) {
            if (false) {
                b = 1;
            } else {
                b = 2;
            }
        } else {
            b = 3;
        }
        b = 4;
    }
}
