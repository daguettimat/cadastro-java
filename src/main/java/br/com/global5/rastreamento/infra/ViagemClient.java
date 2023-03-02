package br.com.global5.rastreamento.infra;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.jr.ob.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ViagemClient {
	
	protected boolean usingHTTPS = false;
	protected JSON service;

	//http://186.250.92.150:9090/ws_rest/public/api/viagem?DataFinalizacaoI=01/04/2019&DataFinalizacaoF=30/04/2019&UltCodigo=1	
	//public static final String SERVICE_HOST = "186.250.92.150:9090/ws_rest/public/api/viagem";
	
	// Anterior a mudanca da porta 443 gcadastro.global5.com.br:443/ws_rest/public/api/viagem
	// Anterior a mudanca da porta 443 gcadastro.global5.com.br:443/ws_rest/public/api/transportador
	
	// WS da Base Produção ||.header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")|| 
	public static final String SERVICE_HOST = "192.168.64.101/ws_rest/public/api/viagem";
	public static final String SERVICE_HOST_EMAIL = "192.168.64.101/ws_rest/public/api/transportador";
	

		
	/**
	 * Construtor padrao
	 */
	public ViagemClient(){
		service = JSON.std;
	}
	
	/**
	 * Construtor que permite que seja setado um {@link JSON} customizado
	 */
	public ViagemClient(JSON service){
		this.service = service;
	}
	
	/**
	 * Chamada para o consumo do WsCliente / viagem id especifico
	 * @return
	 */
	public JSONArray getViagemBI(Integer idViagem){
		// [GET] http://ip.do.client:porta/ws_rest/public/api/viagem/
		String urlString = getHost() + "/" +idViagem;
		
		Client restClient = Client.create();
		
		WebResource webResource = restClient.resource(urlString);
		ClientResponse resp = webResource.accept("application/json")
											.header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")
											.get(ClientResponse.class);
		
		if(resp.getStatus() != 200){
			System.err.println("Não é possivel conectar com o servidor");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não é possível conectar com o servidor!") );
		}
		
		String output = resp.getEntity(String.class);
		
		if( !output.equals("")){
			//AQUI ERROR
			JSONObject obj = new JSONObject(output);
			
			int returnObj = obj.toString().length();
			
			if( returnObj > 20){
				
				JSONArray arrayJson = obj.getJSONArray("viagens");
				
				if(arrayJson.length() >= 0){
					return arrayJson;
				} else {
					return null;
				}
				
			} else {
				return null;
			}			
			
		}		
		
		return null;  
	}

	//JSONObject
	public String getTransportador(String cnpjTransportador){
		// [GET] http://ip.do.client:porta/ws_rest/public/api/viagem/
		String urlString = getHostTransportador() + "/" +cnpjTransportador;
		
		Client restClient = Client.create();
		
		WebResource webResource = restClient.resource(urlString);
		ClientResponse resp = webResource.accept("application/json")
											.header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")
											.get(ClientResponse.class);
		
		if(resp.getStatus() != 200){
			System.err.println("Não é possivel conectar com o servidor");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não é possível conectar com o servidor!") );
		}
		
		try {
			if(resp.getStatus() == 500){			
				System.err.println("Erro 500 na CHP. Pedir apoio ao suporte técnico: Consulta ao Transportador");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Erro 500 na CHP. Pedir apoio ao suporte técnico: Consulta ao Transportador!") );		
			}
			
		} catch (Exception e) {
			
		}
		
		String output = resp.getEntity(String.class);
		
		if( !output.equals("")){
			
			JSONObject obj = new JSONObject(output);
			
				int returnObj = obj.toString().length();
				
				if( returnObj > 20){

					//String jsonEmailsTransp = jsonObjectTransp.opt("contatos").toString();
					JSONObject jsonObjectTransp = obj.getJSONObject("transportador");					
					JSONArray jsonPlacas = jsonObjectTransp.getJSONArray("contatos");					
					int re = jsonObjectTransp.getJSONArray("contatos").length();

					String emails= "";
					
					for (int i =0; i < re; i++){
						
						if(!jsonPlacas.getJSONObject(i).opt("email").toString().equals("null")){
							emails += jsonPlacas.getJSONObject(i).opt("email").toString().toLowerCase() +";";
						}						
						
					}
					
					if(jsonObjectTransp.length() >= 0){
						return  emails;
					} else {
						return null;
					}
					
				} else {
					return null;
				}			
		}				
		return null;  
	}
	

	/**
	 * Chamada para o consumo do WsClient /viagem
	 * @param ultCodigo 
	 * @param dtFinalF 
	 * @param dtFinalI 
	 */
	public JSONArray getViagem(Integer ultCodigo, String dtFinalI, String dtFinalF){
		//http://186.250.92.150:9090/ws_rest/public/api/viagem?DataFinalizacaoI=01/04/2019&DataFinalizacaoF=30/04/2019&UltCodigo=1
		String dataFinalizacaoI = "?DataFinalizacaoI="+dtFinalI;
		String dataFinalizacaoF = "&DataFinalizacaoF="+dtFinalF;
//		String dataFinalizacaoI = "?DataInicioI="+dtFinalI;
//		String dataFinalizacaoF = "&DataInicioF="+dtFinalF;
		String ultimoCodigo = "&UltCodigo="+ultCodigo;
	
		String urlString = getHost() + dataFinalizacaoI + dataFinalizacaoF + ultimoCodigo;
		
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlString);
		ClientResponse resp = webResource.accept("application/json")
										 //.header("Authorization", "Basic V1NXUzoxMDEw")
										 .header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")
										 .get(ClientResponse.class);
		
		if( resp.getStatus() != 200 ){
			System.err.println("Não é possível conectar com o servidor");
		}
		
		String output = resp.getEntity(String.class);
	
		// Retorno output quando não tem dados --> {"viagens":null} 				

		if ( !output.equals("")) {

			JSONObject obj = new  JSONObject(output);
			
			int returnObj = obj.toString().length();
			
			if ( returnObj > 20 ) {
				// veja obj na 2 mao
				JSONArray arrayJson = obj.getJSONArray("viagens");
				
				if ( arrayJson.length() > 0 ) {
					return arrayJson;
				} else {
					return null;
				}
				
			} else {
				
				return null;
			}
	
		}
		
		return null;  		
		
	}
	
	/**
	 * Método interno que retorna o host do webservice chp (agora interno wsg5). Por padrão utiliza http://localhost:8085/veiculosid/{id} retorna 100 apartir do id
	 */
	private String getHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST;
		return host;
	}
	
	private String getHostTransportador(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST_EMAIL;
		return host;
	}
	
	/**
	 * Retorna se o client está utilizando HTTP ou HTTPS para consultar os web services. Por padrão utiliza HTTP.
	 */
	private boolean isUsingHTTPS(){
		return usingHTTPS;
	}

	/**
	 *	Seta se o client deve utilizar HTTPS para consultar os web services. Por padrão é <code>false</code>. 
	 */
	public void setUsingHTTPS(boolean usingHTTPS) {
		this.usingHTTPS = usingHTTPS;
	}
	
	/**
	 * Retorna o {@link JSON} utilizado na desserialização de objetos.
	 */
	public JSON getService() {
		return service;
	}

	/**
	 * Seta um {@link JSON} customizado para a desserialização de objetos.
	 */
	public void setService(JSON service) {
		this.service = service;
	}
		
}
