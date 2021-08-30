public class LexicalRecord {
    private Integer token;
    private String lexeme;

    public LexicalRecord(Integer token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
    }

    public Integer getToken() {
        return this.token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }


    public String getLexeme() {
        return this.lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }


    public String toString(){
        return null;
    }
}
