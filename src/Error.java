public class Error {
    private Integer line;
    private String massage;
    private String lexeme;

    // lista de erros
    public final String ERROR_FILE_NOT_FOUND = "ERRO: Arquivo nao encontrado!";
    public final String ERROR_OPEN_FILE = "ERRO: Abertura do arquivo!";
    public final String ERROR_READ_FILE = "ERRO: Leitura do arquivo!";
    public final String ERROR_POINTER_OPEN_FILE = "ERRO: ";
    public final String ERROR_LA_TOKEN_NOT_RECONIZED = "ERRO LA: Token nao reconhecido!";

    public final String ERROR_TOKEN_NOT_EXPECTED = "ERRO SA: Token nao esperado!";
    public final String ERROR_FINAL_FILE_NOT_EXPECTED = "ERRO SA: Final de arquivo nao esperado!";
    public final String ERROR_LEXEME_NOT_FOUND = "ERRO: Lexema nao identificado!";

    public Error(){ }

    public  Error(String message){
        this.massage = message;
        System.out.print(this.massage);
        System.exit(0);
    }

    public Error(int line, String massage){
        this.massage = massage;
        this.line = line;
        System.out.println(this.massage + "Linha: " + this.line);
    }

    public Error(Integer line, String massage, String lexeme) {
        this.line = line;
        this.massage = massage;
        this.lexeme = lexeme;
        System.out.println(this.massage + "Linha: " + this.line + "Lexema: " + this.lexeme);
    }

    @Override
    public String toString(){

        return ("Sintaxe da classe 'Error' : \n\tnew Error Error.<tipo do erro>, <numero da linha>, <caracter>");
    }
}
