      Z2 <- X2
(B) IF Z2!=0 GOTO A
      GOTO S1
(A)  Z2-- 
      Z1 <- X1
      Z3 <- Y 
(B2) IF Z3!= 0 GOTO A2
       GOTO C2
(A2) Z3--
       Z1++
      GOTO B2
(C2) Y <- Z1
      GOTO B