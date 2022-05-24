import javax.swing.*;

public class HangmanDriver {
    public static void main(String[] args) {
        Hangman hm = new Hangman();
        hm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hm.setVisible(true);
        hm.setSize(600,1000);
    }
}
