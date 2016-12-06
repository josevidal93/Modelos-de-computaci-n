#Funcion f(x1,x2)=x1^x2
       IF X2 !=0 GOTO A2
       Y<-1
       GOTO S1
(A2) Z2 <- 1
(A1) Z3 <- Z2*X1
       X2--
       Z2<-Z3
       IF X2 !=0 GOTO A1
       Y<- Z3