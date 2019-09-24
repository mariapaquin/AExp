# AvailableExpressions

Initialize a symbolic variable for each non-linear variable
expression. Every time the expression is killed, reassign
the *same* symbolic variable (rather than generating a new
one, as in the first approach). 

For example,

    public void m(int a, int b) {
        while (b == 1) {
                b = b+1;
        }
        a = b*b;
    }

is rewritten as

    public void m(int a, int b) {
        int x0 = Debug.makeSymbolicInteger("x0");
		while (b == 1) {
                b = b+1;
				x0 = Debug.makeSymbolicInteger("x0");
        }
        a = x0;
    }