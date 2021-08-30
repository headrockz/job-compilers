import java.util.StringTokenizer;

public class Error {
    private Integer line;
    private String massage;
    private String lexeme;
    private StringTokenizer tokenizer;
    // implementar lista de erros


    public Error(Integer line, String massage, String lexeme) {
        this.line = line;
        this.massage = massage;
        this.lexeme = lexeme;
    }

    public String toString(){
        return null;
    }
}
