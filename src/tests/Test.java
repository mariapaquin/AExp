package tests;

public class Test {

//    public void m(int x, int y){
//        int a = 1;
//        int b = 2;
//
//        x = a+b;
//        y = a*b;
//
//        while (y > a + b) {
//            a = a+1;
//            x = a+b;
//        }
//    }

//    public void testIfElse(int a, int b){
//        a = b+1;
//
//        if (true) {
//            b = 0;
//        } else {
//            b = 1;
//        }
//        a = b+2;
//
//    }

    public void testNestedIf(int a, int b){
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

//    public void m(){
//        int a = 1;
//        if (true) {
//            int b = 2;
//        } else {
//            int c = 3;
//        }
//        int d = 4;
//    }
}
