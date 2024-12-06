package chatSystem;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ChatClient2 {
    public static void main(String[] args) {
        try {
        	LocateRegistry.createRegistry(1100);
        	ChatServer server = new ChatServer("Client2");
        	Naming.rebind("//localhost:1100/Client2", server);
        	System.out.println("Chat client started as: Client2");
        	
        	ChatServerInterface client1 = lookupClientWithRetry("Client1", 1099);
        	ChatServerInterface client3 = lookupClientWithRetry("Client3", 1101);
            
        	server.registerClient("Client1", client1);
        	server.registerClient("Client3", client3);
        	
            System.out.println("Type messages to send:");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                server.sendToAll("Client2: " + input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static ChatServerInterface lookupClientWithRetry(String clientName, int port) {
        ChatServerInterface clientStub = null;
        while (clientStub == null) {
            try {
                clientStub = (ChatServerInterface) Naming.lookup("//localhost:" + port + "/" + clientName);
                System.out.println("Connected to " + clientName);
            } catch (Exception e) {
            	try {
                    Thread.sleep(3000); 
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return clientStub;
    }
}
