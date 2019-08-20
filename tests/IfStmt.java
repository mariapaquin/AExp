public class IfStmt {
        public void testNoBracketsIf(int a, int b){
        if (true)
            b = 1;
        else
            b = 2;
        b = 3;
    }

    public void testIfElse(int a, int b){
        a = b+1;

        if (true) {
            b = 0;
        } else {
            b = 1;
        }
        a = b+2;

    }

    public void testNestedIfElse(int a, int b){
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

    public void testNestedIf(){
        int a;
        int b = 0;
        a = b+1;
        if (true) {
            b = 0;
            if (false) {
                b = 1;
            }
        }
        a = b+2;
    }
}
