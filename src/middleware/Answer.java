package middleware;

public class Answer {
    private String answer; // answer to a user's question

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