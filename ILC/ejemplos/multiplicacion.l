	   Z1<-X1
(B1)  IF Z1 != 0 GOTO A1
	   GOTO S
(A1)  Z1--
	   Z3<-X2
	   Z2<-Y
(D1)  IF Z2 != 0 GOTO C
	   GOTO D2
(C1)  Z2--
	   Z3++
	   GOTO D1
(D2)  Y<-Z3
	   GOTO B1
