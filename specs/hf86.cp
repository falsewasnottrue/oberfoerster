% Vervollstaendigtes System
%
% a (e (x)) -> e (f (f (f (f (x)))))
% b (x) -> e (f (f (f (x))))
% c (x) -> e (f (f (x)))
% d (x) -> e (f (x))
% g (e (x)) -> x


NAME	HF86

MODE	COMPLETION

SORTS	ANY

SIGNATURE
	a, b, c, d, e, f, g : ANY -> ANY

ORDERING   KBO
	a = 5, b = 6, c = 6, d = 6, e = 1, f = 1, g = 1
	b > a > c > d > e > f > g

VARIABLES
	x : ANY

EQUATIONS
	d(x) = e(f(x))
	c(x) = d(f(x))
	c(f(x)) = b(x)
	x = g(e(x))
	b(f(x)) = a(e(x))

CONCLUSION
