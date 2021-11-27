public class LexicalRegister {
    private Integer token;
    private String lexeme;

    private int type, classs, address;

    public LexicalRegister(){
        this.token = 0;
        this.lexeme = "";
        this.type = -1;
        this.classs = 1;
    }

    public LexicalRegister(Integer token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
        this.type = -1;
        this.classs = 1;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClasss() {
        return classs;
    }

    public void setClasss(int classs) {
        this.classs = classs;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public String toString(){
        return ("Token: " + this.token + "\n" +
                "Lexema: " + this.lexeme + "\n" +
                "Tipo: " + this.type + "\n" +
                "Class: " + this.classs);
    }
}
