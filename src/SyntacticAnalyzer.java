public class SyntacticAnalyzer {
    private EsFile es_file;
    private LexicalRecord lexical_record; //trocar nome
    private SymbolTable symbol_table; //trocar nome
    private LexicalAnalyzer lexical_analyzer; //trocar nome

    private  String lexeme;
    private Integer token_current;

    public  SyntacticAnalyzer() { }

    public SyntacticAnalyzer(EsFile esfile, SymbolTable symbol_table, LexicalAnalyzer lexical_analyzer) {
        this.es_file = es_file;
        this.symbol_table = symbol_table;
        this.lexical_analyzer = lexical_analyzer;

        this.lexical_record = (LexicalRecord) lexical_analyzer.getLexicalRecord();
        this.token_current = this.lexical_record.getToken();
        this.lexeme = lexical_record.getLexeme();

        if (this.token_current == -1){
            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }

        this.S();

        if (this.token_current != 1) {
            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }
    }

    private void S(){}

    public void houseToken(Integer a){} //trocar nome

    public String toString(){
        return null;
    }
}
