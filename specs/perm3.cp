% Endgueltiges Gleichungssystem:
%------------------------------
%
% f (x,y,z) = f (z,x,y)
% f (x,y,z) = f (x,z,y)
% f (x,y,z) = f (y,x,z)
% f (x,y,z) = f (z,y,x)

NAME        perm3

MODE        COMPLETION

SORTS   ANY

SIGNATURE
	f : ANY ANY ANY -> ANY

ORDERING    KBO
	f = 2
        f
VARIABLES
	x, y, z : ANY

EQUATIONS
	f (x,y,z)  =  f (z,x,y)
	f (x,y,z)  =  f (x,z,y)

CONCLUSION
