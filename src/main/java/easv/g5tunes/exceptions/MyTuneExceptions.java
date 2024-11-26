package easv.g5tunes.exceptions;


public class MyTuneExceptions extends Exception{
    public MyTuneExceptions(Exception e){
        super(e);
    }

    public MyTuneExceptions(String message){
        super(message);
    }
}