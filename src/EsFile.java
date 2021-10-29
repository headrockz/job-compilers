import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

public class EsFile {
    private String name_file_input;
    private String name_file_output;
    private RandomAccessFile input_file;
    private RandomAccessFile output_file;

    public EsFile(String name_file_input) {
        this.name_file_input = name_file_input;
    }

    public EsFile() {
    }

    public void openFileInput(RandomAccessFile input_file){
        this.name_file_input = input_file;
        try {
            this.name_file_input = new RandomAccessFile(new File(this.name_file_input), "r");
        } catch (IOException e){
            new Error(Error.ERROR_OPEN_FILE);
        }
    }

    public void openFileOutput(RandomAccessFile output_file){
        this.name_file_output = output_file;
        try {
            this.name_file_output = new RandomAccessFile(new File(this.name_file_output), "r");
        } catch (IOException e){
            new Error(Error.ERROR_OPEN_FILE);
        }
    }

    public Integer readByte() {
        int character = 0;
        try {
            character = this.input_file.read();
        } catch (IOException e) {
            new Error(Error.ERROR_READ_FILE);
        }
        
        return (character);
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
        } catch (IOException e) {
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
