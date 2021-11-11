import java.util.Enumeration;
import java.util.Hashtable;


public class SymbolTable {
    private final Integer TAM_MAX = 256;
    private LexicalRegister lexical_register;
    private Hashtable hash_table;
    private String[][] symbol_table;

    public SymbolTable() {
        hash_table = new Hashtable(TAM_MAX);

        this.loadSymbolTable();
    }

    private void loadSymbolTable() {
        this.symbol_table = new String[][]
                { // token, lexema, id, var, end
                        {"1", "id"},
                        {"2", "const"},
                        {"3", "final"},
                        {"4", "int"},
                        {"5", "byte"},
                        {"6", "string"},
                        {"7", "while"},
                        {"8", "if"},
                        {"9", "else"},
                        {"10", "and"},
                        {"11", "or"},
                        {"12", "not"},
                        {"13", "=="},
                        {"14", "="},
                        {"15", "("},
                        {"16", ")"},
                        {"17", "<"},
                        {"18", ">"},
                        {"19", "<>"},
                        {"20", ">="},
                        {"21", "<="},
                        {"22", ","},
                        {"23", "+"},
                        {"24", "-"},
                        {"25", "*"},
                        {"26", "/"},
                        {"27", ";"},
                        {"28", "begin"},
                        {"29", "end"},
                        {"30", "readln"},
                        {"31", "write"},
                        {"32", "writeln"},
                        {"2", "true"},
                        {"2", "false"},
                        {"35", "boolean"},
                };
        for (int i = 0; i <symbol_table.length; i++){
            this.lexical_register = new LexicalRegister(
                    Integer.parseInt(this.symbol_table[i][0]),
                    this.symbol_table[i][1]
            );
            this.insertSymbol(symbol_table[i][1], lexical_register);
        }
    }

    public LexicalRegister insertSymbol(String lexeme, LexicalRegister lexical_register) {
        this.hash_table.put(lexeme, lexical_register);
        // TODO: 10/11/2021 olhar isso aqui
        return ((LexicalRegister) this.hash_table.get(lexeme));
    }

    public void removeSymbol(String lexeme){

    }

    public LexicalRegister searchSymbol(String lexeme){
        System.out.println((LexicalRegister) this.hash_table.get(lexeme));
        return ((LexicalRegister) this.hash_table.get(lexeme));
    }

    public Integer getToken(String lexeme){
//        System.out.println(((LexicalRegister) hash_table.get(lexeme)).getToken());
        return (((LexicalRegister) hash_table.get(lexeme)).getToken());
    }

    public String getLexeme(){
        return (null);
    }

    public void displaysHashTable() {
        if (!hash_table.isEmpty()) {
            System.out.println("A hash contem " + hash_table.size() + " elementos: ");
            for (Enumeration n = this.hash_table.keys(); n.hasMoreElements();){
//                System.out.println(n.nextElement() + " " + this.hash_table.get(n.nextElement()));
                LexicalRegister lr1 = (LexicalRegister) this.hash_table.get(n.nextElement());
                System.out.println(lr1.getLexeme() + " (" + lr1.getToken() + ")");
            }
        } else {
            new Error(Error.HASH_EMPTY);
        }
    }

    @Override
    public String toString(){
        String symbol = "A tabela possui " + this.symbol_table.length + " registros.\n";
        for (int i = 0; i < this.symbol_table.length; i++){
            symbol += this.symbol_table[i][0] + "\nToken = " + this.symbol_table[i][0] +
                    "\tLexema = " + this.symbol_table[i][1] + "\n";
        }
        return (symbol);
    }
}
