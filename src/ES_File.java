import java.io.RandomAccessFile;
import java.util.RandomAccess;

public class ES_File {
    private String name_file_input;
    private String name_file_output;
    private RandomAccessFile input_file;
    private RandomAccessFile output_file;

    public ES_File(String name_file_input) {
        this.name_file_input = name_file_input;
    }

    public void openFileInput(RandomAccessFile input_file){}

    public void openFileOutput(RandomAccessFile output_file){}

    public Integer readByte(){
        return null;
    }

    public String readLine(){
        return null;
    }

    public String toString(){
        return  null;
    }
}
