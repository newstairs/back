package project.back.configuration;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    void jasypt(){
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "root";
        String password = "rlawjdrb123";
        //kakao api 개인 restkey
        String restKey = "b97cb92610acfa6862b281d12658b0fc";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        //kakao api 개인 restkey 부분
        String encryptRestKey = jasyptEncrypt(restKey);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword : " + encryptPassword);

        //kakao api 개인 restkey 부분
        System.out.println("encryptRestKey : " + encryptRestKey);

        Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
       }

    private String jasyptEncrypt(String input) {
        String key = "5678";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input){
        String key = "5678";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }

}