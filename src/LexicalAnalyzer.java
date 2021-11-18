public class LexicalAnalyzer {
    private IOFile io_file;
    private LexicalRegister lexical_register;
    private SymbolTable symbol_table;

    private String lexeme;
    private String test;
    private int character, line, current_state, final_state, size_lexeme;
    private char[] valid_character;
    char a = 13;

    // TODO: 16/11/2021 debug
    private boolean debug = true;
    private boolean tests = true;

    public LexicalAnalyzer() { }

    public LexicalAnalyzer(IOFile file, SymbolTable symbol_table) {
        this.io_file = file;
        this.symbol_table = symbol_table;
        this.lexical_register = new LexicalRegister();

        line = 1;
        char a = 13;
        this.valid_character = new char[] {' ', '_', '.', ',', ';', ':', '(', ')', '/',
                '*', '+', '>', '<', '=', '\n', '{', '}', '\r', '"', '\0', '\t', '@', '?',
                '!', '|', '\u0000', '\uFFFF', '\uffff', '\\', '\u0013'};

    }

    public LexicalRegister getLexicalRegister(){
        this.lexical_register = null;
        this.lexeme = "";
        // TODO: 16/11/2021 debug
        this.test = "";
        this.current_state = 0;
        this.final_state = 14;
        this.size_lexeme = 0;

        do {
            this.character = this.io_file.readByte();

            // TODO: 16/11/2021 debug
            if (debug){
//            System.out.println("lex: " + this.lexeme);
//            System.out.println("char: " + (char) this.character);
            }

            if (this.validCharacter(this.character)){

                switch (current_state){
                    case 0:
                        this.test = "";
                        // se for quebra de linha ou espaco
                        if(this.character == ' ' || this.character == '\r' || this.character == '\n' || this.character == a) {
                            if (this.character == '\n'){
                                this.line++;
                            }
                        }
                        // letra ou _
                        if (Character.isLetter((this.character)) || this.character == '_'){
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
                        else if (this.character == 0){
                            this.lexeme += (char) this.character;
                            this.size_lexeme = 1;
                            this.current_state = 3;
                        }
                        // "
                        else if (this.character == '"'){
                            this.current_state = 6;
                        }
                        // /
                        else if (this.character == '/'){
                            this.lexeme += (char) character;
                            this.test += (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 10;
                        }
                        // < >
                        else if (this.character == '<' || this.character == '>'){
                            this.lexeme += (char) character;
                            this.size_lexeme = 1;
                            this.current_state = 7;
                        }
                        // {
                        else if (this.character == '{'){
                            this.test += (char) character;
                            this.current_state = 13;
                        }
                        //  ( ) + - * ; , :
                        else if (this.character == '(' || this.character == ')' || this.character == '+' ||
                                this.character == '-' || this.character == ';' || this.character == ','
                                || this.character == ':') {
                            this.lexeme += ((char) this.character);
                            this.lexical_register = symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                this.current_state = this.final_state;
                            }
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

                        if (debug){
                            System.out.println("Estou no caso 0");
                        }

                    break;
                    // letra ou _
                    case 1:
                        if (Character.isLetterOrDigit(this.character) || this.character == '_'){
                            this.lexeme += (char) character;
                            this.size_lexeme++;
                            this.current_state = 1;

                        }
                        else if (!Character.isLetterOrDigit(this.character) && this.character != '_'
                                && this.character != '\n' || this.character != '\r' || this.character != ' ') {
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {

                                String type = "3";
                                String classs = "2";

                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme,
                                        Integer.parseInt(type), Integer.parseInt(classs)));
                            }
                            io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }

                        if (debug){
                            System.out.println("Estou no caso 1");
                        }

                        break;
                    // digito 1-9
                    case 2:
                        if (Character.isDigit(this.character)){
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = 2;

                        } else if (this.character != '\n' || this.character != '\r' ||
                                this.character != ' ' || this.character == '\n'){
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {

                                String type;
                                String classs = "2";

                                if (Integer.parseInt(this.lexeme) >= -128 && Integer.parseInt(this.lexeme) <= 127) {
                                    type = "0";
                                } else {
                                    type = "1";
                                }

                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme,
                                                Integer.parseInt(type), Integer.parseInt(classs)));
                            }
                            this.io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }

                        if (debug){
                            System.out.println("Estou no caso 2");
                        } else {
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        }

                        break;
                    //digito comecando de 0
                    case 3:
                        if (Character.isDigit(this.character)){ //decimal
                            this.lexeme = String.valueOf((char) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 2;
                        } else if (this.character == 'h'){ //hexadecimal
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = 4;
                        } else if (this.character != '\n' || this.character != '\r' ||
                                this.character != ' ' || this.character == '\n'){
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {

                                String type;
                                String classs = "2";

                                if (Integer.parseInt(this.lexeme) >= -128 && Integer.parseInt(this.lexeme) <= 127) {
                                    type = "0";
                                } else {
                                    type = "1";
                                }

                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme,
                                                Integer.parseInt(type), Integer.parseInt(classs)));
                            }
                            this.io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        } else {
                            new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                        }

                        if (debug){
                            System.out.println("Estou no caso 3");
                        }

                        break;
                    // hexadecimal
                    case 4:

                        break;
                    //
                    case 5:

                        break;
                    // string entre " "
                    case 6:
                        if (this.character != '"' && this.character != '\n' &&
                                this.character != '\r' && this.character != -1) {

                            this.lexeme += (char) character;
                            this.size_lexeme++;
                            this.current_state = 6;

                        } else if (this.character == '\n' || this.character == '\r'){

                        } else if (this.character == -1){

                        } else if (this.character == '"') {
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);

                            if (debug){
                                System.out.println("pesquinando na tabela: " + this.symbol_table.searchSymbol(this.lexeme));
                            }
                            if (this.lexical_register == null) {

                                String type = "3";
                                String classs = "2";

                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"),
                                                this.lexeme, Integer.parseInt(type), Integer.parseInt(classs)));
                            }

                            this.current_state = this.final_state;
                        }

                        if (debug){
                            System.out.println("Estou no caso 6");
                        }
                        break;
                    // < >
                    case 7:
                        if (this.character == '>' || this.character == '='){
                            this.lexeme += (char) this.character;
                            this.size_lexeme++;
                            this.current_state = this.final_state;
                        } else {

                        }
                        break;
                    //
                    case 8:

                        break;
                    // soma subtracao multipicao
                    case 9:
//                        if (this.character == '+'){
//
//                        }

                        break;
                    // divisao ou comentarios /* */
                    case 10:
                        if (this.character == '*'){ //comentario
                            this.lexeme = "";
                            this.current_state = 11;

                            if (tests){
                                this.test += (char) this.character;
                            }

                        } else { //divisao
                            if (Character.isDigit(this.character)){
                                if (this.character == 0){
                                    new Error(Error.ERROR_ZERO_DIVISION, this.line);
                                } else if (this.character == '\n' || this.character == '\r'){
                                    this.line++;
                                    new Error(Error.ERROR_INVALID_SYNTAX, this.line);
                                } else {
                                    this.current_state = this.final_state;
                                }
                            }
                        }

                        if (debug){
                            System.out.println("Estou no caso 10");
                        }

                        break;
                    // comentarios /* */
                    case 11:
                        if (this.character != '*' && this.character == '\r' &&
                                this.character == '\n' && this.character == -1){

                            this.current_state = 11;

                        } else if (this.character == -1) {
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            this.current_state = 0;
                        } else if (this.character == '*') {
                            this.current_state = 12;
                        } else if (this.character == '\n') {
                            this.line++;
                            this.current_state = 0;
                        }

                        if (debug){
                            System.out.println("Estou no caso 11");
                        }

                        if (tests){
                            this.test += (char) this.character;
                            System.out.println(this.test);
                            this.test = "";
                        }

                        break;
                    // comentarios /* */
                    case 12:
                        if (this.character != '/' && this.character == '\r' &&
                                this.character == '\n' && this.character == -1){

                            this.current_state = 11;

                        } else if (this.character == -1) {
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            this.current_state = this.final_state;
                        } else if (this.character == '/') {
                            this.test += (char) this.character;
                            this.current_state = 0;
                        } else if (this.character == '\n' || this.character == '\r') {
//                            this.io_file.giveBack(-1);
//                            this.line++;
//                            this.current_state = 0;
                        }

                        if (debug){
                            System.out.println("Estou no caso 12");
                        }

                        if (tests){
                            this.test += (char) this.character;
                            System.out.println(this.test);
                            this.test = "";
                        }


                        break;
                    // comentarios entre { }
                    case 13:
                        System.out.println("");
                        if (this.character != '}' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){

                            this.current_state = 13;

                        } else if (this.character == '}') {
                            this.current_state = 0;
                        } else if (this.character == '\n') {
//                            this.lexical_register = new LexicalRegister();
                            this.current_state = 13;
                            this.line++;
                        } else if (this.character == -1) {
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            this.current_state = this.final_state;
                            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        if (debug){
                            System.out.println("Estou no caso 13");
                        }

                        if (tests){
                            this.test += (char) this.character;
                            System.out.println(this.test);

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

        // TODO: 16/11/2021 debug
        if (debug){
//            symbol_table.displaysHashTable();
            symbol_table.itensHashTable();
//            System.out.println("linha: " + getLine());
            System.out.println("lexema: " + this.lexeme);
            this.lexeme = "";
        }

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
