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

    //connects to the server by pin
    public void handshake(){
        try{
            socket = new Socket(hostname, port);
            System.out.println("Connection Established");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println("12345");
            String message = in.readLine();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //disconnects from server
    public void disconnect(){
        try {
            socket.close();
            System.out.println("Server Successfully Closed");
        } catch (IOException e) {
            System.out.println("Error disconnecting from server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Requests the factoriarion of String integer
    public String request(String stringInteger){
        String response = null;
        // int intInteger = Integer.parseInt(stringInteger);
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(stringInteger);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = br.readLine();
            System.out.println("Factors: " + response);
            return response;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return response;
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
