public class LexicalAnalyzer {
    private EsFile es_file;
    private LexicalRecord lexical_record; // trocar nome
    private SymbolTable symbol_table; // trocar nome

    private String lexeme;
    private Integer character, line, current_state, final_state, size_lexeme;
    private char[] valid_character;

    public LexicalAnalyzer() { }

    public LexicalAnalyzer(EsFile file, SymbolTable symbol_table) {
        this.es_file = file;
        this.symbol_table = symbol_table;
        this.lexical_record = new LexicalRecord();

        line = 1;

        this.valid_character = new char[] {' ', '_', '.', ',', ';', '(', ')', '/',
                '*', '+', '>', '<', '=', '\n', '{', '}'};

    }

    public LexicalRecord getLexicalRecord(){
        this.lexical_record = null;
        this.lexeme = "";
        this.current_state = 0;
        this.final_state = 14;
        this.size_lexeme = 0;

        do {
            this.character = es_file.readByte();

            if (this.validCharacter(this.character)){

                switch (current_state){
                    case 0:
                        // se nao for quebra de linha ou espaco
                        if(this.character == ' ' || this.character == '\r' || this.character == '\n') {
                            current_state = 0;
                            if (this.character == '\n'){
                                this.line++;
                            }
                        }

                        // letra ou _
                        if (Character.isLetter((this.character)) || this.character == '_'){
                            this.lexeme = "" + ((Character) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 1;
                        }
                        // digito 1-9
                        else if (Character.isDigit(this.character) && character != '0'){
                            this.lexeme = + ((Character) this.character);
                            this.size_lexeme = 1;
                            this.current_state = 3;
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
                            this.lexeme = "" + ((Character) this.character);
                            this.lexical_record = (LexicalRecord) symbol_table.searchSymbol(this.lexeme);
                            if(this.lexical_record == null){
                                this.lexical_record = this.symbol_table.insertSymbol(
                                        lexeme, new LexicalRecord(symbol_table.getToken(), this.lexeme));
                                current_state = final_state;
                            }
                        }
                        // EOF
                        else if (this.character == -1){
                            this.lexical_record = new LexicalRecord(this.character, "EOF");
                            current_state = final_state;
                        }
                        // nao seja em string @ # ? ! % &
                        else  if (this.character != -1) {
                            new ERROR (ERROR.ERROR_LEXEME_NOT_FOUND, this.line, "" + ((Character) this.character))
                    }
                        break;

                    // letra ou _
                    case 1:
                        if (Character.isLetterOrDigit(this.character) || this.character == '_'){
                            this.lexeme += (Character) character;
                            this.size_lexeme++;
                            current_state = 1;
                        }
                        else if (!Character.isLetterOrDigit(this.character) && this.character != '_'
                                && this.character != '\n') {
                            this.lexical_record = new this.symbol_table.searchSymbol(this.lexeme);
                            if (this.lexical_record == null) {
                                this.lexical_record = this.symbol_table.insertSymbol(this.lexeme,
                                new LexicalRecord(this.symbol_table.getToken("id"), this.lexeme))
                            }
                            es_file.giveBack(-1);
                            current_state = final_state;
                        }

                        break;
                    // digito 1-9
                    case 2:


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

                        break;
                    // comentarios /* */
                    case 11:

                        break;
                    //
                    case 12:

                        break;
                    // comentarios entre { }
                    case 13:
                        if (this.character != '}' && this.character != '\r' &&
                                this.character != '\n' && this.character != -1){
                            current_state = 13;
                        } else if (this.character == '}') {
                            current_state = 0;
                        } else if (this.character == '\n') {
                            this.line++;
                            current_state = 13;
                        } else if (this.character == -1) {
                            this.lexical_record = new LexicalRecord(this.character, "EOF");
                            current_state = final_state;
                            new Error(Error.ERROR_FINAL_FILE_NOT_EXPECTED, this.getLine(),
                                    "" + ((Character) this.character));
                        }

                        break;
                    //
                    default:
                        new Error(Error.ERROR_LEXEME_NOT_FOUND, this.line, "" + ((Character) this.character));

                // end switch
                }

            } else {
                new Error(Error.ERROR_INVALID_CHARACTER, this.getLine(), "" + ((Character) this.character));
            }
        } while (this.character != -1 && current_state != final_state);

        return (this.lexical_record);
    }

    public Integer getLine() {
        return this.line;
    }

    public Boolean validCharacter(Integer character){
        boolean valid = false;

        for (int i = 0; i < valid_character.length; i++){
            if (valid_character[i] == (Character) character) {
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
