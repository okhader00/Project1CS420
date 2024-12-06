package chatSystem;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {
	void sendMessage(String message, String sender) throws RemoteException;
	void registerClient(String clientName, ChatServerInterface clientStub) throws RemoteException;
	void receiveMessage(String message) throws RemoteException;
}
