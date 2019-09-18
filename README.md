# AvailableExpressions
Implementation of available expressions through constraint based analysis.

This version keeps track of what symbolic variables each expression maps to for each program point.

For example,

a = b*c;

if (true) {
  b = b+1;
}

a = b*c;

will be be transformed to

int x0 = Debug.makeSymbolicInteger("x0");

int x2 = Debug.makeSymbolicInteger("x2");

a = x0;    

if (true) {
  b = b+1;
}

a = x2;
    

