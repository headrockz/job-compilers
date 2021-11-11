public class SyntacticAnalyzer {
    private IOFile io_file;
    private LexicalRegister lexical_register;
    private SymbolTable symbol_table;
    private LexicalAnalyzer lexical_analyzer;

    private String lexeme;
    private int token_current;

    public  SyntacticAnalyzer() { }

    public SyntacticAnalyzer(IOFile io_file, SymbolTable symbol_table, LexicalAnalyzer lexical_analyzer) {
        this.io_file = io_file;
        this.symbol_table = symbol_table;
        this.lexical_analyzer = lexical_analyzer;

        this.lexical_register = lexical_analyzer.getLexicalRegister();
        System.out.println(this.lexical_register.toString());
        this.token_current = this.lexical_register.getToken();
        this.lexeme = lexical_register.getLexeme();

        if (this.token_current == -1){
            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }

        this.S();

        if (this.token_current != 1) {
            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }
    }

    private void S(){}

    public void getMarriedToken(Integer a){} //trocar nome

    @Override
    public String toString(){
        return null;
    }
}
