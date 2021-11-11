/* Exemplo de Programa na linguagem L */

int n;
string nome;
boolean naoTerminou;
final MAXITER=10;

{ Bloco Principal }

begin
  write "Digite seu nome: ";
  readln nome;
  naoTerminou=true;
  n=0;
  while naoTerminou begin
    writeln,"Ola' ",nome;
    n=n+1;
    naoTerminou<=MAXITER;
  end
end