% Endgueltiges Gleichungssystem:
% ------------------------------
% 
% f (x,y,z,u) = f (z,u,x,y)
% f (x,y,z,u) = f (y,x,z,u)
% f (x,y,z,u) = f (z,u,y,x)
% f (x,y,z,u) = f (x,y,u,z)
% f (x,y,z,u) = f (u,z,y,x)
% f (x,y,z,u) = f (y,x,u,z)

NAME        gleichungen

MODE        COMPLETION

SORTS       any

SIGNATURE   f : any any any any -> any

ORDERING    KBO
            f = 2
	    f

VARIABLES
	x,u,v,w : any
EQUATIONS
	    f (u,v,w,x)  = f (w,x,u,v)
            f (u,v,w,x)  = f (v,u,w,x)

CONCLUSION



