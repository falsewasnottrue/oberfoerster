% Endgueltiges Gleichungssystem:
%------------------------------
%
%(   1)  f (x,y,z,u) = f (u,x,y,z)
%(   2)  f (x,y,z,u) = f (x,y,u,z)
%(   3)  f (x,y,z,u) = f (z,u,x,y)
%(   4)  f (x,y,z,u) = f (z,x,y,u)
%(   5)  f (x,y,z,u) = f (y,u,z,x)
%(   6)  f (x,y,z,u) = f (z,u,y,x)
%(   7)  f (x,y,z,u) = f (u,y,z,x)
%(   8)  f (x,y,z,u) = f (z,x,u,y)
%(   9)  f (x,y,z,u) = f (x,u,y,z)
%(  10)  f (x,y,z,u) = f (x,z,y,u)
%(  11)  f (x,y,z,u) = f (z,y,u,x)
%(  12)  f (x,y,z,u) = f (u,z,y,x)
%(  13)  f (x,y,z,u) = f (y,x,z,u)
%(  14)  f (x,y,z,u) = f (x,u,z,y)
%(  15)  f (x,y,z,u) = f (z,y,x,u)
%(  16)  f (x,y,z,u) = f (y,x,u,z)


NAME        perm4

MODE        COMPLETION

SORTS   ANY

SIGNATURE
	f : ANY ANY ANY ANY -> ANY

ORDERING    KBO
	f = 2
        f
VARIABLES
	x, y, z, u : ANY

EQUATIONS
	f (x,y,z,u)  =  f (u,x,y,z)
	f (x,y,z,u)  =  f (x,y,u,z)


CONCLUSION
