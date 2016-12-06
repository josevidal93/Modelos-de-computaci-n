(A)	Z++

	# Comienzo de la multiplicacion
	Z3<-0
	Z2<-Z
(B)	IF Z2 != 0 GOTO B2
	GOTO C
(B2)	Z2--
	Z4<-Z
	Z5<-Z3
(B3)	IF Z5 != 0 GOTO B4
	GOTO B5
(B4)	Z5--
	Z4++
	GOTO B3
(B5)	Z3<-Z4
	GOTO B
	# Fin de la multiplicacion

	# Comienzo de la macro >
(C)	Z6<-Z3
	Z7<-X
(C2)	IF Z6 != 0 GOTO C3
	GOTO D
(C3)	IF Z7 != 0 GOTO C4
	GOTO D2
(C4)	Z6--
	Z7--
	GOTO C2
	# Fin de la macro >

(D)	GOTO A
(D2)	Z--
	Y<-Z
