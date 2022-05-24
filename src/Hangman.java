import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Canvas;
import java.util.Locale;

/**
 * Hangman class sets up the hangman game to be ran on the driver
 * instance variables:
 * String word: holds the word input
 * String incorrectLetters: holds the incorrect letters guessed
 * String correctLettersString: holds the correct letters as a string
 * int guessesLeft: holds the number of guesses left
 * int guessesTotal: holds the total number of guesses user has entered
 * char[] lettersGuessed: holds all the letters guessed
 * char[] correctLetters: holds the correct letters as a char array
 * JFrame instance variables to setup the window
 */
public class Hangman extends JFrame {
    //initialize String instance variables
    private String word = new String();
    private String incorrectLetters = new String();
    private String correctLettersString = new String();

    //initialize integer instance variables
    private int guessesLeft = 6;
    private int guessesTotal = -1;

    //initialize char array instance variables
    private char[] lettersGuessed = new char[26];
    private char[] correctLetters = new char[26];

    //initialize JFrame instance variables
    private JButton newWordButton = new JButton("Press For New Word!");
    private JTextField inputLetterTextField = new JTextField("Enter new word to play!");
    private JLabel incorrectLettersLabel = new JLabel("Incorrect Letters Guessed:");
    private JLabel guessesLeftLabel = new JLabel("Guesses Left:");
    private JLabel correctLettersLabel = new JLabel();
    private JTextField newWordTextField = new JTextField("Enter New Word and press enter");

    //initialize canvas for drawing picture
    private Canvas picture = new Canvas();


    //no argument constructor

    /**
     * Hangman: No argument constructor
     * sets up the window using JFrame
     * sets up the layout of the window and fonts of the text
     * uses actionlisteners to see when user has interacted with program
     * calls appropriate methods based on which action was detected
     */
    public Hangman(){
        //title the window
        super("Hangman");

        //use boxlayout for my program
        JFrame box = new JFrame();
        box.getContentPane().setLayout(new BoxLayout(box.getContentPane(),BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();

        //call method to draw the hangman picture
        updatePicture();

        //setup jframe modules to prefferred settings
        newWordButton.setPreferredSize(new Dimension(600,50));
        correctLettersLabel.setPreferredSize(new Dimension(600,150));
        incorrectLettersLabel.setPreferredSize(new Dimension(600,150));
        guessesLeftLabel.setPreferredSize(new Dimension(600,50));
        inputLetterTextField.setPreferredSize(new Dimension(600,100));

        //change text size for better user interface
        correctLettersLabel.setFont(new Font("Courier", Font.PLAIN, 50));
        incorrectLettersLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        guessesLeftLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        inputLetterTextField.setFont(new Font("Courier", Font.PLAIN, 40));

        //add all elements to panel
        panel.add(newWordButton);
        panel.add(correctLettersLabel);
        panel.add(incorrectLettersLabel);
        panel.add(picture);
        panel.add(guessesLeftLabel);
        panel.add(inputLetterTextField);

        //left align all elements in box layout
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //add panel to box layout
        box.getContentPane().add(panel);
        box.setVisible(true);

        //add panel to window
        add(panel);

        //create a new frame for the new window that pops up after clicking new word button
        JFrame newWord = new JFrame();

        //stop users from entering input until new word is entered
        inputLetterTextField.setEditable(false);

        //action listener if someone presses new word button
        newWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //reset values for new word
                word = null;
                correctLetters = null;
                correctLettersString = new String();
                incorrectLetters = new String();
                inputLetterTextField.setEditable(true);
                inputLetterTextField.setText("Enter Letter Guess Here.");


                //open new window to enter new word
                newWord.setVisible(true);
                newWordTextField.setEditable(true);
                newWord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newWord.setSize(new Dimension(400,100));
                newWord.setLocationRelativeTo(null);

                //add text field to newWord frame
                newWord.add(newWordTextField);

            }
        });

        //action listener if user presses enter in new word box
        newWordTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //set word to the user given word
                word = newWordTextField.getText();
                word = word.toUpperCase(Locale.ROOT);

                //check if word is valid by calling checkWord method
                if (checkWord(word) != "true") {
                    newWordTextField.setText(checkWord(word));
                }

                //if word is valid continue
                else{
                    //change font size to fit new word
                    //depending on size of word will decide the font
                    correctLettersLabel.setFont(new Font("Courier", Font.PLAIN, 500 / word.length()));

                    //close window after word is entered
                    newWord.setVisible(false);
                    newWordTextField.setEditable(false);

                    //reset initialize game variables
                    correctLettersString = new String();
                    correctLetters = new char[word.length()];
                    lettersGuessed = new char[26];
                    incorrectLetters = new String();
                    guessesLeft = 6;
                    guessesTotal = -1;

                    //reset the jframe components
                    incorrectLettersLabel.setText("Incorrect Letters Guessed: ");
                    guessesLeftLabel.setText("Guesses Left " + guessesLeft);
                    inputLetterTextField.setFont(new Font("Courier", Font.PLAIN, 25));
                    inputLetterTextField.setText("Enter Letter Guess Here.");
                    updatePicture();

                    //set correct letters to none
                    //this way previous games wont effect new games
                    for (int i = 0; i < word.length(); i++) {
                        correctLetters[i] = '_';
                        correctLettersString += correctLetters[i] + " ";
                    }
                    //set the label to the new games word
                    correctLettersLabel.setText(correctLettersString);
                }
            }
        });

        //action listener if user presses enter in the input letter box
        inputLetterTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //run inputLetter method
                inputLetter();
            }
        });

    }


    //method makes sure letter entered is valid and checks if it is correct or incorrect guess

    /**
     * inputLetter method
     * no inputs
     * makes sure letter entered is valid and checks if it is a correct or incorrect guess.
     */
    public void inputLetter(){

        //initialize some variables to help the method run
        boolean correct = false;
        String letter = inputLetterTextField.getText();
        letter = letter.toUpperCase(Locale.ROOT);
        correctLettersString = new String();

        //error if user entered more than one letter
        if(letter.length() > 1){
            inputLetterTextField.setText("Error: Only one letter at a time.");
            return;
        }

        //error if user enters something other than letters
        if(!Character.isLetter(inputLetterTextField.getText().charAt(0))){
            inputLetterTextField.setText("Error: Only guess letters!");
            return;
        }

        //check if letter has already been guessed
        for(int i = 0; i < lettersGuessed.length; i ++){
            if(letter.charAt(0) == lettersGuessed[i]){
                inputLetterTextField.setText("Error: Letter has already been guessed.");
                return;
            }
        }

        //add 1 to the total guesses
        guessesTotal ++;
        //adds the letter to letters guessed array
        lettersGuessed[guessesTotal] = letter.charAt(0);

        //for loop to see if the letter is in the given word
        for(int i = 0; i < word.length(); i++){
            if(lettersGuessed[guessesTotal] == word.charAt(i)){
                correctLetters[i] = letter.charAt(0);
                correct = true;
            }
        }

        //if letter was not correct, add to the incorrect letter string and remove one from guesses left
        if(!correct){
            incorrectLetters += letter + ", ";
            guessesLeft --;
        }

        //convert the correct letters array to a string for easier printing
        for(int i = 0; i < word.length(); i++){
            correctLettersString += correctLetters[i] + " ";
        }

        //set correct letters string to be viewed on screen
        correctLettersLabel.setText(correctLettersString);
        //update incorrect letters to be viewed on screen
        incorrectLettersLabel.setText("Incorrect Letters Guessed: " + incorrectLetters);
        //update guesses left to be viewed on screen
        guessesLeftLabel.setText("Guesses Left: " + guessesLeft);

        //call update picture method to redraw hangman
        updatePicture();
        //check for wins/loss
        checkWin();

    }

    //method to see if player has lost or won yet

    /**
     * checkWin method:
     * no inputs
     * checks if the player has lost or won yet
     */
    public void checkWin(){
        //if user is out of guesses: player lost and game ends
        if(guessesLeft < 1){
            inputLetterTextField.setFont(new Font("Courier", Font.PLAIN, 16));
            inputLetterTextField.setText("Out of guesses! Please enter a new word to play a new game.");
            inputLetterTextField.setEditable(false);
            correctLettersLabel.setFont(new Font("Courier", Font.PLAIN, 40));
            correctLettersLabel.setText("<html> The Correct Word was: "+ "<br>" + word + "<html>");


            return;
        }

        //check string for underscores, if there is then game is not complete
        for(int i = 0; i < correctLetters.length; i++){
            if(correctLetters[i] == '_'){
                inputLetterTextField.setText("");
                return;
            }
        }

        //if method hasnt returned yet then user has won and game ends
        inputLetterTextField.setText("You Won! " +
                    "Enter a new word.");
        inputLetterTextField.setEditable(false);

    }

    //update picture method checks how many guesses are left and draws accordingly

    /**
     * updatePicture method:
     * no inputs
     * checks how many guesses are left and draws the hangman accordingly
     */
    private void updatePicture(){
        picture = new Canvas(){
            public void paint(Graphics g){
                //draw the hangman holder
                g.drawLine(375,350,525,350);
                g.drawLine(450,50,450,350);
                g.drawLine(450,50,250,50);
                g.drawLine(250,50,250,100);

                //draw head if 5 guesses left
                if(guessesLeft < 6) {
                    //head
                    g.drawOval(215, 100, 70, 70);
                }

                //draw torso if 4 guesses left
                if(guessesLeft < 5) {
                    //torso
                    g.drawLine(250, 170, 250, 240);
                }

                //draw left arm if 3 guesses left
                if(guessesLeft < 4) {
                    //left arm
                    g.drawLine(215, 190, 250, 205);
                }

                //draw right arm if two guesses left
                if(guessesLeft < 3) {
                    //right arm
                    g.drawLine(250, 205, 285, 190);
                }

                //draw left leg if 1 guess left
                if(guessesLeft < 2) {
                    //left leg
                    g.drawLine(215, 300, 250, 240);
                }

                //draw right leg if no more guesses left
                if(guessesLeft < 1) {
                    //right leg
                    g.drawLine(285, 300, 250, 240);
                }
            }
        };

        //set size of canvas
        picture.setSize(600, 400);
        picture.setVisible(true);

        //add canvas to frame
        add(picture);
    }

    //check word method checks if the input word is valid or not
    //returns a string containing the error if not valid
    //returns string "true" if valid

    /**
     * checkWord method:
     * checks if the word input is valid.
     * @param w : input is a string that holds the word that the user is trying to play with.
     * @return : returns a string containing "true" if words is valid and an appropriate error message if word is not valid.
     */
    private String checkWord(String w){

        //length must be larger than three
        if(w.length() < 3){
            return "Word Must be atleast 3 letters long!";
        }
        //length must be less than 25
        if(w.length() > 25){
            return "Word is too long!";
        }
        //input must only contain letters
        for(int i = 0; i < w.length(); i++){
            if(!Character.isLetter(w.charAt(i))){
                return "Word must only be made up of Letters!";
            }
        }
        //if all these pass, return true
        return "true";
    }

}
