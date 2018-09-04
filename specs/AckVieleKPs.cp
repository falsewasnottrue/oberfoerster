NAME AckVieleXSKPs

% entscheidende Regel: 
%   ub^mab^nae -> o
%   bei Verwendung der ersten Ordnung muessen dann
%   mindestens A(n,m) kritische Paare berechnet werden;

MODE COMPLETION


SORTS ANY


SIGNATURE
   a,b,c,d,e,f,g,i,o,r,u,w   : ANY -> ANY
   A,B,C,F,G,K,O,P,R,S,T,U,X : ANY -> ANY
   sk                        :     -> ANY

ORDERING KBO   % laengen-lexikographische Ordnung durch KBO simuliert
   w=1, f=1, g=1, A=1, C=1, T=1, B=1, S=1, R=1, F=1, G=1, K=1, U=1,
   P=1, u=1, a=1, c=1, b=1, X=1, d=1, r=1, i=1, e=1, o=1, O=1, sk=1
   w>f>g>A>C>T>B>S>R>F>G>K>U>P>u>a>c>b>X>d>r>i>e>o>O>sk
   
% eine Vielzahl von redundanten kritischen Paaren entsteht bei Verwendung
% der folgenden Ordnungen:
%
% ORDERING KBO   % Silbenordnung durch RPO simuliert
                % = 1, wfgACTBSRFGKUPuacbXdrieoO = 1
%    w>f>g>A>C>T>B>S>R>F>G>K>U>P>u>a>c>b>X>d>r>i>e>o>O
%
% ORDERING KBO   % laengen-lexikographische Ordnung durch KBO simuliert
%    w=1, f=1, g=1, A=1, C=1, T=1, B=1, S=1, R=1, F=1, G=1, K=1, U=1,
%    P=1, u=1, a=1, c=1, b=1, X=1, d=1, r=1, i=1, e=1, o=1, O=1
%    b>O>w>f>g>A>C>T>B>S>R>F>G>K>U>P>u>a>c>X>d>r>i>e>o
               

VARIABLES   x : ANY


EQUATIONS

w(w(u(x)))		= U(f(f(x)))
f(f(b(x)))		= B(f(f(x)))
f(f(a(x)))		= A(g(g(x)))
g(g(a(e(x))))		= F(d(x))
A(F(x))			= F(c(x))
B(F(x))			= F(b(x))
U(F(x))			= r(b(x))
g(g(a(a(x))))		= G(a(x))
g(g(a(b(x))))		= G(b(x))
A(G(x))			= G(a(x))
B(G(x))			= G(b(x))
U(G(x))			= u(b(x))
g(g(b(x)))		= B(T(T(x)))
T(T(b(x)))		= B(T(T(x)))
T(T(a(x)))		= R(R(a(x)))
B(R(R(x)))		= R(R(b(x)))
B(A(R(R(b(x)))))	= S(a(b(K(P(x)))))
P(b(x))			= b(P(x))
P(a(x))			= a(a(x))
U(A(R(R(b(x)))))	= u(b(a(x)))
T(T(K(b(x))))		= S(b(K(C(x))))
T(T(K(a(x))))		= S(a(x))
B(S(x))			= S(b(x))
A(S(x))			= S(a(x))
U(S(x))			= u(x)
C(b(x))			= b(C(x))
C(a(x))			= a(b(x))
w(o(x))			= o(x)

u(b(b(b(a(b(b(a(e(x)))))))))	= o(x)

i(r(b(c(x))))		= O(x)
c(X(x))			= b(c(x))
O(X(x))			= O(x)
O(d(x))			= x
i(o(b(x)))		= x
w(b(x))			= x
w(a(x))			= x
O(a(x))			= x
O(o(b(x)))		= x
w(S(x))			= x
U(X(x))			= x
f(S(x))			= x
g(S(x))			= x
T(S(x))			= x
w(R(x))			= x


CONCLUSION
   a(sk) = sk   % Das System muss zuerst A(n,m) kritische Paare erzeugen,
                % bevor der Beweis gefunden wird


