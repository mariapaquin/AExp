public class IfNestedEmptyBlocks {
    public void m(int a){
        if (true) {
            if(false){

            }
        }
        System.out.println(a);
    }
}
