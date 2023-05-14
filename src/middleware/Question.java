package middleware;

/*
 * This object holds a user's question.
 */
public class Question {

    private String question;    // question from user

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

}