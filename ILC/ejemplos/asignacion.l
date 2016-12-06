	# Y<-0
(A)	Y--
	IF Y != 0 GOTO A

	# Asignacion
	Z++
(A2)	IF X != 0 GOTO A3
	IF Z != 0 GOTO A4
(A3)	X--
	Y++
	Z2++
	IF Z != 0 GOTO A2
(A4)	IF Z2 != 0 GOTO A5
	IF Z != 0 GOTO S
(A5)	Z2--
	X++
	IF Z != 0 GOTO A4
