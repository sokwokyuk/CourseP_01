package AppKickstarter.Server;
/**
 * This class implements a client
 * @author 
 * @version 1.0
 */
public class Client {

	private String ClientID;
	private int nPerson;
	
	/**
	 * This constructs a client with ClientID and nPerson
	 * @param ClientID : The unique id of the client 
	 * @param nPerson : Number of person included in this ClientID
	 */
	public Client(String ClientID, int nPerson) {
		this.ClientID = ClientID;
		this.nPerson = nPerson;
	}
	
	/**
	 * This returns the current id of the client
	 * @return This client's id
	 */
	public String getClientID() {
		return this.ClientID;
	}
	
	/**
	 * This returns the number of person in the current ClientID
	 * @return Number of person 
	 */
	public int getnPerson() {
		return this.nPerson;
	}

}