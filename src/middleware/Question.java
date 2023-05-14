package middleware;

public class Question {

    private String question;    // question from user

    /*
     * Takes in a question formatted as a String
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