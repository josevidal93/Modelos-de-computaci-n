(A)	Z++
	IF (Z > X) GOTO S
	IF (Z > X2) GOTO S
	IF (Z | X) GOTO B
	GOTO A
(B)	IF (Z | X2) GOTO C
	GOTO A
(C)	Y <- Z
	GOTO A
