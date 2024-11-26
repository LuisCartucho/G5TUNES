package easv.g5tunes.exceptions;


public class WorkoutExceptions extends Exception{
    public WorkoutExceptions (Exception e){
        super(e);
    }

    public WorkoutExceptions (String message){
        super(message);
    }
}