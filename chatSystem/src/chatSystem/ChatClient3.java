package chatSystem;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ChatClient3 {
    public static void main(String[] args) {
        try {
        	LocateRegistry.createRegistry(1101);
        	ChatServer server = new ChatServer("Client3");
        	Naming.rebind("//localhost:1101/Client3", server);
            System.out.println("Chat client started as: Client3");
        	
        	ChatServerInterface client1 = lookupClientWithRetry("Client1", 1099);
        	ChatServerInterface client2 = lookupClientWithRetry("Client2", 1100);
        	
        	server.registerClient("Client1", client1);
        	server.registerClient("Client2", client2);
        	
            System.out.println("Type messages to send:");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                server.sendToAll("Client3: " + input);
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
