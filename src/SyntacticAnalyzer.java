// S -> {D}* B
// D -> int id [= [+ | -] const] {, id [= [+ | -] const]}*;
//  | string id;
//  | boolean id;
//  | byte id;
//  | int id;
//  | final id = const;
// B -> begin {C}* end
// C -> (write | writeln) {,EXP}+;
//  | readln, id;
//  | while EXP B
//  | if EXP (C | B) [else (C | B)]
//  | id = EXP;
// EXP -> EXPS [(< | <= | > | >= | == | = | <>) EXPS]
// EXPS -> [+ | -] T {( + | - | or) T}*
// T -> F {(* | / | and) F}*
// F -> (id | const | not F | "(" EXP ")")

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

        this.symbol_table.displaysHashTable();
        System.out.println(this.lexical_analyzer.test);
        this.io_file.saveAssembler(this.code_generator);
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
                this.getMatchToken(symbol_table.getToken("int"));
                type = this.symbol_table.getType("int");

            } else if (this.token_current == symbol_table.getToken("byte")) {
                this.getMatchToken(symbol_table.getToken("byte"));
                type = this.symbol_table.getType("byte");

            } else if (this.token_current == symbol_table.getToken("boolean")) {
                this.getMatchToken(symbol_table.getToken("boolean"));
                type = this.symbol_table.getType("boolean");

            } else if (this.token_current == symbol_table.getToken("string")) {
                this.getMatchToken(symbol_table.getToken("string"));
                type = this.symbol_table.getType("string");
            }

            this.getMatchToken(symbol_table.getToken("id"));
            rl_temp_id = this.previous_lexical_register;
            rl_temp_id.setType(type);


            if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
                rl_temp_id.setClasss(this.symbol_table.getClass("var"));
            } else {
                new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
            }

            while (this.token_current == symbol_table.getToken(",")){
                this.getMatchToken(symbol_table.getToken(","));
                this.getMatchToken(symbol_table.getToken("id"));
                rl_temp_id = this.previous_lexical_register;
                rl_temp_id.setType(type);


                if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
                    rl_temp_id.setClasss(this.symbol_table.getClass("var"));
                } else {
                    new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
                }

                if (this.token_current == symbol_table.getToken("=")) {
                    this.getMatchToken(symbol_table.getToken("="));

                    if (this.token_current == symbol_table.getToken("-")) {
                        this.getMatchToken(symbol_table.getToken(""));
                        signal = "-";
                    } else {
                        this.getMatchToken(symbol_table.getToken("+"));
                        signal = "+";
                    }

                    this.getMatchToken(symbol_table.getToken("const"));
                    rl_temp_const = this.previous_lexical_register;
                    rl_temp_const.setType(rl_temp_id.getType());

                    if (rl_temp_const.getClasss() == this.symbol_table.getClass("empty")) {
                        rl_temp_const.setClasss(rl_temp_id.getClasss());
                    } else {
                        new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
                    }

                }
            }

            if (this.token_current == symbol_table.getToken("=")) {
                this.getMatchToken(symbol_table.getToken("="));

                if (this.token_current == symbol_table.getToken("-")) {
                    this.getMatchToken(symbol_table.getToken(""));
                    signal = "-";
                } else if (this.token_current == symbol_table.getToken("+")){
                    this.getMatchToken(symbol_table.getToken("+"));
                    signal = "+";
                }

                this.getMatchToken(symbol_table.getToken("const"));
                rl_temp_const = this.previous_lexical_register;
                rl_temp_const.setType(rl_temp_id.getType());

                if (rl_temp_const.getClasss() == this.symbol_table.getClass("empty")) {
                    rl_temp_const.setClasss(rl_temp_id.getClasss());
                } else {
                    new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
                }

            }

        } else {
            this.getMatchToken(symbol_table.getToken("final"));

            this.getMatchToken(symbol_table.getToken("id"));
            rl_temp_id = this.previous_lexical_register;

            if (rl_temp_id.getClasss() == this.symbol_table.getClass("empty")) {
                rl_temp_id.setClasss(this.symbol_table.getClass("const"));
            } else {
                new Error(Error.ERROR_IDENTIFIER_ALREADY_DECLARED);
            }

            if (this.token_current == symbol_table.getToken("=")) {
                this.getMatchToken(symbol_table.getToken("="));

                if (this.token_current == symbol_table.getToken("-")) {
                    this.getMatchToken(symbol_table.getToken(""));
                    signal = "-";
                } else if (this.token_current == symbol_table.getToken("+")){
                    this.getMatchToken(symbol_table.getToken("+"));
                    signal = "+";
                }

                this.getMatchToken(symbol_table.getToken("const"));
                rl_temp_const = this.previous_lexical_register;
                rl_temp_const.setClasss(rl_temp_id.getClasss());
                type = this.symbol_table.getType(rl_temp_const.getLexeme(), true);
                rl_temp_const.setType(type);
                rl_temp_id.setType(rl_temp_const.getType());

            }
        }

        this.getMatchToken(symbol_table.getToken(";"));

    }

    private void programBlock(){
        this.getMatchToken(symbol_table.getToken("begin"));

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
        this.getMatchToken(symbol_table.getToken("end"));
    }

    private void commands(){
        LexicalRegister rl_temp_id = null;
        LexicalRegister rl_temp_const = null;
        int type = -1;
        String signal = "";

        if (this.token_current == symbol_table.getToken("id")){
            this.getMatchToken(symbol_table.getToken("id"));

            rl_temp_id = this.previous_lexical_register;

            if (this.token_current == symbol_table.getToken("=")){
                this.getMatchToken(symbol_table.getToken("="));
                this.expressions();
            } else {
                new Error(Error.ERROR_TOKEN_NOT_EXPECTED, this.lexical_analyzer.getLine(), lexical_register.getLexeme());
            }

            this.getMatchToken(symbol_table.getToken(";"));

        } else if (this.token_current == symbol_table.getToken("if")){
            this.getMatchToken(symbol_table.getToken("if"));

            this.expressions();

            if (this.token_current == symbol_table.getToken("begin")){
                this.programBlock();
            }

        } else if (this.token_current == symbol_table.getToken("while")){
            this.getMatchToken(symbol_table.getToken("while"));

            this.expressions();

            if (this.token_current == symbol_table.getToken("begin")){
                this.programBlock();
            }

        } else if (this.token_current == symbol_table.getToken("readln")){
            this.getMatchToken(symbol_table.getToken("readln"));

            this.getMatchToken(symbol_table.getToken(","));

            this.getMatchToken(symbol_table.getToken("id"));

            this.getMatchToken(symbol_table.getToken(";"));

        } else if (this.token_current == symbol_table.getToken("write")){
            this.getMatchToken(symbol_table.getToken("write"));

            this.getMatchToken(symbol_table.getToken(","));

            this.getMatchToken(symbol_table.getToken("const"));

            if(this.token_current == symbol_table.getToken("const")){
                this.getMatchToken(symbol_table.getToken("const"));

                if (this.token_current == symbol_table.getToken(",")){
                    this.getMatchToken(symbol_table.getToken(","));

                    if(this.token_current == symbol_table.getToken("const")){
                        this.getMatchToken(symbol_table.getToken("const"));
                        this.expressions();
                    } else if (this.token_current == symbol_table.getToken("id")){
                        this.getMatchToken(symbol_table.getToken("id"));
                        this.expressions();
                    }
                }
            } else if (this.token_current == symbol_table.getToken("id")){
                this.getMatchToken(symbol_table.getToken("id"));

                if (this.token_current == symbol_table.getToken(",")){
                    this.getMatchToken(symbol_table.getToken(","));

                    if(this.token_current == symbol_table.getToken("const")){
                        this.getMatchToken(symbol_table.getToken("const"));
                        this.expressions();
                    } else if (this.token_current == symbol_table.getToken("id")){
                        this.getMatchToken(symbol_table.getToken("id"));
                        this.expressions();
                    }
                }
            }

            this.getMatchToken(symbol_table.getToken(";"));

        } else if (this.token_current == symbol_table.getToken("writeln")){
            this.getMatchToken(symbol_table.getToken("writeln"));

            this.getMatchToken(symbol_table.getToken(","));

            if(this.token_current == symbol_table.getToken("const")){
                this.getMatchToken(symbol_table.getToken("const"));

                if (this.token_current == symbol_table.getToken(",")){
                    this.getMatchToken(symbol_table.getToken(","));

                    if(this.token_current == symbol_table.getToken("const")){
                        this.getMatchToken(symbol_table.getToken("const"));
                        this.expressions();
                    } else if (this.token_current == symbol_table.getToken("id")){
                        this.getMatchToken(symbol_table.getToken("id"));
                        this.expressions();
                    }
                }
            } else if (this.token_current == symbol_table.getToken("id")){
                this.getMatchToken(symbol_table.getToken("id"));

                if (this.token_current == symbol_table.getToken(",")){
                    this.getMatchToken(symbol_table.getToken(","));

                    if(this.token_current == symbol_table.getToken("const")){
                        this.getMatchToken(symbol_table.getToken("const"));
                        this.expressions();
                    } else if (this.token_current == symbol_table.getToken("id")){
                        this.getMatchToken(symbol_table.getToken("id"));
                        this.expressions();
                    }
                }
            }

            this.getMatchToken(symbol_table.getToken(";"));

        }

    }

    private void expressions(){

        this.simpleExpressions();

        if (this.token_current == this.symbol_table.getToken("<")){
            this.getMatchToken(this.symbol_table.getToken("<"));
            this.simpleExpressions();
        } else if (this.token_current == this.symbol_table.getToken("<=")){
            this.getMatchToken(this.symbol_table.getToken("<="));
            this.simpleExpressions();
        } else if (this.token_current == this.symbol_table.getToken(">")){
            this.getMatchToken(this.symbol_table.getToken(">"));
            this.simpleExpressions();
        } else if (this.token_current == this.symbol_table.getToken(">=")){
            this.getMatchToken(this.symbol_table.getToken(">="));
            this.simpleExpressions();
        } else if (this.token_current == this.symbol_table.getToken("==")){
            this.getMatchToken(this.symbol_table.getToken("=="));
            this.simpleExpressions();
        } else if (this.token_current == this.symbol_table.getToken("<>")){
            this.getMatchToken(this.symbol_table.getToken("<>"));
            this.simpleExpressions();
        }
    }
    // EXP -> EXPS [(< | <= | > | >= | == | = | <>) EXPS]
// EXPS -> [+ | -] T {( + | - | or) T}*
// T -> F {(* | / | and) F}*
// F -> (id | const | not F | "(" EXP ")")

    private void simpleExpressions(){
        LexicalRegister rl_temp_id = null;
        LexicalRegister rl_temp_const = null;
        String signal = "";

        if (this.token_current == this.symbol_table.getToken("+")) {
            this.getMatchToken(this.symbol_table.getToken("+"));
            signal = "+";
        } else if (this.token_current == this.symbol_table.getToken("-")){
            this.getMatchToken(this.symbol_table.getToken("-"));
            signal = "-";
        }

        this.terms();

        while(this.token_current == this.symbol_table.getToken("+") ||
                this.token_current == this.symbol_table.getToken("-") ||
                this.token_current == this.symbol_table.getToken("or")){

            if (this.token_current == this.symbol_table.getToken("+")){
                this.getMatchToken(this.symbol_table.getToken("+"));
            } else if (this.token_current == this.symbol_table.getToken("-")){
                this.getMatchToken(this.symbol_table.getToken("-"));
            } else if (this.token_current == this.symbol_table.getToken("or")){
                this.getMatchToken(this.symbol_table.getToken("or"));
            }

            this.terms();
        }

    }

    private void terms(){

        this.factors();

        while (this.token_current == this.symbol_table.getToken("*") ||
                this.token_current == this.symbol_table.getToken("/") ||
                this.token_current == this.symbol_table.getToken("and")){

            this.factors();
        }
    }


    private void factors(){
        if(this.token_current == this.symbol_table.getToken("id")){
            this.getMatchToken(symbol_table.getToken("id"));

        } else if(this.token_current == this.symbol_table.getToken("const")){
            this.getMatchToken(symbol_table.getToken("const"));

        } else if(this.token_current == this.symbol_table.getToken("not")){
            this.getMatchToken(symbol_table.getToken("not"));

            this.factors();

        } else if(this.token_current == this.symbol_table.getToken("(")){
            this.getMatchToken(symbol_table.getToken("("));

            this.expressions();

            this.getMatchToken(symbol_table.getToken(")"));

        }
    }

    public void getMatchToken(int token_expected){
        if(token_expected == this.token_current){
            this.previous_lexical_register = this.lexical_register;
            this.lexical_register = lexical_analyzer.getLexicalRegister();
            this.token_current = lexical_register.getToken();
            this.lexeme = lexical_register.getLexeme();
        } else {
            new Error(Error.ERROR_TOKEN_NOT_EXPECTED, this.lexical_analyzer.getLine(), lexical_register.getLexeme());
        }
    }

    @Override
    public String toString(){
        return "Token Corrente: " + this.token_current + "\n" +
                "Lexema: " + this.lexeme + "\n" +
                "Linha Atual: " + this.lexical_analyzer.getLine() + "\n";
    }
}
