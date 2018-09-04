NAME	HF86

MODE	REDUCTION

SORTS	ANY

SIGNATURE
	a, b, c, d, e, f, g : ANY -> ANY
        sk : -> ANY
ORDERING   KBO
	a = 5, b = 6, c = 6, d = 6, e = 1, f = 1, g = 1, sk = 1
	b > a > c > d > e > f > g > sk

VARIABLES
	x : ANY

EQUATIONS
	 a (e (x)) = e (f (f (f (f (x)))))
	 b (x) = e (f (f (f (x))))
	 c (x) = e (f (f (x)))
	 d (x) = e (f (x))
	 g (e (x)) = x

CONCLUSION
% Loesung :
% e (f (f (f (f (f (f (f (e (f (f (e (f (e (f (g (x)))))))))))))))) =
% e (f (f (f (f (f (f (f (f (f (f (f (f (f (f (e (x))))))))))))))))

         a(b(c(d(e(f(g(sk))))))) = a(c(g(a(a(e(e(sk)))))))

