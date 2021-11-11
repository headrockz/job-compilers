public class LexicalAnalyzer {
    private IOFile io_file;
    private LexicalRegister lexical_register; // trocar nome
    private SymbolTable symbol_table; // trocar nome

    private String lexeme;
    private int character, line, current_state, final_state, size_lexeme;
    private char[] valid_character;

    public LexicalAnalyzer() { }

    public LexicalAnalyzer(IOFile file, SymbolTable symbol_table) {
        this.io_file = file;
        this.symbol_table = symbol_table;
        this.lexical_register = new LexicalRegister();

        line = 1;

        this.valid_character = new char[] {' ', '_', '.', ',', ';', '(', ')', '/',
                '*', '+', '>', '<', '=', '\n', '{', '}'};

    }

    public LexicalRegister getLexicalRegister(){
        this.lexical_register = null;
        this.lexeme = "";
        this.current_state = 0;
        this.final_state = 14;
        this.size_lexeme = 0;
//        System.out.println(this.io_file.readChar());

        do {
            this.character = this.io_file.readByte();

            if (this.validCharacter(this.character)){

                switch (current_state){
                    case 0:
                        // se for quebra de linha ou espaco
                        if(this.character == ' ' || this.character == '\r' || this.character == '\n') {
                            this.current_state = 0;
                            if (this.character == '\n'){
                                this.line++;
                            }
                        }

                        // letra ou _
                        if (Character.isLetter((this.character)) || this.character == '_'){
                            this.lexeme = "" + ((char) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 1;
                        }
                        // digito 1-9
                        else if (Character.isDigit(this.character) && character != '0'){
                            this.lexeme = "" + ((char) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 2;
                        }
                        // "
                        else if (this.character == '"'){
                            current_state = 6;
                        }
                        // /
                        else if (this.character == '/'){
                            current_state = 10;
                        }
                        // {
                        else if (this.character == '{'){
                            current_state = 13;
                        }
                        //  ( ) + - * ; ,
                        else if (this.character == '(' || this.character == ')' || this.character == '+' ||
                                this.character == '-' || this.character == ';' || this.character == ',') {
                            this.lexeme = "" + ((char) this.character);
                            this.lexical_register = (LexicalRegister) symbol_table.searchSymbol(this.lexeme);

                            if(this.lexical_register == null){
                                // TODO: 10/11/2021 descomentar linha
                                this.lexical_register = this.symbol_table.insertSymbol(
                                        lexeme, new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                                current_state = final_state;
                            }
                        }
                        // EOF
                        else if (this.character == -1){
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            current_state = final_state;
                        }
                        // nao seja em string @ # ? ! % &
                        else if (this.character != -1) {
                            new Error (Error.ERROR_LEXEME_NOT_FOUND, this.line, "" + ((char) this.character));
                    }
                        break;
                    // letra ou _
                    case 1:
                        if (Character.isLetter(this.character) || this.character == '_'){
                            this.lexeme += (char) character;
                            this.size_lexeme++;

                            System.out.println(this.lexeme);
                        }
                        else if (!Character.isLetter(this.character) && this.character != '_'
                                && this.character != '\n') {
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                new LexicalRegister(this.symbol_table.getToken("id"), this.lexeme));
                            }
                            io_file.giveBack(-1);
                            this.current_state = this.final_state;
                        }

                        break;
                    // digito 1-9
                    case 2:
                        if (Character.isDigit(this.character)){
                            this.lexeme += (char) character;
                            this.size_lexeme++;

                        } else if (!Character.isLetterOrDigit(this.character) && this.character != '\n'){
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme));
                            }
                            io_file.giveBack(-1);
                            current_state = final_state;
                        }

                        break;
                    //digito comecando de 0
                    case 3:


                        break;
                    //
                    case 4:

                        break;
                    //
                    case 5:

                        break;
                    // string entre " "
                    case 6:
                        if(this.character != '"'){
                            this.lexeme += (char) character;
                            this.size_lexeme++;
                        } else if (this.character == '"') {
                            this.lexical_register = this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_register == null) {
                                this.lexical_register = this.symbol_table.insertSymbol(this.lexeme,
                                        new LexicalRegister(this.symbol_table.getToken("const"), this.lexeme));
                            }

                            io_file.giveBack(-1);
                            current_state = final_state;
                        }

                        break;
                    //
                    case 7:

                        break;
                    //
                    case 8:

                        break;
                    //
                    case 9:

                        break;
                    // divisao ou comentarios /* */
                    case 10:
                        //comentario
                        if (this.character == '*'){
                            this.current_state = 11;

                        } else { //divisao

                        }

                        break;
                    // comentarios /* */
                    case 11:
                        if (this.character != '/' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){
                            current_state = 13;
                        } else if (this.character == '/') {
                            current_state = 0;
                        } else if (this.character == '\n') {
                            this.line++;
                            current_state = 13;
                        }

                        break;
                    //
                    case 12:

                        break;
                    // comentarios entre { }
                    case 13:
                        if (this.character != '}' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){
                        } else if (this.character == '}') {
                            current_state = 0;
                        } else if (this.character == '\n') {
                            this.line++;
                        } else if (this.character == -1) {
                            this.lexical_register = new LexicalRegister(this.character, "EOF");
                            current_state = final_state;
                            new Error (Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((char) this.character));
                        }

                        break;
                    //
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
