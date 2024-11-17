import java.io.*;
import java.lang.annotation.IncompleteAnnotationException;
import java.net.*;


public class Client{

    private String hostname;
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

            this.output = new PrintWriter(socket.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Client: Cannot Open Server");
            e.printStackTrace();
        }
    }

    //connects to the server by pin
    public void handshake() throws IOException{

        System.out.println("Starting Handshake...");

        output.println("12345");
        output.flush();
    }

    //disconnects from server
    public void disconnect(){
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Server Successfully Closed");
            }
        } catch (IOException e) {
            System.out.println("Error disconnecting from server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Requests the factoriarion of String integer
    public String request(String stringInteger){
        try {
            output.println(stringInteger);
            output.flush();

            String response = input.readLine();
            System.out.println("Response received: " + response);
            return response;

        } catch (Exception e) {
            System.out.println("Error with request: " + stringInteger);
            e.printStackTrace();
            return "ERROR EXCEPTION TRYING TO PROCESS NUMBER";
        }
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
