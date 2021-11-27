// S -> {D}* B
// D -> int id [= [+ | -] const] {, id [= [+ | -] const]}*;
//  | string id;
//  | boolean id;
//  | byte id;
//  | int id;
//  | final id = const, id = const;
// B -> begin {C}* end
// C -> (write | writeln) {,EXP}+;
//  | readln, id;
//  | while EXP B
//  | if EXP (C | B) [else (C | B)]
//  | id = EXP;

public class SyntacticAnalyzer {
    private IOFile io_file;
    private SymbolTable symbol_table;
    private LexicalAnalyzer lexical_analyzer;
    private LexicalRegister lexical_register;
    private LexicalRegister previous_lexical_register;

    private String lexeme, code_generator;
    private int token_current;

    public  SyntacticAnalyzer() { }

    public SyntacticAnalyzer(IOFile io_file, SymbolTable symbol_table, LexicalAnalyzer lexical_analyzer) {
        this.io_file = io_file;
        this.symbol_table = symbol_table;
        this.lexical_analyzer = lexical_analyzer;
        this.code_generator = "";

        this.lexical_register = lexical_analyzer.getLexicalRegister();
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

        this.saveAssembler(this.code_generator);
    }

    private void S(){
        this.code_generator += "sseg SEGMENT STACK ;inicio seg. pilha\n";
        this.code_generator += "byte 4000h DUP(?) ;dimensiona pilha\n";
        this.code_generator += "sseg ENDS\n";

        this.code_generator += "dseg SEGMENT PUBLIC ;inicio seg. dados\n";
        this.code_generator += "byte 4000h DUP(?) ;temporarios\n";

        while (this.token_current == symbol_table.getToken("int") ||
                this.token_current == symbol_table.getToken("boolean") ||
                this.token_current == symbol_table.getToken("byte") ||
                this.token_current == symbol_table.getToken("string") ||
                this.token_current == symbol_table.getToken("final")) {
            this.declarationBlock();
        }

        this.code_generator += "dseg ENDS ;fim seg. dados\n";

        this.programBlock();

        this.code_generator += "mov ah,4Ch ;encerrar o programa\n";
        this.code_generator += "int 21h\n";
        this.code_generator += "cseg ENDS ;fecha segmento dados\n";
        this.code_generator += "END start; fim do programa\n";
    }

    private void declarationBlock(){
        LexicalRegister rl_temp_id = null;
        LexicalRegister rl_temp_const = null;
        int type = -1;
        String signal = "";

        if (this.token_current != symbol_table.getToken("final")) {
            if (this.token_current == symbol_table.getToken("int")) {
                this.getMarriedToken(symbol_table.getToken("int"));
                type = this.symbol_table.getType("int");

            } else if (this.token_current == symbol_table.getToken("byte")) {
                this.getMarriedToken(symbol_table.getToken("byte"));
                type = this.symbol_table.getType("byte");

            } else if (this.token_current == symbol_table.getToken("boolean")) {
                this.getMarriedToken(symbol_table.getToken("boolean"));
                type = this.symbol_table.getType("boolean");

            } else if (this.token_current == symbol_table.getToken("string")) {
                this.getMarriedToken(symbol_table.getToken("string"));
                type = this.symbol_table.getType("string");
            }

            this.getMarriedToken(symbol_table.getToken("id"));
            rl_temp_id = this.previous_lexical_register;
            rl_temp_id.setType(type);


            if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
                rl_temp_id.setClasss(this.symbol_table.getClass("var"));
            } else {
                new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
            }

//            while (this.token_current == symbol_table.getToken(",")){
//                this.getMarriedToken(symbol_table.getToken(","));
//                this.getMarriedToken(symbol_table.getToken("id"));
//                rl_temp_id = this.previous_lexical_register;
//                rl_temp_id.setType(type);
//
//
//                if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
//                    rl_temp_id.setClasss(this.symbol_table.getClass("var"));
//                } else {
//                    new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
//                }
//
//                if (this.token_current == symbol_table.getToken("=")) {
//                    this.getMarriedToken(symbol_table.getToken("="));
//
//                    if (this.token_current == symbol_table.getToken("-")) {
//                        this.getMarriedToken(symbol_table.getToken(""));
//                        signal = "-";
//                    } else {
//                        this.getMarriedToken(symbol_table.getToken("+"));
//                        signal = "+";
//                    }
//
//                    this.getMarriedToken(symbol_table.getToken("const"));
//                    rl_temp_const = this.previous_lexical_register;
//                    rl_temp_const.setType(rl_temp_id.getType());
//
//                    if (rl_temp_const.getClasss() == this.symbol_table.getClass("empty")) {
//                        rl_temp_const.setClasss(rl_temp_id.getClasss());
//                    } else {
//                        new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
//                    }
//
//                }
//            }

            if (this.token_current == symbol_table.getToken("=")) {
                this.getMarriedToken(symbol_table.getToken("="));

                if (this.token_current == symbol_table.getToken("-")) {
                    this.getMarriedToken(symbol_table.getToken(""));
                    signal = "-";
                } else {
                    this.getMarriedToken(symbol_table.getToken("+"));
                    signal = "+";
                }

                this.getMarriedToken(symbol_table.getToken("const"));
                rl_temp_const = this.previous_lexical_register;
                rl_temp_const.setType(rl_temp_id.getType());

                if (rl_temp_const.getClasss() == this.symbol_table.getClass("empty")) {
                    rl_temp_const.setClasss(rl_temp_id.getClasss());
                } else {
                    new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
                }

            }

        } else {
            this.getMarriedToken(symbol_table.getToken("final"));

            this.getMarriedToken(symbol_table.getToken("id"));
            rl_temp_id = this.previous_lexical_register;

            if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
                rl_temp_id.setClasss(this.symbol_table.getClass("const"));
            } else {
                new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
            }

            if (this.token_current == symbol_table.getToken("=")) {
                this.getMarriedToken(symbol_table.getToken("="));

                if (this.token_current == symbol_table.getToken("-")) {
                    this.getMarriedToken(symbol_table.getToken(""));
                    signal = "-";
                } else {
                    this.getMarriedToken(symbol_table.getToken("+"));
                    signal = "+";
                }

                this.getMarriedToken(symbol_table.getToken("const"));
                rl_temp_const = this.previous_lexical_register;
                rl_temp_const.setClasss(rl_temp_id.getClasss());
                type = this.symbol_table.getType(rl_temp_const.getLexeme(), true);
                rl_temp_const.setType(type);
                rl_temp_id.setType(rl_temp_const.getType());

            }
        }

        this.getMarriedToken(symbol_table.getToken(";"));

        this.symbol_table.displaysHashTable();

    }

    private void programBlock(){
        this.getMarriedToken(symbol_table.getToken("begin"));

        this.code_generator += "cseg SEGMENT PUBLIC ;inicio seg. codigo\n";
        this.code_generator += "ASSUME CS:cseg, DS:dseg\n";
        this.code_generator += "strt: ;inicio do programa\n";

        while (this.token_current == symbol_table.getToken("id") ||
                this.token_current == symbol_table.getToken("while") ||
                this.token_current == symbol_table.getToken("if") ||
                this.token_current == symbol_table.getToken("readln") ||
                this.token_current == symbol_table.getToken("write") ||
                this.token_current == symbol_table.getToken("writeln"))
        {
            this.commands();
        }
        this.getMarriedToken(symbol_table.getToken("end"));
    }

    private void commands(){
        if (this.token_current == symbol_table.getToken("id")){
            this.getMarriedToken(symbol_table.getToken("id"));

        } else if (this.token_current == symbol_table.getToken("if")){
            this.getMarriedToken(symbol_table.getToken("if"));


        } else if (this.token_current == symbol_table.getToken("while")){
            this.getMarriedToken(symbol_table.getToken("while"));

        } else if (this.token_current == symbol_table.getToken("readln")){
            this.getMarriedToken(symbol_table.getToken("readln"));

        } else if (this.token_current == symbol_table.getToken("write")){
            this.getMarriedToken(symbol_table.getToken("write"));

            this.getMarriedToken(symbol_table.getToken(","));

            this.getMarriedToken(symbol_table.getToken("const"));
            this.expressions();

        } else if (this.token_current == symbol_table.getToken("writeln")){
            this.getMarriedToken(symbol_table.getToken("writeln"));

            this.getMarriedToken(symbol_table.getToken(","));

            this.getMarriedToken(symbol_table.getToken("const"));
            this.expressions();



        }
    }

    private LexicalRegister expressions(){ return null; }

    private LexicalRegister simpleExpressions(){ return null; }

    private LexicalRegister T(){ return null; }

    private LexicalRegister F(){ return null; }

    public void getMarriedToken(int token_expected){
        if(token_expected == this.token_current){
            this.previous_lexical_register = this.lexical_register;
            this.lexical_register = lexical_analyzer.getLexicalRegister();
            this.token_current = lexical_register.getToken();
        } else {
            new Error(Error.ERROR_TOKEN_NOT_EXPECTED, this.lexical_analyzer.getLine(), lexical_register.getLexeme());
        }
    }

    private void saveAssembler(String code_generator) {

    }

    @Override
    public String toString(){
        return "Token Corrente: " + this.token_current + "\n" +
                "Lexema: " + this.lexeme + "\n" +
                "Linha Atual: " + this.lexical_analyzer.getLine() + "\n";
    }
}
