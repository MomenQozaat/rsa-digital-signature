import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSASignatureDemo {

    public static void main(String[] args) throws Exception {

        // 1) توليد زوج مفاتيح RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        System.out.println("RSA key pair generated (2048-bit).");

        // 2) الرسالة الأصلية
        String message = "Transfer $1000 to Mark";
        System.out.println("Original message: " + message);

        // 3) توقيع الرسالة باستخدام المفتاح الخاص
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(message.getBytes("UTF-8"));
        byte[] signature = signer.sign();

        String sigBase64 = Base64.getEncoder().encodeToString(signature);
        System.out.println("Signature generated: " + sigBase64.substring(0, 40) + "...");

        // 4) التحقق من التوقيع بالمفتاح العام
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(message.getBytes("UTF-8"));
        boolean valid = verifier.verify(signature);
        System.out.println("Verification result: " + (valid ? "Signature is VALID" : "Signature is INVALID"));

        // 5) اختبار التلاعب: تغيير الرسالة
        String tampered = "Transfer $1000 to Oscar";
        Signature verifier2 = Signature.getInstance("SHA256withRSA");
        verifier2.initVerify(publicKey);
        verifier2.update(tampered.getBytes("UTF-8"));
        boolean valid2 = verifier2.verify(signature);
        System.out.println("Tampered message: " + (valid2 ? "VALID (unexpected!)" : "INVALID (as expected)"));
    }
}