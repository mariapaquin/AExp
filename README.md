# AvailableExpressions
Implementation of available expressions through constraint based analysis.

Track available expressions for each program point. Initialize the first
constraint term (exit[init]) to empty set. Initialize every other
constraint term to AE (the set of expressions) and kill when we find
a path along which it is not available).

After the analysis is performed, we have a map from each statement to the
expressions that are available *upon entry* to that statement.

For example,

a = b*c;

b = a*d;

c = b*c;

produces the key value pairs:

Key : a=b * c;
Value : []

Key : b=a * d;
Value : [b * c]

Key : c=b * c;
Value : [a * d]