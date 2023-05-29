package middleware;

import frontend.HistoryPanel;
import frontend.QnAPanel;

public class commandMediator {

    PromptFactory factory;
    SayItAssistant sayIt;
    HistoryManager historyManager; 
    HistoryPanel history; 
    QnAPanel qna; 
    String command; 

    commandMediator(SayItAssistant sayIt, HistoryManager historyManager, QnAPanel qna, HistoryPanel history){
        this.sayIt = sayIt; 
        this.historyManager = historyManager;
        this.qna = qna; 
        this.history = history;
        factory = new PromptFactory(); 

    }

    /*
     * newCommand is our "update" method that takes in a prompt and utilizes the factory
     * to generate the command and the prompt. there is a switch case on the command
     * that allows the mediator to do its required job
     * 
     * @input String prompt
     */
    void newCommand(String prompt){
        
        Object[] promptCommandPair = factory.createPrompt(prompt);
        String command = promptCommandPair[0].toString();
        String query = promptCommandPair[1].toString();
        
        switch(command){
            case "question": 
                /*
                 * sayIt respond method calls the whisper api to get the prompt
                 */
                sayIt.respond();

            case "delete prompt":
                if (qna.getStatus() == true){
                    historyManager.delete(qna.getQuestionPanel().getCurrentQuestionNumber());
                    history.revalidateHistory(qna);
                    qna.setQuestion(new Question("Welcome to SayIt Assistant"));
                    qna.setAnswer(new Answer(""));
                    //revalidating the question and answer display
                    qna.revalidate();

                    //setting the display to be false
                    qna.setDisplayFalse();
                }

            case "clear all":
                historyManager.clearAll();
                history.revalidateHistory(qna);
                qna.setQuestion(new Question("Welcome to SayIt Assistant"));
                qna.setAnswer(new Answer(""));
                qna.revalidate();
                qna.setDisplayFalse(); 

            case "setup email":
                //insert method here
            
            case "create email":
                //insert method here

            case "send email":
                //insert method here
            
            default:
                return; 
    
        }
    }
    
}
