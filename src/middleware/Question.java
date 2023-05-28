package middleware;

/*
 * This object holds a user's question.
 */
public class Question implements Prompt{

    private String question;    // question from user
    private int questionNumber; //number of the question in the database

    public Prompt createPrompt(String prompt){
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
    public void setQestionNumber(int n){
        questionNumber = n; 
    }

    /*
     * get the current number of the question in the database. 
     */
    public int getQuestionNumber(){
        return this.questionNumber; 
    }

}