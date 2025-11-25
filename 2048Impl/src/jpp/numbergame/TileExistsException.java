package jpp.numbergame;

public class TileExistsException extends RuntimeException{

    public TileExistsException(){
    }


    public TileExistsException(String message){
        super(message);
    }
}
