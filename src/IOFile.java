import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOFile {
    private String name_file_input;
    private String name_file_output;
    private RandomAccessFile input_file;
    private RandomAccessFile output_file;

    public IOFile() {

    }

    public IOFile(String name_file_input) {
        this.name_file_input = name_file_input;
    }

    // metodos implementados

    public void openFileInput(String input_file){
        this.name_file_input = input_file;
        try {
            this.input_file = new RandomAccessFile(new File(this.name_file_input), "r");
        } catch (IOException e){
            new Error(Error.ERROR_OPEN_FILE);
        }
    }

    public void openFileOutput(String output_file){
        this.name_file_output = output_file;
        try {
            this.output_file = new RandomAccessFile(new File(this.name_file_output), "rw");
        } catch (IOException e){
            new Error (Error.ERROR_OPEN_FILE);
        }
    }

    public Integer readByte() {
        int character = 0;
        try {
            character = this.input_file.read();
        } catch (IOException e) {
            new Error (Error.ERROR_READ_FILE);
        }
        
        return (character);
    }
    public char readChar() {
        int character = 0;
        try {
            character = this.input_file.read();
        } catch (IOException e) {
            new Error (Error.ERROR_READ_FILE);
        }

        return ((char) character);
    }

    public String readLine(){
        String line = "";
        try {
            line = this.input_file.readLine();
        } catch (IOException e) {
            new Error(Error.ERROR_READ_FILE);
        }

        return (line);
    }

    public boolean checkOpenFile(String input_file){
        File file;
        boolean error = true;
        try {
            file = new File(input_file);
            error = file.exists();

        // @ TODO: 10/11/2021 olhar essa parte
        } catch (Exception e) {
            new Error(Error.ERROR_OPEN_FILE);
        }

        return (error);
    }

    public void giveBack(int point){
        try {
            if (this.input_file.getFilePointer() > 0){
                this.input_file.seek(this.input_file.getFilePointer() + point);
            }
        } catch (IOException e){
            new Error(Error.ERROR_POINTER_OPEN_FILE);
        }
    }

    @Override
    public String toString(){
        return ("Arquivo de entrada : " + this.input_file + "\n" +
                "Arquivo de saida : " + this.output_file + "\n");
    }
}
