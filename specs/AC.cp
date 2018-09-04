NAME		AC

MODE		COMPLETION

SORTS		ANY

SIGNATURE	f : ANY ANY -> ANY

ORDERING	KBO
                f = 1
                f

VARIABLES	x,y,z : ANY

EQUATIONS	f(f(x,y),z) = f(x,f(y,z))
		f(x,y) = f(y,x)

CONCLUSION 
