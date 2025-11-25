package de.uniwue.jpp.ook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Ook {

    //------------------------ entry point of application -------------------------------------//
    static void main(String[] args) throws IOException {
        if(args.length == 3){
            create().createExecutor().execute(args);
        }else{
            create().createShell(System.in, System.out, System.in).run();
        }
    }

    //------------------------ creates handle for accessing all submodules ---------------------//
    static Ook create(){
        return new Ook() {};
    }

    //------------------------ creation of submodules ------------------------------------------//
    default Util createUtil() {
        return Util.create();
    }

    default Shell createShell(InputStream input, OutputStream output, InputStream readInput) {
        return Shell.create(this, input, output, readInput);
    }

    default Parser createParser() {
        return Parser.create(this);
    }

    default Interpreter createInterpreter(Supplier<Integer> onRead, Consumer<Integer> onWrite){
        return Interpreter.create(onRead, onWrite);
    }

    default Executor createExecutor(){
        return Executor.create(this);
    }
}
