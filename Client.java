import java.io.*;
import java.net.*;


public class Client {

    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localHost", 4999);
        
        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println("Hello World");
        pr.flush(); //forces any buffered output to be written to output stream

    }

    public void handshake(){

    }
    
    public void disconnect(){

    }

    public void request(){

    }

    public void getSocket(){

    }

}
