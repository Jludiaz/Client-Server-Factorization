import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

public class Server {

    private ServerSocket serverSocket = new ServerSocket();
    private int port;

    private ArrayList<LocalDateTime> timeList = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        

        System.out.println("Client Connected");
        
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        String string = reader.readLine();
        System.out.println("Client: " + string);


    }
    
    public Server(int port){
        this.port = port;
    }

    public serve(int serves){
        try {
            //CREATE A SERVERSOCKET WITH PORT
            serverSocket = new ServerSocket(port);
            System.out.println("Server is reaching connection with Port" + port);

            for (int i = 0; i < serves; i++){
                //ACCEPT CONNECTION
                Socket socket = serverSocket.accept();
                System.out.println("New connection established");

                //GET CURRENT TIMESTAMP AND SAVE TO ARRAYLIST
                getConnectedTimes();

            }

        } catch (IOException e) {
            System.out.println("Error Serving Client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getConnectedTimes(){
        /*
            The server should record the time each client 
            was connected so that it can properly return 
            these values
        */
        LocalDateTime now = LocalDateTime.now();
        timeList.add(now);
        System.out.println("New Connection Established at: " + now);

    }

    public void disconnect(){
        try {
            serverSocket.close();
            System.out.println("Server Successfully Closed");
        } catch (IOException e) {
            System.out.println("Error disconnecting from server: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
