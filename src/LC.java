public class LC {
//    private Error error;
//    private Compiler compiler;

    public static void main(String[] args) {

        if (new IOFile().checkOpenFile("../tests/test-comment.l")){
            new Error (Error.ERROR_FILE_NOT_FOUND);
        } else {
            new Compiler("tests/test-comment.l", "exemplo.asm");
            System.out.println("Fim normal da execução");
        }
    }
}
