package middleware;


import frontend.DisplayPanel;

public class commandMediator {

    PromptFactory factory;
    SayItAssistant sayIt;
    HistoryManager historyManager; 
    DisplayPanel display; 
    String command; 

    commandMediator(SayItAssistant sayIt, HistoryManager historyManager, DisplayPanel display){
        this.sayIt = sayIt; 
        this.historyManager = historyManager;
        this.display = display; 
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
        String command = promptCommandPair[1].toString();
        String query = promptCommandPair[2].toString();
        
        switch(command){
            case "question": 
                sayIt.respond();

            case "delete prompt":
                //re-route delete button logic to here

            case "clear all":
                //re-route clear all button logic to here

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
