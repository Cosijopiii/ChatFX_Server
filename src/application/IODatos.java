package application;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.Observer;

public class IODatos implements Runnable,Observer{
	
	 private Mensaje msj;
	 private ObjectInputStream Idato;
	 private ObjectOutputStream Odato;
	 private Socket socket;
	 private Server s;
	public IODatos(Socket socket, Mensaje msj,Server s) {
		this.socket=socket;
		this.s=s;
		this.msj = msj;
		try {
			this.Idato = new ObjectInputStream(socket.getInputStream());
			this.Odato = new  ObjectOutputStream( socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
		boolean on=true;
		msj.addObserver(this);
		while(on){
			try {
				DataChat da=(DataChat)Idato.readObject();
				for (String key : s.users.keySet()) {
				   da.getObservableList().add(key);
				}
				msj.setMsj(da);
				
			} catch (IOException | ClassNotFoundException e) {
				on=false;
				try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				try {
					Idato.close();
					Odato.close();
				} catch (IOException e1) {
				
					System.err.println("error en IO");
				}
				e.printStackTrace();
				System.err.println("cliente desconectado");
				
				
			}	
		}
	}	
	@Override
	public void update(Observable arg0, Object m) {
		try {
			Odato.writeObject((DataChat)m);
			Odato.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
