package br.com.global5.infra.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;

import com.google.zxing.Result;

public class ConectionGrLog {
	
	public Statement stm;
	public ResultSet rs; 
	private static String caminho = "jdbc:sqlserver://201.48.123.195:1433;databaseName=grlog";
	private static String usuario = "financeiroglobal5";
	private static String senha = "abCD12#$";
	
	public static Connection conexao;
	
	
	public static void conectar(){
		try {

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

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"SUCESSO","Conectado no GR-LOG!!!"));

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			// TODO: handle finally clause
		}
	}	

}
