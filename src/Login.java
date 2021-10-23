//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha2: Upravte funkciu na prihlasovanie tak, aby porovnavala        //
//         heslo ulozene v databaze s heslom od uzivatela po            //
//         potrebnych upravach.                                         //
// Uloha3: Vlozte do prihlasovania nejaku formu oneskorenia.            //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.io.IOException;
import java.util.StringTokenizer;
import passwordsecurity2.Database.MyResult;

public class Login {
    protected static MyResult prihlasovanie(String meno, String heslo) throws IOException, Exception{
        /*
        *   Delay je vhodne vytvorit este pred kontolou prihlasovacieho mena.
        */

        // pockaj 100 milisekund pred hladamin (ochrana voci DoS)
        Thread.sleep(100);

        Database db = new Database();

        MyResult account = db.find( meno);

        if (!account.getFirst()){
            db.closeBdd();
            return new MyResult(false, "Nespravne meno.");
        }
        else {
            StringTokenizer st = new StringTokenizer(account.getSecond(), ":");
            st.nextToken();      //prvy token je prihlasovacie meno

            /*
            *   Pred porovanim hesiel je nutne k heslu zadanemu od uzivatela pridat prislusny salt z databazy a nasledne tento retazec zahashovat.
            */
            // dalsi (druhy) token je heslo
            String databasePassword = st.nextToken();
            // dalsi (treti) token je heslo
            String salt = st.nextToken();

            try{
                // zahesuj heslo od pouzivatela so saltom z databazy
                heslo = Security.getHash(heslo, salt);

                // porovnaj so zahesovane heslo odpouzivatela s tym spravnym v databaze
                boolean rightPassword = databasePassword.equals(heslo);

                // ak sa nezhoduju zobraz chybovu hlasku
                if (!rightPassword) {
                    db.closeBdd();
                    return new MyResult(false, "Nespravne heslo.");
                }
            }catch (Exception e){

                // ak doslo k chybe pri hesovani zobraz chybovu hlasku
                db.closeBdd();
                return new MyResult(false, "Chyba pri zadavani hesla.");
            }
        }
        db.closeBdd();
        return new MyResult(true, "Uspesne prihlasenie.");
    }
}
