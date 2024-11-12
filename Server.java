import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

public class Server {


    private ServerSocket serverSocket;
    private int port;

    private ArrayList<LocalDateTime> timeList = new ArrayList<>();
    
    public Server(int port){
        this.port = port;
    }

    public void serve(int serves){

        //OPEN SERVER
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is reaching connection with Port" + port);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Cannot Open Server");
            e.printStackTrace();
        }


        //ACCEPT SERVER
        for (int i = 0; i < serves; i++) {
            try {
                //ACCEPT CONNECTION
                Socket socket = serverSocket.accept();
                //GET CURRENT TIME
                LocalDateTime now = LocalDateTime.now();
                timeList.add(now);

                //HANDLE CLIENT
                System.out.println("New connection established: " + port + " at time: " + now);

                new ClientHandler(socket, this).start();

            } catch (IOException e) {
                System.out.println("Error Serving Client: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public ArrayList getConnectedTimes(){
        
        /*
            The server should record the time each client 
            was connected so that it can properly return 
            these values
        */
        return this.timeList;
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

    public String factorize (int number){
        ArrayList<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            if (number % i == 0){
                factors.add(i);
            }
        }
        return factors.toString();
    }

    private class ClientHandler extends Thread{
        private Socket sock;
        private Server server;

        public ClientHandler(Socket sock, Server server){
            this.sock = sock;
            this.server = server;
        }

        public void run(){
            try(
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true); //autoflush
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))
            ){
                //HANDSHAKE
                String passcode = in.readLine();
                if(!passcode.equals("12345")){
                    out.println("Denied Access");
                    System.out.println("Incorrect Passcode Error");
                    sock.close();
                    return;
                }

                out.println("CONNECTION OK");

                //Read and process factorizarion
                String message = new String();
                while(((message = in.readLine()) != null)){
                    try {
                        int number = Integer.parseInt(message);
                        String factors = server.factorize(number);
                        out.println(factors);
                    } catch (Exception e) {
                        // TODO: handle exception
                        out.println("ERROR EXCEPTION TRYING TO PROCESS NUMBER");
                        e.printStackTrace();
                    }
                }
                
            } catch(Exception e){
                System.out.println("Connection Lost " + sock.getRemoteSocketAddress());
            }
        }
    }
}
