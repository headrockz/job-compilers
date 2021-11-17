public class LexicalRegister {
    private Integer token;
    private String lexeme;

    private int type, classs, address;

    public LexicalRegister(){
        this.token = 0;
        this.lexeme = "";
    }

    public LexicalRegister(Integer token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
    }

    public LexicalRegister(Integer token, String lexeme, int type, int classs) {
        this.token = token;
        this.lexeme = lexeme;
        this.type = type;
        this.classs = classs;
    }

    public Integer getToken() {
        return (this.token);
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public String getLexeme() {
        return (this.lexeme);
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public String toString(){
        return ("Token: " + this.token + "\n" +
                "Lexema: " + this.lexeme);
    }
}
