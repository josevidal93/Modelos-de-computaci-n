#Funcion Fibonacci
    Z1++
    Z2++
    (A1)   Z3++
    Z2--  
    IF Z2 != 0 GOTO A1
    (B1)   Z4++
    Z2++
    Z1--
    IF Z1 != 0 GOTO B1
    (C1)   Z1++
    Z4--
       IF Z4 != 0 GOTO C1
    (D1)   Z1++
    Z3--
    IF Z3 != 0 GOTO D1
    X--
    IF X != 0 GOTO A1
    (A2)   Y++
    Z2--
    IF Z2 != 0 GOTO A2