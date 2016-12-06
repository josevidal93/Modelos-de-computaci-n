      # A diferencia de la macro definida en ILC
      # la ejecucion de "Y <- X - Z" con Z > X 
      # producira un bucle infinito
      Y<-X1
      Z1<-X2
(C1)  IF Z1 != 0 GOTO A1
      GOTO S
(A1)  IF Y != 0 GOTO B1
      GOTO A1
(B1)  Y--
      Z1--
      GOTO C1
