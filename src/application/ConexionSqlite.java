package application;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class ConexionSqlite {

    private Connection connection = null;
    private ResultSet r = null;
    private Statement statement = null;
    private PreparedStatement ps=null;
    private String db= ConexionSqlite.class.getResource("resources/database.db").getPath();    

    public ConexionSqlite()
    {
      try{
    	 System.out.println(db);
         Class.forName("org.sqlite.JDBC");
         connection = DriverManager.getConnection("jdbc:sqlite:" + this.db );
         System.out.println("Conectado a la base de datos SQLite [ " + this.db + "]");
      }catch(Exception e){
         System.out.println(e);
      }

    }
    public void deleteall(){
    	 try {
			statement = connection.createStatement();
			 statement.execute("Delete from usuarios; ");
			 statement.execute("VACUUM");
    	    statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
    }  
    public ArrayList<Usuario> select()
    {
       ArrayList<Usuario> l=new ArrayList<Usuario>();
       try {
         statement = connection.createStatement();
         r = statement.executeQuery("SELECT * FROM usuarios ; ");
         while (r.next())
         {
        	l.add(new Usuario(r.getString(1),r.getString(2)));
         }
        }
        catch (SQLException ex) {
           System.out.println(ex);
        }
     
       return l;
    }
    public boolean Insert(Usuario u){
    	try {
    	
			ps=connection.prepareStatement("insert into usuarios(user,password) values(?,?)");
			ps.setString(1,u.getUser());
			ps.setString(2,u.getPassword());
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			
			return false;
			
		}
    	return true;
    }
     public void desconectar()
       {
           try {
                r.close();
               statement.close();
               connection.close();
               System.out.println("Desconectado de la base de datos [ " + this.db + "]");
           }
           catch (SQLException ex) {
               System.out.println(ex);
           }
       }
}
   
