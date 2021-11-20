public class Error {
    private int line;
    private String massage;
    private String lexeme;

    // lista de erros
    public static final String ERROR_FILE_NOT_FOUND = "ERRO: Arquivo nao encontrado!";
    public static final String ERROR_OPEN_FILE = "ERRO: Abertura do arquivo!";
    public static final String ERROR_READ_FILE = "ERRO: Leitura do arquivo!";
    public static final String ERROR_POINTER_OPEN_FILE = "ERRO: Ponteiro do arquivo de entrada!";
    public static final String ERROR_INVALID_CHARACTER = "ERRO: Caracter invalido!";
    public static final String ERROR_LEXEME_NOT_FOUND = "ERRO: Lexema nao identificado!";
    public static final String HASH_EMPTY = "ERRO: Hash vazia!";
    public static final String ERROR_ZERO_DIVISION = "ERRO: Nao existe divisao por 0!";

    public static final String ERROR_INVALID_SYNTAX = "ERRO AL: sintaxe invalida!";
    public static final String ERROR_LINE_BREAK_UNEXPECTED = "ERRO AL: Quebra de linha nao esperada!";
    public static final String ERROR_STRING_SIZE_EXCEEDED = "ERRO AL: Tamanho da String excedido!";
    public static final String ERROR_HEXADECIMAL_SIZE_EXCEEDED = "ERRO AL: Tamanho do hexadecimal excedido!";
    public static final String ERROR_LA_TOKEN_NOT_RECONIZED = "ERRO AL: Token nao reconhecido!";

    public static final String ERROR_TOKEN_NOT_EXPECTED = "ERRO AS: Token nao esperado!";
    public static final String ERROR_FINAL_FILE_NOT_EXPECTED = "ERRO AS: Final de arquivo nao esperado!";

    public Error(){ }

    public Error(String message){
        this.massage = message;
//        System.out.print(this.massage);
        System.exit(0);
    }

    public Error(String massage, int line){
        this.massage = massage;
        this.line = line;
        System.out.println(this.massage + " Linha: " + this.line);
        System.exit(0);
    }

    public Error(String massage, int line, String lexeme) {
        this.line = line;
        this.massage = massage;
        this.lexeme = lexeme;
        System.out.println(this.massage + " Linha: " + this.line + " Lexema: " + this.lexeme);
        System.exit(0);
    }

    @Override
    public String toString(){
        return ("Sintaxe da classe 'Error' : \n\tnew Error Error.<tipo do erro>, <numero da linha>, <caracter>");
    }
}
