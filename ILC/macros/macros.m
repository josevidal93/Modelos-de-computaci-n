#
#  macros.m
#
# This file is part of ILC - http://ilc.sourceforge.net/
#
# ILC is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free
# Software Foundation; either version 2, or (at your option) any later
# version.
#
# ILC is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License
# along with ILC; see the file COPYING.  If not, write to the Free
# Software Foundation, 59 Temple Place - Suite 330, Boston, MA
# 02111-1307, USA.


#
# Las macros nos proporcionan la posibilidad de dar un nombre a un
# fragmento de codigo que usemos repetidamente y usarlo en otros
# programas como una abreviacion suya. A la instruccion que utilizamos
# como abreviacion la llamamos macro, y el fragmento de codigo que
# representa lo llamamos expansion de la macro.
#
# Para definir macros utilizaremos una notacion similar al lenguaje L,
# pero cambiando los identificadores de variables y de etiquetas. En
# una macro no se permite la utilizacion de las variables ni de las
# etiquetas propias del lenguaje L (variables X,Y,Z y etiquetas A,B,
# C,D,S). Esto evitara confusion a la hora de expandir la macro.
#
# En su lugar utilizaremos como identificadores de variables locales
# a la macro a W1...Wn y usaremos como identificadores de etiquetas
# locales a la macro a G1...Gn o F, este ultimo usado para definir el
# al final de la maro.
#


#
# Salto incondicional a la etiqueta T
#

MACRO { GOTO $ETIQ[T1] }
EXPANSION
{
         W1++
         IF W1 != 0 GOTO T1
}



#
# Inicializacion de la variable T1 a 0
#

MACRO { $VAR[T1] <- 0 }
EXPANSION
{
   (G1)  T1--
         IF T1 != 0 GOTO G1
}



#
# Asignacion (Esta macro tendra el efecto de copiar el valor de la variable
# T2 en la variable T1, dejando sin alterar el valor de la
# variable T2)
#

MACRO { $VAR[T1] <- $VAR[T2] }
EXPANSION
{
         w1 <- 0
         T1 <- 0
   (G1)  IF T2 != 0 GOTO G2
         GOTO G3
   (G2)  T2--
         T1++
         W1++
         GOTO G1
   (G3)  IF W1 !=0 GOTO G4
         GOTO F
   (G4)  W1--
         T2++
         GOTO G3
}



#
# Asignacion de un natural
#

MACRO { $VAR[T1] <- $NATURAL[T2] }
EXPANSION
{
         T1 <- 0
         %MESP_REP( T1++ ; 1 ; T2 )
}



#
# Suma (al igual que en la especificacion del libro, si el segundo 
# de los sumandos es la propia variable donde asignamos la suma, 
# "Y <- X + Y", esta operacion se transforma en "Y <- X + X". Y si el primer 
# operando es la misma variable donde asignamos la suma, "Y <- Y + X" la operacion se 
# transformaria en "Y <- X")
#

MACRO { $VAR[T3] <- $VAR[T1] + $VAR[T2] }
EXPANSION
{
         T3 <- T1
         W1 <- T2
   (G2)  IF W1 != 0 GOTO G1
         GOTO F
   (G1)  W1--
         T3++
         GOTO G2
}



#
# Suma con primer operando natural
#

MACRO { $VAR[T3] <- $NATURAL[T1] + $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- W1 + T2
}



#
# Suma con segundo operando natural
#

MACRO { $VAR[T3] <- $VAR[T1] + $NATURAL[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- T1 + W1
}



#
# Resta (equivalente a la funcion monus. Hacer notar que la ejecucion de la
# instruccion "Y <- X - Y" sera equivalente a la instruccion "Y <- X - X". Y la 
# ejecucion de la instruccion "Y <- Y - X" provocara un bucle infinito en la ejecucion)
#

MACRO { $VAR[T3] <- $VAR[T1] - $VAR[T2] }
EXPANSION
{
         IF (T1 > T2) GOTO G1
         T3 <- 0
         GOTO F
   (G1)  T3 <- T1
         W1 <- T2
   (G2)  IF W1 != 0 GOTO G3
         GOTO F
   (G3)  IF T3 != 0 GOTO G4
         GOTO G3
   (G4)  T3--
         W1--
         GOTO G2
}



#
# Resta con primer operando natural
#

MACRO { $VAR[T3] <- $NATURAL[T1] - $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- W1 - T2
}



#
# Resta con segundo operando natural
#

MACRO { $VAR[T3] <- $VAR[T1] - $NATURAL[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- T1 - W1
}



#
# Multiplicacion (La ejecucion de la instruccion "Y <- Y * X" o "Y <- Y * N" producira 
# siempre como salida 0 debido al planteamiento de la macro)
#

MACRO { $VAR[T3] <- $VAR[T1] * $VAR[T2] }
EXPANSION
{
         W2 <- T2
         T3 <- 0
   (G2)  IF W2 != 0 GOTO G1
         GOTO F
   (G1)  W2--
         W1 <- T1 + T3
         T3 <- W1
         GOTO G2
}



#
# Multiplicacion con primer operando natural
#

MACRO { $VAR[T3] <- $NATURAL[T1] * $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- W1 * T2
}



#
# Multiplicacion con segundo operando natural
#

MACRO { $VAR[T3] <- $VAR[T1] * $NATURAL[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- T1 * W1
}



#
# Division entera (La ejecucion de la instruccion "Y <- X / Y" o "Y <- N / Y" no producira
# los resultados esperados debido al planteamiento de la macro)
#

MACRO { $VAR[T3] <- $VAR[T1] / $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- 0
   (G1)  IF (W1 < T2) GOTO F
         W2 <- W1
         W1 <- W2 - T2
         T3++
         GOTO G1
}



#
# Division entera con primer operando natural
#

MACRO { $VAR[T3] <- $NATURAL[T1] / $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- W1 / T2
}



#
# Division entera con segundo operando natural
#

MACRO { $VAR[T3] <- $VAR[T1] / $NATURAL[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- T1 / W1
}



#
# Potencia (La ejecucion de la instruccion "Y <- Y pow X1" o "Y <- Y pow N" siempre 
# producira como salida 1 debido a la definicion de la macro)
#

MACRO { $VAR[T3] <- $VAR[T1] pow $VAR[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- 1
   (G1)  IF (W1 = 0) GOTO F
         W1--
         W2 <- T3
	      T3 <- W2 * T1
         GOTO G1
}



#
# Potencia con base natural
#

MACRO { $VAR[T3] <- $NATURAL[T1] pow $VAR[T2] }
EXPANSION
{
         W1 <- T1
         T3 <- W1 pow T2
}



#
# Potencia con exponente natural
#

MACRO { $VAR[T3] <- $VAR[T1] pow $NATURAL[T2] }
EXPANSION
{
         W1 <- T2
         T3 <- T1 pow W1
}



#
# Divisor de
#

MACRO { IF ( $VAR[T1] | $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         # Prevencion de errores en la expansion multiple de esta macro 
	      # en un programa en L
	      W2 <- 0
         W1 <- 0
   (G1)  W2 <- W1 * T1
         IF (T2 = W2) GOTO T3
         W1++
         IF (W1 > T2) GOTO F
         GOTO G1
}



#
# Divisor de con primer operando natural
#

MACRO { IF ( $NATURAL[T1] | $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
        W1 <- T1
        IF (W1 | T2) GOTO T3
}



#
# Divisor de con segundo operando natural
#

MACRO { IF ( $VAR[T1] | $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
        W1 <- T2
        IF (T1 | W1) GOTO T3
}



#
# Comparacion con 0
#

MACRO { IF ( $VAR[T1] = 0 ) GOTO $ETIQ[T2] }
EXPANSION
{
         IF T1 != 0 GOTO F
         GOTO T2
}



#
# Mayor que
#

MACRO { IF ( $VAR[T1] > $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         W2 <- T2
   (G1)  IF W1 != 0 GOTO G2
         GOTO F
   (G2)  IF W2 != 0 GOTO G3
         GOTO T3
   (G3)  W1--
         W2--
         GOTO G1
}



#
# Mayor que con primer operando natural
#

MACRO { IF ( $NATURAL[T1] > $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 > T2) GOTO T3
}



#
# Mayor que con segundo operando natural
#

MACRO { IF ( $VAR[T1] > $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 > W1) GOTO T3
}



#
# Menor que
#

MACRO { IF ( $VAR[T1] < $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         W2 <- T2
   (G3)  IF W2 != 0 GOTO G1
         GOTO F
   (G1)  IF W1 != 0 GOTO G2
         GOTO T3
   (G2)  W1--
         W2--
         GOTO G3
}



#
# Menor que con primer operando natural
#

MACRO { IF ( $NATURAL[T1] < $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 < T2) GOTO T3
}



#
# Menor que con segundo operando natural
#

MACRO { IF ( $VAR[T1] < $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 < W1) GOTO T3
}



#
# Mayor o igual que
#

MACRO { IF ( $VAR[T1] >= $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         IF (T1 > T2) GOTO T3
         IF (T1 = T2) GOTO T3
}



#
# Mayor o igual que con primer operando natural
#

MACRO { IF ( $NATURAL[T1] >= $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 >= T2) GOTO T3
}



#
# Mayor o igual que con segundo operando natural
#

MACRO { IF ( $VAR[T1] >= $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 >= W1) GOTO T3
}



#
# Menor o igual que
#

MACRO { IF ( $VAR[T1] <= $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         IF (T1 < T2) GOTO T3
         IF (T1 = T2) GOTO T3
}



#
# Menor o igual que con primer operando natural
#

MACRO { IF ( $NATURAL[T1] <= $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 <= T2) GOTO T3
}



#
# Menor o igual que con segundo operando natural
#

MACRO { IF ( $VAR[T1] <= $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 <= W1) GOTO T3
}



#
# Igualdad
#

MACRO { IF ( $VAR[T1] = $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         W2 <- T2
   (G1)  IF W1 != 0 GOTO G2
         IF W2 != 0 GOTO F
         GOTO T3
   (G2)  IF W2 != 0 GOTO G3
         GOTO F
   (G3)  W1--
         W2--
         GOTO G1
}



#
# Igualdad con primer operando natural
#

MACRO { IF ( $NATURAL[T1] = $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 = T2) GOTO T3
}



#
# Igualdad con segundo operando natural
#

MACRO { IF ( $VAR[T1] = $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 = W1) GOTO T3
}



#
# Desigualdad
#

MACRO { IF ( $VAR[T1] != $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         IF (T1 = T2) GOTO F
         GOTO T3
}



#
# Desigualdad con primer operando natural
#

MACRO { IF ( $NATURAL[T1] != $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T1
         IF (W1 = T2) GOTO T3
}



#
# Desigualdad con segundo operando natural
#

MACRO { IF ( $VAR[T1] != $NATURAL[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- T2
         IF (T1 != W1) GOTO T3
}


##########################################################
#       MACROS UTILIZADAS EN EL PROGRAMA UNIVERSAL       #
##########################################################


#
# Codificacion de un par
#

MACRO { $VAR[T1] <- < $VAR[T2], $VAR[T3] > }
EXPANSION
{
         W1 <- 2 pow T2
         W2 <- 2 * T3
         W2++
         T1 <- W1 * W2
         T1--
}



#
# Codificacion de un numero de godel cuya primer componente es L1, 
# la segunda L2 y la ultima es Ln
#

MACRO { $VAR[T1] <- [ L1...Ln ] }
EXPANSION
{
         w2 <- 0
         W4 <- 1
         W5 <- %Long_lista(L1...Ln)
   (G1)  W2++
         IF (W2 > W5) GOTO G2  
         W1 <- p (W2)
	      W6 <- P(L1...Ln)(W2)
         W3 <- W1 pow W6
         w8 <- w4
         W4 <- W8 * W3
         GOTO G1
   (G2)  T1 <- W4

}



#
# Parte izquierda
#

MACRO { $VAR[T1] <- l( $VAR[T2] ) }
EXPANSION
{
      W1 <- 0
      W3 <- T2 + 1
(G1)  W1++
      W2 <- 2 POW W1
      IF ( W2 | W3 ) GOTO G1
      T1 <- W1 - 1
}



#
# Parte derecrha
#

MACRO { $VAR[T1] <- r( $VAR[T2] ) }
EXPANSION
{
         W1 <- T2 + 1
         W2 <- l(T2)
         W3 <- 2 pow W2
         W4 <- W1 / W3
         W4--
         T1 <- W4 / 2
}



#
# Primo T1-esimo
#

MACRO { $VAR[T2] <- p( $VAR[T1] ) }
EXPANSION
{
      # Prevencion de errores en la expansion multiple de esta macro 
      # en codigos L
      W1 <- 0
      W2 <- 0
      W3 <- 0
      T2 <- 2
(G1)  W1++
      IF ( W1 = T2 ) GOTO G3
      IF ( W1 | T2 ) GOTO G2
      GOTO G1
(G2)  W2++
      GOTO G1
(G3)  IF ( W2 = 1 ) GOTO G4
(G5)  T2++
      W1 <- 0
      W2 <- 0
      GOTO G1
(G4)  W3++
      IF ( W3 = T1 ) GOTO F
      GOTO G5
}



#
# Primo T1-esimo con subindice natural
#

MACRO { $VAR[T2] <- p( $NATURAL[T1] ) }
EXPANSION
{
         W1 <- T1
         T2 <- p(W1)
}




#
# Proyeccion del elemento T1-esimo de un numero de godel
#

MACRO { $VAR[T3] <- ($VAR[T2])$VAR[T1] }
EXPANSION
{
   		# Prevencion de errores en el uso multiple de la macro 
		# proyeccion dentro de un mismo codigo L
		   W1 <- 0  
   (G1)  IF (W1 > T2) GOTO G2
         W2 <- p(T1)
         W3 <- W1 + 1
         W4 <- W2 pow W3
         IF (W4 | T2) GOTO G3
         GOTO G2
   (G3)  W1++
         GOTO G1
   (G2)  T3 <- W1
}



#
# Proyeccion del elemento T1-esimo de un numero de godel con 
# subindice natural
#

MACRO { $VAR[T3] <- ($VAR[T2])$NATURAL[T1] }
EXPANSION
{
         W1 <- T1
         T3 <- (T2)W1
}



#
# Longitud de un numero de godel
#

MACRO { $VAR[T1] <- Long($VAR[T2]) }
EXPANSION
{
   		 W1++  # Empezaremos haciendo la proyeccion del primer elemento
   (G2)  IF (W1 > T2) GOTO F
         W2 <- (T2)W1
         IF W2 != 0 GOTO G1
         W3 <- W1
   (G3)  W3++
         IF (W3 > T2) GOTO G4
         W4 <- (T2)W3
         IF W4 != 0 GOTO G1
         GOTO G3
   (G1)  W1++
         GOTO G2
   (G4) W1-- 
   		T1 <- W1
}



#
# Numero de orden de la etiqueta situada en la instruccion T2-esima
# del programa con codigo T3
#
MACRO { $VAR[T1] <- Etiq( $VAR[T2], $VAR[T3]) }
EXPANSION
{
         W1 <- T3 + 1
         W2 <- (W1)T2
         T1 <- l (W2)
}



#
# Numero de orden de la variable situada en la instruccion T2-esima
# del programa con codigo T3
#
MACRO { $VAR[T1] <- Var( $VAR[T2], $VAR[T3]) }
EXPANSION
{
         W1 <- T3 + 1
         W2 <- (T3)T2
         W3 <- r (W2)
         T1 <- r (W3)
         T1++
}



#
# Codigo de la instruccion T2-esima del programa con codigo T3
#
MACRO { $VAR[T1] <- Instr( $VAR[T2], $VAR[T3]) }
EXPANSION
{
         W1 <- T3 + 1
         W2 <- (T3)T2
         W3 <- r (W2)
         T1 <- l (W3)
}



#
# Codigo de la etiqueta a la que se salta cuando la instruccion T2-esima
# del programa con codigo T3 es una instruccion de salto (0 en el caso en
# que no lo sea)
#
MACRO { $VAR[T1] <- Etiq'( $VAR[T2], $VAR[T3]) }
EXPANSION
{
         W1 <- T3 + 1
         W2 <- (T3)T2
         W3 <- r (W2)
         W4 <- l (W3)
         T1 <- W4 - 2
}



#
# Delta_E (denota si el valor de la variable con codigo T1 es distinto de 0
# en el estado de programa codificado como T2)
#

MACRO { IF Delta_E( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- p(T1)
         IF (W1 | T2) GOTO T3
}



#
# Delta_DI (denota si el valor de la variable con codigo T1 es distinto de 0
# en el estado de programa codificado como T2)
#

MACRO { IF Delta_DI( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- p(T1)
         W2 <- r(T2)
         IF (W1 | W2) GOTO T3
}



#
# Descripcion instantanea inicial a partir de los valores de entrada L1...Ln
#

MACRO { $VAR[T1] <- Inic( L1...Ln ) }
EXPANSION
{
               W4 <- 1
               W5 <- %Long_lista(L1...Ln)
        (G1)   IF (W1 > W5 ) GOTO G2
               W2 <- 2 * W1
               W3 <- p(W2)
               W6 <- P(L1...Ln)(W1)
               W5 <- W3 pow W6
               W4 <- W4 * W5
               W1++
               GOTO G1
        (G2)   W5 <- 1
               T1 <- < W5, W4 >
}



#
# SKIP
#
MACRO { IF SKIP( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- l (T1)
         W2 <- Instr (W1, T2)
         IF W2 != 0 GOTO G1
         W3 <- T2 + 1
         W4 <- Long (W3)
         IF (W1 <= W4) GOTO T3
   (G1)  W5 <- Var (W1, T2)
         IF Delta_DI (W5, T1) GOTO F
         IF (W2 >= 2) GOTO T3
}



#
# INCR
#
MACRO { IF INCR( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- l (T1)
         W2 <- Instr (W1, T2)
         IF (W2 = 1) GOTO T3
}



#
# DECR
#
MACRO { IF DECR( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- l (T1)
         W2 <- Instr (W1, T2)
         IF (W2 != 2) GOTO F
         W3 <- Var (W1, T2)
         IF Delta_DI (W3, T1) GOTO T3
}



#
# SALTO
#
MACRO { IF SALTO( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- l (T1)
         W2 <- Instr (W1, T2)
         IF (W2 <= 2) GOTO F
         W3 <- Var (W1, T2)
         IF Delta_DI (W3, T1) GOTO G1
         GOTO F
   (G1)  W4 <- T2 + 1
         W5 <- Long (W4)
   (G2)  IF (W6 > W5) GOTO F
         W7 <- Etiq (W6, T2)
         W8 <- Etiq' (W1, T2)
         IF (W7 = W8) GOTO T3
         W6++
         GOTO G2
}



#
# Siguiente descripcion instantanea
#

MACRO { $VAR[T1] <- Suc ($VAR[T2], $VAR[T3]) }
EXPASION
{
         W1 <- l (T2)
         W2 <- r (T2)
         W3 <- W1 + 1
         IF SKIP (T2, T3) GOTO G1
         IF INCR (T2, T3) GOTO G2
         IF DECR (T2, T3) GOTO G3
         GOTO G4
   (G1)  T1 <- < W3, W2 >
         GOTO F
   (G2)  W4 <- Var (W1, T2)
         W5 <- p(W4)
         W6 <- W5 * W2
         T1 <- < W3, W6 >
         GOTO F
   (G3)  W4 <- Var (W1, T2)
         W5 <- p(W4)
         W6 <- W5 / W2
         T1 <- < W3, W6 >
         GOTO F
   (G4)  W4 <- T2 + 1
         W5 <- Long (W4)
         W5++
         T1 <- < W5, W2 >
}



#
# Codificacion de la descripcion instantanea del programa Ln-1 con las variables
# de entrada L1...Ln-2 despues de Ln pasos
#

MACRO { $VAR[T1] <- Inst( L1...Ln ) }
EXPANSION
{
         W1 <- Ln
         T1 <- Inic (L1...Ln-2)
   (G2)  IF W1 != 0 GOTO G1
         GOTO F
   (G1)  W1--
         W3 <- Suc (T1, Ln-1)
         T1 <- W3
         GOTO G2
}



#
# Final (salto a T3 cuando T1 represente una descripcion instantanea terminal
# del programa codificado por T2)
#

MACRO { IF FINAL ( $VAR[T1], $VAR[T2] ) GOTO $ETIQ[T3] }
EXPANSION
{
         W1 <- l (T1)
         W2 <- Long (T2)
         IF (W1 > W2) GOTO T3
}



#
# Pasos (cierto si existe el programa Ln-1 se detiene antes o en Ln pasos
# de ejecucion con las entradas L1...Ln-2)
#

MACRO { IF PASOS ( L1...Ln ) GOTO $ETIQ[T1] }
EXPANSION
{
         W1 <- Inst (L1...Ln)
         IF FINAL (W1, Ln-1) GOTO T1
}



#
# Funcion universal (devuelve el valor de la variable de salida del programa
# con codigo Ln cuando se ejecuta con las entradas L1...Ln-1)
#

MACRO { $VAR[T1] <-  Omega( L1...Ln ) }
EXPANSION
{
         W1 <- Inic(L1...Ln)
   (G1)  IF FINAL (W1, Ln) GOTO G2
         W2++
         W1 <- Inst(L1...Ln, W2)
         GOTO G1
   (G2)  W3 <- r (W1)
         T1 <- (W3)1
}
