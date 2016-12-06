(A)	IF X != 0 GOTO B
	IF X2 != 0 GOTO S
	Y++
	GOTO S
(B)	IF X2 != 0 GOTO C
	GOTO S
(C)	X--
	X2--
	GOTO A
