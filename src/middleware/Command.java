package middleware;

public class Command implements Observer {
    //Private strings that recognize the desired prompt. 
    private final String QUESTION = "question";
    private final String DELETE_PROMPT = "delete prompt";
    private final String CLEAR_ALL = "clear all";
    private final String SETUP_EMAIL = "setup email";
    private final String CREATE_EMAIL = "create email";

    public Command(){

    }
    
    @Override
    public void update(Question question, Answer answer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}
