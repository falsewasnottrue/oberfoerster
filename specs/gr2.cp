NAME    GR2

MODE    COMPLETION

SORTS   ANY

SIGNATURE
        gra, grb, grc, grd, gre : ANY -> ANY
          a,   b,   c,   d,   e : ANY -> ANY

ORDERING   KBO
        gre = 1, grd = 1, grc = 1, grb = 1, gra = 1, 
	e = 1, d = 1, c = 1, b = 1, a = 1
        gre > grd > grc > grb > gra > e > d > c > b > a

VARIABLES
        x : ANY

EQUATIONS
        gra (a (x))             = x
        a (gra (x))             = x
        grb (b (x))             = x
        b (grb (x))             = x
        grc (c (x))             = x
        c (grc (x))             = x
        grd (d (x))             = x
        d (grd (x))             = x
        gre (e (x))             = x
        e (gre (x))             = x
        b (a (x))               = a (b (c (x)))
        c (a (x))               = a (c (d (x)))
        c (b (x))               = b (c (e (x)))
        d (a (x))               = a (d (x))
        d (b (x))               = b (d (x))
        d (c (x))               = c (d (x))
        e (a (x))               = a (e (x))
        e (b (x))               = b (e (x))
        e (c (x))               = c (e (x))
        e (d (x))               = d (e (x))
        b (b (x))   = c (x)
        c (c (x))   = d (x)
        d (d (x))   = e (x)

CONCLUSION
        gre (x) = x
