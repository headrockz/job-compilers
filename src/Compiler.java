public class Compiler {
    private IOFile io_file;
    private SymbolTable symbol_table;
    private LexicalAnalyzer lexical_analyzer;
    private SyntacticAnalyzer syntactic_analyzer;
    private Optimizer optimizer;


    public Compiler() { }

    public Compiler(String input_file, String output_file) {

        this.io_file = new IOFile();
        this.io_file.openFileInput(input_file);
        this.io_file.openFileOutput(output_file);

        this.symbol_table = new SymbolTable();
        this.lexical_analyzer = new LexicalAnalyzer(io_file, symbol_table);
        this.syntactic_analyzer = new SyntacticAnalyzer(this.io_file, this.symbol_table, this.lexical_analyzer);


        this.optimizer = new Optimizer();
    }

    @Override
    public String toString(){
        return null;
    }
}
