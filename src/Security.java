//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Vytvorit funkciu na bezpecne generovanie saltu.              //
// Uloha2: Vytvorit funkciu na hashovanie.                              //
// Je vhodne vytvorit aj dalsie pomocne funkcie napr. na porovnavanie   //
// hesla ulozeneho v databaze so zadanym heslom.                        //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Security {
    
    private static String hash(String password, byte[] salt) throws Exception{
        /*
         *   Pred samotnym hashovanim si najskor musite ulozit instanciu hashovacieho algoritmu.
         *   Hash sa uklada ako bitovy retazec, takze ho nasledne treba skonvertovat na String (napr. cez BigInteger);
         */

        // hesovaci algoritmus  PBKDF2WithHmacSHA1
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        // zaheshopvanie hesla so saltom
        byte[] secretKeys = factory.generateSecret(spec).getEncoded();

        return Base64.getUrlEncoder().encodeToString(secretKeys);
    }
    //https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    protected static String getSalt(/*long min, long max*/) {
        /*
         *   Salt treba generovat cez secure funkciu.
         */

        // bezpecne vygenerovanie nahodnych 16 bytov
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);

        // enkodovanie do String formatu UTF 8
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    protected static String getHash(String password, String salt) throws Exception {
        // dekodovanie zo Stringu na byty
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);

        String hashPasswd = "";

        // pokusit sa zahesovat heslo
        return hash(password, saltBytes);
    }
}

