import java.io.*;
import java.net.*;
import java.security.spec.ECGenParameterSpec;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;

    private BufferedReader input;
    private PrintWriter output;

    private ArrayList<LocalDateTime> timeList = new ArrayList<>();
    
    public Server(int port) throws IOException{
        this.port = port;
        //initialize serverSocket
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("serverSocket Initialized");
        }catch(IOException e) {
            throw new IOException("Error with ServerSocket Constructor");
        }
   
    }

    public void serve(int serves){
        //ACCEPT SERVER
        for (int i = 0; i < serves; i++) {
            try {
                ClientHandler client = new ClientHandler(this.socket, this);
                new Thread(client).start();

                LocalDateTime now = LocalDateTime.now();
                timeList.add(now);
                System.out.println("New connection established: " + port + " at time: " + now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes(){
        
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
        try {
            ArrayList<Integer> factors = new ArrayList<>();
            for (int i = 1; i <= number; i++) {
                if (number % i == 0) {
                    factors.add(i);
                }
            }
            return "The number " + number + " has " + factors.size() + " factors";
        } catch (Exception e) {
            return "There was an exception on the server";
        }
    }

    //Handshake ClientHandler 
    /*
    private class HandshakeHandler extends Thread{
        private Server server;

        public HandshakeHandler (Server server){
            this.server = server;
        }

        public void run(){

            Socket socket = new Socket();
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Starting run()");

            try{
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //autoflush
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //HANDSHAKE
                String passcode = in.readLine();
                System.out.println(passcode);
                if(!passcode.equals("12345")){
                    //out.println("Denied Access");
                    socket.close();
                    return;
                }

                //out.println("Connection Established");
                ClientHandler clientHandler = new ClientHandler(socket, server);
                new Thread(clientHandler).start();
                

            }catch(IOException e){
               e.printStackTrace();
            }
        }
    }
    */

    private class ClientHandler extends Thread{
        private final Server server;
        private final Socket socket;

        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket, Server server){
            this.server = server;
            this.socket = socket;
        }

        public void run(){

            try{
                out = new PrintWriter(socket.getOutputStream(), true); 
                in = new BufferedReader(new InputStreamReader( 
                socket.getInputStream()));

                //PASSCODE
                String passcode = in.readLine();
                System.out.println(passcode);
                if(!passcode.equals("12345")){
                    //out.println("Denied Access");
                    socket.close();
                    return;
                }

                //Read and process factorizarion
                String message = new String();
    
                while(((message = in.readLine()) != null)){

                    try {
                        int number = Integer.parseInt(message);
                        String factors = server.factorize(number);
                        out.println(factors);
                    } catch (Exception e) {
                        out.println("ERROR EXCEPTION TRYING TO PROCESS NUMBER");
                        e.printStackTrace();
                    }

                }
                
            } catch(Exception e){
                System.out.println("Connection Lost " + socket.getRemoteSocketAddress());
            }
        }
    }
}
