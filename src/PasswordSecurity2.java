package passwordsecurity2;

import javax.swing.JFrame;
import javax.xml.crypto.Data;


public class PasswordSecurity2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        GUI okno = new GUI();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setVisible(true);
        okno.setResizable(false);
        okno.setLocationRelativeTo(null);
    }
    
}
