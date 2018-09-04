NAME       group

MODE       COMPLETION

SORTS	ANY

SIGNATURE
	e : -> ANY
	i : ANY -> ANY
	f : ANY ANY -> ANY

ORDERING  KBO
        i = 0, f = 1, e = 1
	i > f > e

VARIABLES
	x,y,z : ANY

EQUATIONS
	f(x,e) = x
	f(x,i(x)) = e
	f(f(x,y),z) = f(x,f(y,z))

CONCLUSION 
