public class LC {
    private Error error;
    private Compiler compiler;

    public static void main(String[] args) {

        if (new IOFile().checkOpenFile("../exemplo.l")){
            new Error (Error.ERROR_FILE_NOT_FOUND);
        } else {
            new Compiler("exemplo.l", "exemplo.asm");
            System.out.println("Fim normal da execução");
        }

    }
}
