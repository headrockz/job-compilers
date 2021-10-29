public class Compiler {
    private EsFile es_file;
    private SymbolTable sysmbol_table;
    private LexicalAnalyzer lexical_anayzer;
    private SyntacticAnalyzer syntactic_analyzer;
    private Optimizer optimizer;


    public Compiler() { }

    public Compiler(String input_file, String output_file) {

        es_file = new EsFile();
        es_file.openFileInput(input_file);
        es_file.openFileOutput(output_file);

        sysmbol_table = new SymbolTable();
        lexical_anayzer = new LexicalAnalyzer();
        syntactic_analyzer = new SyntacticAnalyzer(es_file, sysmbol_table, lexical_anayzer);
        optimizer = new Optimizer();

    }

    public String toString(){
        return null;
    }
}
