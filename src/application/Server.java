package application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public static final int PORT=6013;
	public static final int MAX_CONEX=10;
	Socket cliente=null;
	ServerSocket server=null;
	ConexionSqlite s;
	DatosUsuario data;
	ConcurrentHashMap<String,Socket> users=new  ConcurrentHashMap<String,Socket>();
	/**
	 * @param argv
	 */
	
	public Server (){
		
	}
	public void init(){
	
		try {
			
			 
			server =new ServerSocket(PORT,MAX_CONEX);
			cliente = new Socket();
			Mensaje  m=new Mensaje();
			ConexionSqlite s=new ConexionSqlite();
			while(true){
				cliente=server.accept();
				ObjectInputStream d=new ObjectInputStream(cliente.getInputStream()) ;
				data=(DatosUsuario) d.readObject();
				System.out.println("conectado "+data.getUsuario());
				if(data.getAsunto().equals(DatosUsuario.type.chatPrivate)){
					Thread io=new Thread(new IODatos(cliente,m,this));
					io.start();		
				}
				if(data.getAsunto().equals(DatosUsuario.type.register)){
					boolean ok= s.Insert(new Usuario(data.getUsuario(),data.getPassword()));
						DataOutputStream ob=new DataOutputStream(cliente.getOutputStream());
						System.out.println(ok);
						ob.writeBoolean(ok);
						if(ok){
							users.put(data.getUsuario(),cliente);
							Thread io=new Thread(new IODatos(cliente,m,this));
							io.start();
							
						}else{
							cliente.close();
						}
					 
				}
				if(data.getAsunto().equals(DatosUsuario.type.sessionstart)){
					
					boolean x=false;
				
					for (Usuario us : s.select()) {
						if(us.getUser().equals(data.getUsuario().trim())&&us.getPassword().equals(data.getPassword().trim())){
							x=true;
							break;
						}
					}
					DataOutputStream ob=new DataOutputStream(cliente.getOutputStream());
					ob.writeBoolean(x);
					if(x){
						users.put(data.getUsuario(),cliente);
						Thread io=new Thread(new IODatos(cliente,m,this));
						io.start();
					}else{
						cliente.close();
					}
				}
				
			
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				
				cliente.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
}
