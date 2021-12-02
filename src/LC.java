/*
    * Link: https://github.com/headrockz/job-compilers
    * Grupo:
    * Asafe Felipe - E00731
    * Yasmim Lopes - E00790
    * Leonardo Akio - E00905
 */
public class LC {
//    private Error error;
//    private Compiler compiler;

    public static void main(String[] args) {

        if (!new IOFile().checkOpenFile("exemplo.l")){
            new Error (Error.ERROR_FILE_NOT_FOUND);
        } else {
            new Compiler("exemplo.l", "exemplo.asm");
            System.out.println("Fim normal da execução");
        }
    }
}