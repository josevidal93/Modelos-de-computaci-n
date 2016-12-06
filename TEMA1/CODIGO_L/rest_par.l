       Y <- X1
       Z <- X2
(C)  IF Z !=0 GOTO A
      GOTO S
(A)  IF Y != 0 GOTO B
      GOTO A
(B)  Y--
       Z--
      GOTO C