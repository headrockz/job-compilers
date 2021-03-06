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
        // type:
        // -1 -> type_reserved
        // 1 -> type_boolean
        // 2 -> type_byte
        // 3 -> type_int
        // 4 -> type_string
        // class:
        // 1 -> class_empty
        // 2 -> class_var
        // 3 -> class_const
        // address

        this.symbol_table = new String[][]
                {      //token, lexeme, type, class, address
                        {"1", "id", "-1", "1", ""},
                        {"2", "const", "-1", "3", ""},
                        {"2", "true", "1", "3", ""},
                        {"2", "false", "1", "3", ""},
                        {"3", "final", "-1", "3", ""},
                        {"4", "int", "3", "1", ""},
                        {"5", "byte", "2", "1", ""},
                        {"6", "string", "4", "1", ""},
                        {"7", "while", "-1", "1", ""},
                        {"8", "if", "-1", "1", ""},
                        {"9", "else", "-1", "1", ""},
                        {"10", "and", "-1", "1", ""},
                        {"11", "or", "-1", "1", ""},
                        {"12", "not", "-1", "1", ""},
                        {"13", "==", "-1", "1", ""},
                        {"14", "=", "-1", "1", ""},
                        {"15", "(", "-1", "1", ""},
                        {"16", ")", "-1", "1", ""},
                        {"17", "<", "-1", "1", ""},
                        {"18", ">", "-1", "1", ""},
                        {"19", "<>", "-1", "1", ""},
                        {"20", ">=", "-1", "1", ""},
                        {"21", "<=", "-1", "1", ""},
                        {"22", ",", "-1", "1", ""},
                        {"23", "+", "-1", "1", ""},
                        {"24", "-", "-1", "1", ""},
                        {"25", "*", "-1", "1", ""},
                        {"26", "/", "-1", "1", ""},
                        {"27", ";", "-1", "1", ""},
                        {"28", "begin", "-1", "1", ""},
                        {"29", "end", "-1", "1", ""},
                        {"30", "readln", "-1", "1", ""},
                        {"31", "write", "-1", "1", ""},
                        {"32", "writeln", "-1", "1", ""},
                        {"33", "boolean", "1", "1", ""},
                };
        for (int i = 0; i <symbol_table.length; i++){
            this.lexical_register = new LexicalRegister(
                    Integer.parseInt(this.symbol_table[i][0]),
                    this.symbol_table[i][1],
                    Integer.parseInt(this.symbol_table[i][2]),
                    Integer.parseInt(this.symbol_table[i][3])
            );

            this.insertSymbol(symbol_table[i][1], lexical_register);
        }
    }

    public LexicalRegister insertSymbol(String lexeme, LexicalRegister lexical_register) {
        this.hash_table.put(lexeme, lexical_register);
        return ((LexicalRegister) this.hash_table.get(lexeme));
    }

    public void removeSymbol(String lexeme){

    }

    public LexicalRegister searchSymbol(String lexeme){
        return ((LexicalRegister) this.hash_table.get(lexeme));
    }

    public Integer getToken(String lexeme){
//        System.out.println(((LexicalRegister) hash_table.get(lexeme)).getToken());
        return (((LexicalRegister) hash_table.get(lexeme)).getToken());
    }

    // funcao chama a outra funcao caso id n??o seja final
    public Integer getType(String s_type) {
        return getType(s_type, false);
    }
    // is_final recebe true caso o id seja final e s_type recebe o lexema para descobrir o tipo
    public Integer getType(String s_type, boolean is_final){
        int type = -1;
        // -1 -> type_reserved
        // 1 -> type_boolean
        // 2 -> type_byte
        // 3 -> type_int
        // 4 -> type_string

        if (s_type.equals("boolean")) {
            type = 1;
        } else if (s_type.equals("byte")){
            type = 2;
        } else if (s_type.equals("int")){
            type = 3;
        } else if ((s_type.equals("string"))){
            type = 4;
        }

        if (is_final) {
            try {
                int temp_int = Integer.parseInt(s_type);
                if (temp_int >= -128 && temp_int <= 127) {
                    type = 2;
                } else {
                    type = 3;
                }
            } catch (NumberFormatException e) {
                if (s_type.equalsIgnoreCase("true") || s_type.equalsIgnoreCase("false")) {
                    type = 1;
                } else {
                    type = 4;
                }
            }
        }

        return (type);
    }

    public Integer getClass(String s_class){
        int classs = -1;
        // 1 -> class_empty
        // 2 -> class_var
        // 3 -> class_const

        if (s_class.equals("empty")){
            classs = 1;
        } else if (s_class.equals("var")){
            classs = 2;
        } else if (s_class.equals("const")) {
            classs = 3;
        }

        return (classs);
    }


    public void displaysHashTable() {
        if (!hash_table.isEmpty()) {
            System.out.println("A hash contem " + hash_table.size() + " elementos: ");
            for (Enumeration n = this.hash_table.keys(); n.hasMoreElements();){
//                System.out.println(n.nextElement() + " " + this.hash_table.get(n.nextElement()));
                LexicalRegister lr1 = (LexicalRegister) this.hash_table.get(n.nextElement());
                System.out.println("Lexema: " + lr1.getLexeme() + " Token: " + lr1.getToken() +
                        " Tipo: " + lr1.getType() + " Classe: " + lr1.getClasss());
            }
        } else {
            new Error(Error.HASH_EMPTY);
        }
    }

    public void itensHashTable() {
        if (!hash_table.isEmpty()) {
            System.out.println("A hash contem " + hash_table.size() + " elementos: ");
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
