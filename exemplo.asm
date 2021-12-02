sseg SEGMENT STACK ;inicio seg. pilha
byte 4000h DUP(?) ;dimensiona pilha
sseg ENDS
dseg SEGMENT PUBLIC ;inicio seg. dados
byte 4000h DUP(?) ;temporarios
dseg ENDS ;fim seg. dados
cseg SEGMENT PUBLIC ;inicio seg. codigo
ASSUME CS:cseg, DS:dseg
strt: ;inicio do programa
cseg SEGMENT PUBLIC ;inicio seg. codigo
ASSUME CS:cseg, DS:dseg
strt: ;inicio do programa
mov ah,4Ch ;encerrar o programa
int 21h
cseg ENDS ;fecha segmento dados
END start; fim do programa
