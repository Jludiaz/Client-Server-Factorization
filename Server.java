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

        //CLIENT HANDLER
        HandshakeHandler client = new HandshakeHandler(this);
        new Thread(client).start();
        
    }

    public void serve(int serves){
        //ACCEPT SERVER
        for (int i = 0; i < serves; i++) {
            try {
                Socket serveSocket = new Socket();
                //ACCEPT CONNECTION
                //GET CURRENT TIME
                LocalDateTime now = LocalDateTime.now();
                timeList.add(now);

                //HANDLE CLIENT
                System.out.println("New connection established: " + port + " at time: " + now);

                ClientHandler client = new ClientHandler(serveSocket, this);
                new Thread(client).start();

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
        ArrayList<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            if (number % i == 0){
                factors.add(i);
            }
        }
        return factors.toString();
    }

    //Handshake ClientHandler 
    private class HandshakeHandler extends Thread{
        private Server server;

        public HandshakeHandler (Server server){
            this.server = server;
        }

        public void run(){

            //Socket Accept
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
                    out.println("Denied Access");
                    return;
                }

                out.println("Connection Established");
            }catch(IOException e){
               e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread{
        private Server server;
        private Socket socket;

        public ClientHandler(Socket socket, Server server){
            this.server = server;
            this.socket = socket;
        }

        public void run(){

            BufferedReader in = null;
            PrintWriter out = null;

            try{
                out = new PrintWriter(this.socket.getOutputStream(), true); 
                System.out.println("MARKER");
                in = new BufferedReader(new InputStreamReader( 
                this.socket.getInputStream()));

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
