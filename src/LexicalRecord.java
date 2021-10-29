public class LexicalRecord {
    private Integer token;
    private String lexeme;

    private int type;
    private int classs;
    private int address;

    public  LexicalRecord(){
        this.token = 0;
        this.lexeme = "";
    }

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
