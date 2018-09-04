NAME	HU80

MODE	COMPLETION

SORTS	ANY

SIGNATURE
	f, g, h : ANY ANY -> ANY

ORDERING   KBO
	f=4, g=4, h=2
	f > g > h

VARIABLES
	x,y : ANY

EQUATIONS
	h(x,f(x,y)) = y
	y = f(x,h(x,y))
	g(h(x,y),y) = x
	h(g(x,y),y) = x
	h(x,h(y,x)) = y

CONCLUSION
	f(x,f(y,x)) = f(f(x,y),x)
	g(x,g(y,x)) = g(g(x,y),x)
	h(x,h(y,x)) = h(h(x,y),y)

% Loesungen
% x = x
% x = x
% x = h (h (x,y),y)

