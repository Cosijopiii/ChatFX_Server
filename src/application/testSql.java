package application;


public class testSql {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConexionSqlite s=new ConexionSqlite();
			
		for (Usuario u : s.select()) {
			System.out.println(u);
		}
		
	}

}
