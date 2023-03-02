package br.com.global5.infra.fourinf;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.jr.ob.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MotoristaInfFinanceiraFourInf {
	
	protected boolean usingHTTPS = false;
	protected JSON service;
	//homologacao.fourinf.com.br 137.184.197.233
    public static final String SERVICE_HOST = "www.fourinf.com.br/rest/consulta/requisicao";
    public static final String SERVICE_POST = "www.fourinf.com.br/rest/consulta";
//    public static final String SERVICE_HOST = "homologacao.fourinf.com.br/rest/consulta/requisicao";
//    public static final String SERVICE_POST = "homologacao.fourinf.com.br/rest/consulta";
	//public static final String SERVICE_HOST = "www.fourinf.com.br/rest/consulta/requisicao";
	public static final String SERVICE_GET  = "";
	//public static final String SERVICE_POST = "www.fourinf.com.br/rest/consulta";
	
	public MotoristaInfFinanceiraFourInf() {
		service = JSON.std;
	}
	
	public MotoristaInfFinanceiraFourInf(JSON service){
		this.service = service;
	}
	
	private String postHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://" ) + SERVICE_POST;
		return host;
	}
	
	private String putHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://" ) + SERVICE_HOST;
		return host;
	}
	
	private String getHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST;
		return host;
	}
	
	public boolean isUsingHTTPS(){
		return usingHTTPS;
	}
	
	public void setUsingHTTPS(boolean usingHTTPS){
		this.usingHTTPS = usingHTTPS;
	}
	
	public JSON getService(){
		return service;
	}
	
	public void setService(JSON service){
		this.service = service;		
	}
	
	public JSONObject getRequisicao(String numRequisicao, String chaveAcesso){
		
		String req = numRequisicao;
		String chv = chaveAcesso;
		
		String urlString = getHost() + "/" + numRequisicao + "/" + chaveAcesso ;
		
		Client restClient = Client.create();
		
		WebResource webResource = restClient.resource(urlString);
		ClientResponse resp = webResource.accept("application/json")
											.header("Authorization", "Basic Y2FkYXN0cm9AZ2xvYmFsNS5jb20uYnI6SW50Rm91ckdsb2I3NTM2OTg3")
											.get(ClientResponse.class);	
		
		
		if( resp.getStatus() != 200 ){
			System.out.println("Não é possível conectar com o servidor Four Info.");
		}
		
		String output = resp.getEntity(String.class);
		
		if ( !output.equals("")){
			JSONObject obj = new JSONObject(output);
			
			int returnObj = obj.toString().length();
			JSONObject obj1 = (JSONObject) obj.get("dadosResposta");
			JSONArray arrayJson = obj1.getJSONArray("requisicao");
			
				int r = arrayJson.length();
					
				for ( int i=0; i < r ; i++){
					String t = arrayJson.getJSONObject(i).opt("statusRequisicao").toString().trim(); 
					if( !t.toString().equals("SOLICITADA")){
						// Retorna o obj caso encontre os dados requiridos diferente de solicitada
						return obj;
					}
				}
			// Retorna null caso a pesquisa não esteja concluída.
			return null;
		}
		
		return null;
		
	}
	
	
	
	public JSONObject getMotoristaFichaFinanceira(String cpf , String ufProtesto, String ddd, String telefone){
		
		
		String m = "{\"autenticacao\":{"
						+ "\"usuario\":\"cadastro@global5.com.br\","
					 	+ "\"senha\":\"IntFourGlob7536987\""
					+ "},"
						+ "\"dadosConsulta\":{"
							+ "\"consultaCadastral\":\"true\","
							+ "\"tipoDocumento\":\"CPF\","
							+ "\"documento\":\"" + cpf + "\","
							+ "\"ufProtesto\":\"" + ufProtesto + "\","
							+ "\"ddd\":\"" + ddd + "\","
							+ "\"telefone\":\"" + telefone + "\""
							+ "}"
						+ "}"
					;

		Client restClient = Client.create();
		
		String urlString = postHost();
		
		WebResource webResource = restClient.resource(urlString);
				
		ClientResponse resp = webResource.type("application/json")
										 .post(ClientResponse.class, m);
		
		String output = resp.getEntity(String.class);
		
		resp.getClient();
				
		if (!output.equals("") && resp.getStatus() == 200){
			
			JSONObject obj = new JSONObject(output);
			
			int returnObj = obj.toString().length();				
				if( returnObj > 20){								   
					JSONObject jsonObjectMotrista = obj.getJSONObject("dadosResposta");
					if(jsonObjectMotrista.length() >= 0){
						restClient.destroy();
						resp.close();
						return jsonObjectMotrista;
					} else {
						return null;
					}
				} else {
					return null;
				}
			
		}		
			return null;
	}

	public void sendPost() throws IOException{
		
		// Initial sendPost
		
		 try {
        
			 URL url = new URL("http://gt.buxingxing.com/api/v1/token");
             HttpURLConnection conn = (HttpURLConnection)url.openConnection();
             conn.setDoOutput(true);
             conn.setDoInput(true);
             conn.setReadTimeout(5000);
             conn.setConnectTimeout(5000);
             conn.setUseCaches(false);
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Content-Type", "application/json");
             conn.connect();
             DataOutputStream out = new DataOutputStream(conn.getOutputStream());
             JSONObject body = new JSONObject();
             //body.put("openid", openid);
             //body.put("token", gwgo_token);
             String json = java.net.URLEncoder.encode(body.toString(), "utf-8");
             out.writeBytes(json);
             out.flush();
             out.close();
             BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             String lines;
             StringBuffer sb = new StringBuffer("");
             while((lines = reader.readLine()) != null) {
                 lines = URLDecoder.decode(lines, "utf-8");
                 sb.append(lines);
             }
             reader.close();
             conn.disconnect();
         } catch (Exception e) {
             e.printStackTrace();
         }
     
		// Final sendPost
		
	}
	
	public void testConnectionGet() {
		// Test new method
		HttpURLConnection connection = null;
		//urlString + m
		try {
			URL url = new URL("http://www.json-generator.com/j/cglqaRcMSW?indent=4");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setUseCaches(false);
			connection.setAllowUserInteraction(false);
			connection.connect();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer stringBuffer = new StringBuffer();
			
			String output;
			
			while((output = bufferedReader.readLine()) != null ){
				stringBuffer.append(output);
			}
			
			bufferedReader.close();
			System.out.println(stringBuffer.toString());
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if( connection != null){
				try{
					connection.disconnect();					
				} catch (Exception ex) {
					System.out.println("Error");
				}
			}
		}
	
		// Finaly new Method
	}
	
}
