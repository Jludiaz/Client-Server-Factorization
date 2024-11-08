import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(4999);
        Socket socket = server.accept();

        System.out.println("Client Connected");
        
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        String string = reader.readLine();
        System.out.println("Client: " + string);


    }
    
    public Server(){
        
    }

    public void getConnectedTimes(){
        /*
            The server should record the time each client 
            was connected so that it can properly return 
            these values
        */
    }

    public void disconnect(){
        
    }

}
