NAME	Ackermann

MODE	REDUCTION

SORTS	NAT

SIGNATURE
	0 : -> NAT
	s : NAT -> NAT
	+ : NAT NAT -> NAT
	* : NAT NAT -> NAT
	fac : NAT -> NAT
	ack : NAT NAT -> NAT

ORDERING   KBO
        ack = 1, fac = 1, * = 1, + = 1, s = 1, 0 = 1
	ack > fac > * > + > s > 0

VARIABLES
	x,y : NAT

EQUATIONS
	+(x,0) = x
	+(x,s(y)) = s(+(x,y))
	*(x,0) = 0
	*(x,s(y)) = +(*(x,y),x)
	fac(0) = s(0)
	fac(s(x)) = *(s(x),fac(x))
	ack(0,y) = s(y)
	ack(s(x),0) = ack(x,s(0))
	ack(s(x),s(y)) = ack(x,ack(s(x),y))

CONCLUSION fac(s(s(s(s(s(0)))))) = ack(s(s(s(0))),0)
	*(ack(s(s(s(0))),0),0) = *(0,ack(s(s(s(0))),0))
