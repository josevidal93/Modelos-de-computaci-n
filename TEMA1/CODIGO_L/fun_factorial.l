#Funcion f(x)=x!

       IF X1 !=0 GOTO A2
       Y<-1
       GOTO S1
(A2) Z2 <- 1
(A1) Z3 <- Z2*X1
       X1--
       Z2<-Z3
       IF X1 !=0 GOTO A1
       Y<- Z3