public class LexicalAnalyzer {
    private ES_File file;
    private LexicalRecord lexical; // trocar nome
    private SymbolTable symbol; // trocar nome
    private String lexeme;
    private Integer caracter;
    private Integer line;

    public LexicalAnalyzer(ES_File file) {
        this.file = file;
    }

    public LexicalRecord getLexicalRecord(){
        return null;
    }

    public Integer getLine() {
        return this.line;
    }

    public Boolean validCaracter(Integer a){ // trocar nome
        return false;
    }
}
