//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Do suboru s heslami ulozit aj sal.                           //
// Uloha2: Pouzit vytvorenu funkciu na hashovanie a ulozit heslo        //
//         v zahashovanom tvare.                                        //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.security.NoSuchAlgorithmException;
import passwordsecurity2.Database.MyResult;


public class Registration {
    protected static MyResult registracia(String meno, String heslo) throws  Exception{
        Database db = new Database();
        if (db.exist(meno)){
            System.out.println("Meno je uz zabrate.");
            db.closeBdd();
            return new MyResult(false, "Meno je uz zabrate.");
        }
        else {
            /*
            *   Salt sa obvykle uklada ako tretia polozka v tvare [meno]:[heslo]:[salt].
            */
            try{
                String salt = Security.getSalt();
                heslo = Security.getHash(heslo, salt);

                return db.addUser(meno, heslo, salt);
            }catch (Exception e){
                System.out.println("Chyba pri zadavani hesla.");
                return new MyResult(false, "Chyba pri zadavani hesla.");
            }finally {
                db.closeBdd();
            }
        }
    }
    
}
