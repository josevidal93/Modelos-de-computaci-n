	IF X != 0 GOTO A
	Y++
	Z++
	IF Z != 0 GOTO S
(A)	X--
	IF X != 0 GOTO B
	Z++
	IF Z != 0 GOTO S
(B)	X--
	IF X != 0 GOTO A
	Y++
