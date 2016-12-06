# Tercer ejemplo: copia de X a Y no destructiva

(A1) IF X !=0 GOTO B1
       GOTO C1
(B1) X--
       Y++
        Z++
       GOTO A1
(C1) IF Z != 0 GOTO D1
        GOTO S1
(D1) Z--
        X++
        GOTO C1