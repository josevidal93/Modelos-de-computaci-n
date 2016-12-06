	IF X != 0 GOTO A
	Y++
	Z++
	IF Z != 0 GOTO S
(A)	X--
	IF X != 0 GOTO B
(C)	Z++
	IF Z != 0 GOTO C
(B)	X--
	IF X != 0 GOTO A
	Y++
