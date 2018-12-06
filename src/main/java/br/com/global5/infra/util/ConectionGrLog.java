package br.com.global5.infra.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.google.zxing.Result;

public class ConectionGrLog {
	
	public Statement stm;
	public ResultSet rs;
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
	//"jdbc:sqlserver://"+serverip+"\\SQLEXPRESS:"+serverport+";databaseName="+dbName+"";
	private static String caminho = "jdbc:sqlserver://201.48.123.195:1433;databaseName=grlog";
	private static String usuario = "financeiroglobal5";
	private static String senha = "F!n@GlOb@l$";
	
	public static Connection conexao;
	
	
	public static void conectar(){
		try {
			//System.setProperty("jdbc.Drivers", driver);
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				conexao = DriverManager.getConnection(caminho,usuario,senha);
				JOptionPane.showMessageDialog(null, "conectado!!!","Banco de Dados",JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			// TODO: handle finally clause
		}
	}

	
//	public static void main (String[] args){
//		// Create a variable for the connection string.
//
//		
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		
//        	try {
//        		// Establish the connection.
//        		
//            		con = DriverManager.getConnection(connectionUrl);
//            
//            		// Create and execute an SQL statement that returns some data.
//        			String SQL = "select CD_REGISTRO_VIAGENS,CD_CLIENTE from dbo.CADASTRO_VIAGENS_NOVO where dbo.CADASTRO_VIAGENS_NOVO.CD_REGISTRO_VIAGENS = 294641";
//            		stmt = con.createStatement();
//            		rs = stmt.executeQuery(SQL);
//            
//            		// Iterate through the data in the result set and display it.
//            		while (rs.next()) {
//            			System.out.println(rs.getString(1) + " " + rs.getString(2));
//            		}
//        	}
//        
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		finally {
//			if (rs != null) try { rs.close(); } catch(Exception e) {}
//	    		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//	    		if (con != null) try { con.close(); } catch(Exception e) {}
//		}
//	}
	
	public void getConnectionGR() throws ClassNotFoundException{
//		"jdbc:sqlserver://{host}[:{port}][;databaseName={database}]"
		String urlSqlServer = "jdbc:sqlserver://201.48.123.195:1433;databaseName=grlog;user=financeiroglobal5;password=F!n@GlOb@l$";
		
		Connection conn = null;
		Statement  stmt = null;
		ResultSet	 rs = null;
		
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");			
			conn = DriverManager.getConnection(urlSqlServer);
			
			String SQL = "select CD_REGISTRO_VIAGENS,CD_CLIENTE from dbo.CADASTRO_VIAGENS_NOVO where dbo.CADASTRO_VIAGENS_NOVO.CD_REGISTRO_VIAGENS = 294641";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				System.out.println(rs.getString(1) + " " + rs.getString(2));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			   if(rs != null) try { rs.close(); } catch(Exception e) {}
			   if(stmt != null) try { stmt.close(); } catch(Exception e) {}
			   if(conn != null) try { conn.close(); } catch(Exception e) {}
		}	
		
	}

}
