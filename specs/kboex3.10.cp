% Vervollstaendigtes System 
%
%  f(x,h(x,y)) -> y
%  g(h(x,y),y) -> x
%  h(1,x) -> x
%  h(x,1) -> x
%  f(1,x) -> x
%  f(x,x) -> 1
%  g(x,1) -> x
%  g(x,x) -> 1

NAME Cancellationlaw

MODE COMPLETION

SORTS ANY

SIGNATURE 
	 f, g, h : ANY ANY -> ANY
	 1 : -> ANY

ORDERING KBO
	f = 2, g = 2, h = 2, 1 = 1
	f > g > h > 1

VARIABLES x, y : ANY

EQUATIONS
	f(x,h(x,y)) = y
	g(h(x,y),y) = x
	h(1,x) = x
	h(x,1) = x

CONCLUSION


