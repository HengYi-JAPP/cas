import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * 描述：
 *
 * @author jzb 2018-02-03
 */
public class JasyptTest {
    public static void main(String[] args) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword("123456");
        System.out.println(encryptedPassword);
        passwordEncryptor = new StrongPasswordEncryptor();
        System.out.println(passwordEncryptor.checkPassword("123456", encryptedPassword));

        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        //UUZFWS/DISHEkHjfL3mdpFSadQxXY5NYBrHvQ3/Ql2cW2EyWOJ8nPCYgrYd06eiP
        textEncryptor.setPassword("123456");
        String myEncryptedText = textEncryptor.encrypt(encryptedPassword);
        System.out.println(myEncryptedText);
        System.out.println(textEncryptor.decrypt(myEncryptedText));
    }
}
