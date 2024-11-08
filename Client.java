import java.io.*;
import java.lang.annotation.IncompleteAnnotationException;
import java.net.*;


public class Client{

    private String hostname = new String();
    private int port;
    private Socket socket;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void handshake(){
        try{
            socket = new Socket(hostname, port);
            System.out.println("Connection Established");

            PrintWriter pr = new PrintWriter(socket.getOutputStream());
            pr.println("Hello World");
            pr.flush();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void disconnect(){

    }

    public void request(){
        
    }

    public Socket getSocket(){
        return this.socket;
    }
    
    public int getPort(){
        return this.port;
    }

    public String getLocalAddress(){
        return this.hostname;
    }

}
