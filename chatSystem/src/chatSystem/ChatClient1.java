package chatSystem;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

//Other client files are the same besides name and port number so refer to comments here for all
public class ChatClient1 {
    public static void main(String[] args) {
        try {
        	LocateRegistry.createRegistry(1099);	//Create RMI registry with different port number for each client
        	ChatServer server = new ChatServer("Client1");	//Each client has its own ChatServer object
        	Naming.rebind("//localhost:1099/Client1", server);	//Renaming client so other clients can find it by name and port number
        	System.out.println("Chat client started as: Client1");
        	
        	//Lookup clients
        	ChatServerInterface client2 = lookupClientWithRetry("Client2", 1100);	
        	ChatServerInterface client3 = lookupClientWithRetry("Client3", 1101);
        	
        	//Once clients are found, register them to servers client list
        	server.registerClient("Client2", client2);
        	server.registerClient("Client3", client3);
        	        	
            System.out.println("Type messages to send:");

            Scanner scanner = new Scanner(System.in);
            while (true) {
            	//Continuously accepts input and sends messages to all other connected clients
                String input = scanner.nextLine();
                server.sendToAll("Client1: " + input);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static ChatServerInterface lookupClientWithRetry(String clientName, int port) {
    	//Method to search for other clients by name and port number continuously until they are found
        ChatServerInterface clientStub = null;
        while (clientStub == null) {
            try {
                clientStub = (ChatServerInterface) Naming.lookup("//localhost:" + port + "/" + clientName);
                System.out.println("Connected to " + clientName);
            } catch (Exception e) {
                try {
                    Thread.sleep(3000); //Wait for 3 seconds before retrying
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return clientStub;
    }
}
