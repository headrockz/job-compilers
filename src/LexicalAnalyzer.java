public class LexicalAnalyzer {
    private IOFile io_file;
    private LexicalRegister lexical_register;
    private SymbolTable symbol_table;

    private String lexeme;
    private int character, line, current_state, final_state, size_lexeme;
    private char[] valid_character;
    public String test = "";

    // TODO: 16/11/2021 debug
    private boolean debug = true;

    public LexicalAnalyzer() { }

    public LexicalAnalyzer(IOFile file, SymbolTable symbol_table) {
        this.io_file = file;
        this.symbol_table = symbol_table;
        this.lexical_register = new LexicalRegister();

        line = 1;
        this.valid_character = new char[] {' ', '_', '.', ',', ';', ':', '(', ')', '/',
                '*', '+', '>', '<', '=', '\n', '{', '}', '\r', '"', '-', '\0', '\t', '@', '?',
                '!', '|', '\u0000', '\uFFFF', '\uffff', '\\'};

    }

    public LexicalRegister getLexicalRegister(){
        this.lexical_register = null;
        this.lexeme = "";
        this.current_state = 0;
        this.final_state = 14;
        this.size_lexeme = 0;

        do {
            this.character = this.io_file.readByte();

            if (debug){
                test +=  (char) this.character;
            }

            if (this.validCharacter(this.character)){

                switch (current_state){
                    case 0:
                        // se for quebra de linha ou espaco
                        if(this.character == ' ' || this.character == '\r' || this.character == '\n') {
                            if (this.character == '\n'){
                                this.line++;
                            }
                        }
                        // letra ou _
                        else if (Character.isLetter((this.character)) || this.character == '_'){
                            this.lexeme = "" + (char) this.character;
                            this.size_lexeme = 1;
                            this.current_state = 1;
                        }
                        // digito 1-9
                        else if (Character.isDigit(this.character) && character != '0'){
                            this.lexeme = "" + (char) this.character;
                            this.size_lexeme = 1;
                            this.current_state = 2;
                        }
                        // digito comecando com 0
                        else if (this.character == '0'){
                            this.lexeme = "" + (char) this.character;
                            this.size_lexeme = 1;
                            this.current_state = 3;
                        }
                        // "
                        else if (this.character == '"'){
                            this.lexeme = "";
                            this.current_state = 6;
                        }
                        // =
                        else if (this.character == '='){
                            this.lexeme = "" + (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 7;
                        }
                        // >
                        else if (this.character == '>' || this.character == '='){
                            this.lexeme = "" + (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 8;
                        }
                        // <
                        else if (this.character == '<'){
                            this.lexeme = "" + (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 9;
                        }
                        // /
                        else if (this.character == '/'){
                            this.lexeme = "" + (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 10;
                        }
                        // {
                        else if (this.character == '{'){
                            this.current_state = 13;
                        }
                        //  ( ) + - * ; ,
                        else if (this.character == '(' || this.character == ')' || this.character == '+' ||
                                this.character == '-' || this.character == '*' ||
                                this.character == ';' || this.character == ',') {
                            this.lexeme = "" + ((char) this.character);
                            this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                this.current_state = this.final_state;
                            }

                            this.current_state = this.final_state;
                            System.out.println("lexema: " + this.lexeme);
                        }
                        // EOF
                        else if (this.character == -1){
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            this.current_state = this.final_state;
                        }
                        // nao seja em string @ # ? ! % &
                        else if (this.character != -1) {
                            new Error (Error.ERROR_LEXEME_NOT_FOUND, this.line, "" + ((char) this.character));
                        }

                    break;
                    // letra ou _
                    case 1:
                        if (Character.isLetterOrDigit(this.character) || this.character == '_'){
                            this.lexeme += (char) character;
                            this.size_lexeme++;
                            this.current_state = 1;

                        }
                        else if (!Character.isLetterOrDigit(this.character) || this.character != '_'
                                && this.character != '\n' || this.character != '\r' || this.character != -1) {
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);

                            if (this.lexical_register == null) {

                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                            }

                            io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }

                        if (debug){
//                            System.out.println("Estou no caso 1");
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // digito 1-9
                    case 2:
                        if (Character.isDigit(this.character)){
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = 2;

                        } else if (this.character != '\n' && this.character != '\r' && this.character != -1){
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);

                            if (this.lexical_register == null) {
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme));
                            }

                            this.io_file.giveBack(-1);
                            this.current_state = this.final_state;

                        } else if (this.character == '\n' || this.character == '\r'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    //digito comecando de 0
                    case 3:
                        if (Character.isDigit(this.character)){ //decimal
                            this.lexeme = String.valueOf((char) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 2;
                        } else if (this.character == 'h' || this.character == 'H'){ //hexadecimal
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = 4;
                        } else if(Character.isLetter(this.character) && this.character != 'h' && this.character != 'H'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        } else if (this.character != '\n' || this.character != '\r' || this.character != -1){
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);

                            if (this.lexical_register == null) {
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme));
                            }

                            this.io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        } else if (this.character == '\n' || this.character == '\r'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // hexadecimal
                    case 4:
                        if (Character.isDigit(this.character) || this.character == 'A' ||
                                this.character == 'B' || this.character == 'C' ||
                                this.character == 'D' || this.character == 'E' || this.character == 'F') {
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = 5;
                        } else if (Character.isLetter(this.character) && !Character.isDigit(this.character) &&
                                this.character == 'A' && this.character == 'B' && this.character == 'C' ||
                                this.character == 'D' && this.character == 'E' && this.character == 'F'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        }

                        if (debug){
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // hexadecimal
                    case 5:
                        char hexa = (char) this.lexeme.charAt(2);

                        if (this.size_lexeme <= 4) {
                            if (Character.isDigit(this.character) || this.character == 'A' ||
                                    this.character == 'B' || this.character == 'C' ||
                                    this.character == 'D' || this.character == 'E' || this.character == 'F') {
                                this.lexeme += (char) this.character;
                                this.size_lexeme++;
                                this.current_state = 5;
                            } else if (this.character != '\n' || this.character != '\r' || this.character != -1){
                                if (this.size_lexeme == 3){
                                    this.lexeme = "0h0" + hexa;
                                }
                                this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                                if (this.lexical_register == null) {

                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme));
                                }
                                this.io_file.giveBack(-1);
                                this.current_state = 0;
                            }

                        } else {
                            new Error(Error.ERROR_HEXADECIMAL_SIZE_EXCEEDED, this.line);
                            this.current_state = this.final_state;
                        }

                        if (debug){
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // string entre " "
                    case 6:
                        if (this.size_lexeme <= 255) {
                            if (this.character != '"' && this.character != '\n' &&
                                    this.character != '\r' && this.character != -1) {

                                this.lexeme += (char) character;
                                this.size_lexeme++;
                                this.current_state = 6;

                            } else if (this.character == '\n' || this.character == '\r') {
                                this.current_state = this.final_state;
                                new Error(Error.ERROR_LINE_BREAK_UNEXPECTED, this.line);
                            } else if (this.character == -1) {
                                this.current_state = this.final_state;
                                new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                        "" + ((char) this.character));
                            } else if (this.character == '"') {
                                this.lexeme += "$";
                                this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);

                                if (this.lexical_register == null) {

                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("const"),
                                                    this.lexeme));
                                }
                                this.current_state = this.final_state;
                            }
                        } else {
                            new Error(Error.ERROR_STRING_SIZE_EXCEEDED, this.getLine());
                        }

                        if (debug){
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // = ==
                    case 7:
                        if (this.character == '=') {
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            if (this.character != '\n' && this.character != '\r' && this.character != -1){
                                this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                                if (debug) {
                                    System.out.println("pesquinando na tabela: " + this.symbol_table.searchSymbol(this.lexeme));
                                }

                                if(this.lexical_register == null){
                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                }
                                io_file.giveBack(-1);
                                this.current_state = this.final_state;
                            }
                        } else if (this.character != '\n' && this.character != '\r' && this.character != -1){
                            this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                            }

                            io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }
                        else if (this.character == '\n' || this.character == '\r'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
//                            System.out.println("Estou no caso 7");
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // >  >=
                    case 8:
                        if (this.character == '=') {
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            if (this.character != '\n' && this.character != '\r' && this.character != -1){
                                this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                                if(this.lexical_register == null){
                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));

                                }

                                io_file.giveBack(-1);
                                this.current_state = this.final_state;
                            }
                        } else if (this.character != '\n' && this.character != '\r' && this.character != -1){
                            this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                            }

                            io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }
                        else if (this.character == '\n' || this.character == '\r'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_LINE_BREAK_UNEXPECTED, this.line);
                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
//                            System.out.println("Estou no caso 8");
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // < <= <>
                    case 9:
                        if (this.character == '>' || this.character == '=') {
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            if (this.character != '\n' && this.character != '\r' && this.character != -1){
                                this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                                if(this.lexical_register == null){
                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                }

                                io_file.giveBack(-1);
                                this.current_state = this.final_state;

                            }
                        } else if (this.character != '\n' && this.character != '\r' && this.character != -1){
                            this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                            }

                            io_file.giveBack(-1);
                            this.current_state = this.final_state;

                        }
                        else if (this.character == '\n' || this.character == '\r'){
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
//                            System.out.println("Estou no caso 9");
                            System.out.println("lexema: " + this.lexeme);
                        }

                        break;
                    // divisao ou comentarios /* */
                    case 10:
                        System.out.println("");

                        if (this.character == '*'){ //comentario
                            this.lexeme = "";
                            this.current_state = 11;
                        } else { //divisao
                             if (this.character != '\n' && this.character != '\r' && this.character != -1){
                                this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                                if (debug) {
                                    System.out.println("pesquinando na tabela: " + this.symbol_table.searchSymbol(this.lexeme));
                                }

                                if(this.lexical_register == null){
                                    this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                            new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                }
                                io_file.giveBack(-1);
                                this.current_state = 0;
                            }
                        }

                        break;
                    // comentarios /* */
                    case 11:
                        if (this.character != '*' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){

                            this.current_state = 11;

                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        } else if (this.character == '*') {
                            this.current_state = 12;
                        } else if (this.character == '\n' || this.character == '\r') {
                            this.current_state = 11;
                            if (this.character == '\n'){
                                this.line++;
                            }
                        }

                        break;
                    // comentarios /* */
                    case 12:
                        if (this.character != '/' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){

                            this.current_state = 11;

                        } else if (this.character == -1) {
                            this.current_state = this.final_state;
                            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        } else if (this.character == '/') {
                            this.current_state = 0;
                        } else if (this.character == '\n') {
                            this.current_state = 11;
                            this.line++;
                        }

                        break;
                    // comentarios entre { }
                    case 13:
                        if (this.character != '}' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){

                            this.current_state = 13;

                        } else if (this.character == '}') {
                            this.current_state = 0;
                        } else if (this.character == '\n') {
                            this.current_state = 13;
                            this.line++;
                        } else if (this.character == -1) {
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            this.current_state = this.final_state;
                            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        break;
                    // caso default
                    default:
                        new Error(Error.ERROR_LEXEME_NOT_FOUND, this.line, "" + ((char) this.character));

                } // end switch
            } else {
                new Error(Error.ERROR_INVALID_CHARACTER, this.getLine(), "" + ((char) this.character));
            }

        } while (this.character != -1 && current_state != final_state);

        return (this.lexical_register);
    }

    public Integer getLine() {
        return this.line;
    }

    public Boolean validCharacter(int character){
        boolean valid = false;

        for (int i = 0; i < valid_character.length; i++){
            if (valid_character[i] == (char) character) {
                valid = true;
                i = valid_character.length;
            }
        }
        if (!valid){
            valid = Character.isLetterOrDigit(character);
        }

        return valid;
    }

}
