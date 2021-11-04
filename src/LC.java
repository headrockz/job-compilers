public class LC {
    private Error error;
    private Compiler compiler;

    public static void main(String[] args) {

        if (new EsFile().checkOpenFile("exemplo.lc")){
            new Error (Error.ERROR_FILE_NOT_FOUND);
        } else {
            new Compiler("exemplo.lc", "exemplo.asm");
            System.out.println("Fim normal da execução");
        }

    }
}
