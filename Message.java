class Message{

	private int id;
	private int round;
	private int ttl;
	private int topic;

	Message(int id,int round,int ttl){
		this.round=round;	
		this.ttl=ttl;
		this.id=id;
	}

	public int getId(){
		return this.id;
	}

	public int getTopic(){
		return this.topic;
	}

	public int getRound(){
		return this.round; 
	}	

	public int getTTL(){
		return this.ttl;
	}

	public void decTTL(){
		this.ttl--;
	}
}
