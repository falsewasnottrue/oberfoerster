NAME        Z22

MODE        COMPLETION

SORTS       ANY

SIGNATURE   a, a1, b, b1, c, c1, d, d1, e, e1: ANY -> ANY
               

ORDERING    KBO
            e1 = 1, e = 1, d1 = 1, d = 1, c1 = 1, c = 1, 
	    b1 = 1, b = 1, a1 = 1, a = 1
            e1 > e > d1 > d > c1 > c > b1 > b > a1 > a

VARIABLES   x: ANY

EQUATIONS
  
           a (b (c (x))) = d (x)
           b (c (d (x))) = e (x)
           c (d (e (x))) = a (x)
           d (e (a (x))) = b (x)
           e (a (b (x))) = c (x)
           a (a1 (x)) = x
           a1 (a (x)) = x
           b (b1 (x)) = x
           b1 (b (x)) = x
           c (c1 (x)) = x
           c1 (c (x)) = x
           d (d1 (x)) = x
           d1 (d (x)) = x
           e (e1 (x)) = x
           e1 (e (x)) = x

CONCLUSION
