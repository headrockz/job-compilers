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
        this.token_current = this.lexical_register.getToken();
        this.lexeme = lexical_register.getLexeme();

        if (this.token_current == -1){
            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }

//        this.S();

        if (this.token_current != 1) {
            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, lexical_analyzer.getLine(),
                    "" + this.token_current);
        }
    }

    private void S(){
        while (this.token_current == symbol_table.getToken("int") ||
                this.token_current ==symbol_table.getToken("boolean") ||
                this.token_current ==symbol_table.getToken("byte") ||
                this.token_current ==symbol_table.getToken("string") ||
                this.token_current ==symbol_table.getToken("final"))
        {
            this.delarationBlock();
        }
        this.programBlock();
    }

    private void delarationBlock(){ }

    private void programBlock(){
        this.getMarriedToken(symbol_table.getToken("begin"));

        while (this.token_current == symbol_table.getToken("id") ||
                this.token_current ==symbol_table.getToken("while") ||
                this.token_current ==symbol_table.getToken("if") ||
                this.token_current ==symbol_table.getToken("readln") ||
                this.token_current ==symbol_table.getToken("write") ||
                this.token_current ==symbol_table.getToken("writeln"))
        {
            this.C();
        }
        this.getMarriedToken(symbol_table.getToken("end"));
    }

    private void C(){ }

    private LexicalRegister exp(){ return null; }

    private LexicalRegister expS(){ return null; }

    private LexicalRegister T(){ return null; }

    private LexicalRegister F(){ return null; }

    public void getMarriedToken(int token_expected){
        if(token_expected == this.token_current){
            this.lexical_register = lexical_analyzer.getLexicalRegister();
            this.token_current = lexical_register.getToken();
        }
    }

    @Override
    public String toString(){
        return null;
    }
}
