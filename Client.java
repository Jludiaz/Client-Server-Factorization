import java.io.*;
import java.lang.annotation.IncompleteAnnotationException;
import java.net.*;


public class Client{

    private String hostname = new String();
    private int port;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(String hostname, int port) throws IOException{
        this.hostname = hostname;
        this.port = port;

        try {
            this.socket = new Socket(hostname, port);
            System.out.println("Server is reaching connection with Port: " + port);

            this.output = new PrintWriter(socket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Client: Cannot Open Server");
            e.printStackTrace();
        }
    }

    //connects to the server by pin
    public void handshake() throws IOException{
        output.println("12345");
        String response = input.readLine();

        String key = "Connection Established";
        if (!key.equals(response)){
            throw new IOException("Sorry. Connection Password Denied\nServer Response: " + response);
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
            output.println(stringInteger);
            response = input.readLine();
            System.out.println("Factors: " + response);
            return response;

        } catch (Exception e) {
            System.out.println("Error with request: " + stringInteger);
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
