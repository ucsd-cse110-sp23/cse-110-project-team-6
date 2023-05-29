package middleware;

/*
 * This object holds the answer to a user's question.
 */
public class Answer implements IResponse {

    private String answer; // answer to a user's question

    public Answer createResponse(String answer) {
        return new Answer(answer);
    }
    
    /*
     * Stores the user's answer if it exists.
     * 
     * @param answer: the answer to be stored
     */
    public Answer(String answer) {

        // makes sure that the answer is not null
        if (answer == null) {
            throw new IllegalArgumentException("Entered answer was null");
        }
        else {
            this.answer = answer;
        }
    }

    /*
     * Returns the answer that was given.
     * 
     * @return String: answer
     */
    public String toString() {
        return this.answer;
    }
}