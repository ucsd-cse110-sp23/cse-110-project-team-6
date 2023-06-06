package SayItAssistant.middleware;

/*
 * This object holds a user's question.
 */
public class Question implements IPrompt{

    private String question;    // question from user
    private int questionNumber; //number of the question in the database
    private boolean STORABLE = true;
    private boolean UPDATES_DISPLAY = true;
    private String MESSAGE = "Question";

    public IPrompt createPrompt(String prompt){
        return new Question(prompt);
    }
    
    /*
     * Stores the question if it exists.
     * 
     * @param question: the question to be stored
     */
    public Question(String question) {

        // makes sure that the question is not null
        if (question == null) {
            throw new IllegalArgumentException("Entered question was null");
        }
        else {
            this.question = question;
        }
    }

    /*
     * Returns the question that was asked.
     * 
     * @return String: user question
     */
    public String toString() {
        return this.question;
    }

    /*
     * associate the question to its respective number in the history
     */
    public void setPromptNumber(int promptNumber){
        questionNumber = promptNumber; 
    }

    /*
     * get the current number of the question in the database. 
     */
    public int getPromptNumber(){
        return this.questionNumber; 
    }

    public String getMessage() {
        return MESSAGE;
    }

    public boolean isStorable() {
        return STORABLE;
    }
    
    public boolean updatesDisplay() {
        return UPDATES_DISPLAY;
    }
}