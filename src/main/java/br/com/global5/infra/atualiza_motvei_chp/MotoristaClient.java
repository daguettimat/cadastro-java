package br.com.global5.infra.atualiza_motvei_chp;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.jr.ob.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MotoristaClient {
	
	protected boolean usingHTTPS = false;
	protected JSON service;
	
	// 192.168.64.101/ws_rest/public/api/motorista/
	//[PUT] http://ip.do.client:porta/ws_rest/public/api/motorista/IdMotorista
	public static final String SERVICE_HOST = "192.168.64.101/ws_rest/public/api/motorista";
	
	
	
	// Construtor padrao
	public MotoristaClient(){
		service = JSON.std;
	}
	
	// Construtor que permite que seja setado um {@link JSON} customizado
	public MotoristaClient(JSON service){
		this.service = service;
	}
	
	private String putHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST;
		return host;
	}
	
	/**
	 * Método interno que retorna o host do webservice chp (agora interno wsg5). 
	 */
	private String getHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST;
		return host;
	}

	/**
	 * Retorna se o client está utilizando HTTP ou HTTPS para consultar os web services. Por padrão utiliza HTTP.
	 */
	public boolean isUsingHTTPS() {
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
	
	/**
	 * @author Francis Bueno
	 * @param idMotorista
	 * @param cpfMotorista
	 * @param nomeMotorista
	 * @param rgMotorista
	 * @param cnhMotorista
	 * @param cnhCategoriaMotorista
	 * @param cnhValidadeMotorista
	 * @param cnhUfMotorista
	 * @param cnhSegurancaMotorista
	 * @return true or false
	 * Atualiza o banco de dados do sistema Trafegus pelo id do motorista, cpf é obrigatório informar
	 */
	public boolean putMotorista(String idMotorista, String cpfMotorista, String nomeMotorista, String rgMotorista,
			String cnhMotorista, String cnhCategoriaMotorista, String cnhValidadeMotorista,
			String cnhUfMotorista, String cnhSegurancaMotorista , 			
			String ufRgMotorista, String dtNascMotorista, String cnhDtPrimEmissao,
			String nomePaiMotorista , String nomeMaeMotorista, String logradouroMotorista, String cepMotorista, String numEnderecoMotorista,
			String ufEnderecoMotorista, String paisEnderecoMotorista, String bairroEnderecoMotorista , String complEnderecoMotorista,
			String cidadeEnderecoMotorista) {			 		
		
		String urlString = putHost() + "/" + idMotorista;
		
		Client restClient = Client.create();
		
		WebResource webResource = restClient.resource(urlString);
		
		String inputV = "{\"cpf_motorista\":\"" + cpfMotorista + "\",";
		
		if (nomeMotorista != null ){
			inputV +=  "\"nome\":\"" + nomeMotorista + "\",";
		}
		if (rgMotorista  != null ){
			inputV +=  "\"rg\":\""  + rgMotorista  + "\",";
		}
		if (cnhMotorista != null ){
			inputV +=  "\"nro_cnh\":\"" + cnhMotorista + "\",";
		}
		if (cnhCategoriaMotorista != null ){
			inputV +=  "\"categoria_cnh\":\"" + cnhCategoriaMotorista + "\",";
		}
		if (cnhValidadeMotorista != null ){
			inputV += "\"validade_cnh\":\"" + cnhValidadeMotorista + "\"," ;
		}
		if (cnhSegurancaMotorista != null ){
			inputV += "\"cnh_seg\":\"" + cnhSegurancaMotorista  + "\",";
		}
		if (cnhUfMotorista != null ){
			inputV +=  "\"cnh_uf\":\"" + cnhUfMotorista + "\",";			
		}		

		if (ufRgMotorista != null ){
			inputV +=  "\"rg_uf\":\"" + ufRgMotorista + "\",";			
		}		
		if (dtNascMotorista != null ){
			inputV +=  "\"data_nasc\":\"" + dtNascMotorista + "\",";			
		}

 		if (cnhDtPrimEmissao != null ){
			inputV +=  "\"moto_data_primeira_habilitacao\":\"" + cnhDtPrimEmissao + "\",";			
		}	
		if (nomePaiMotorista != null ){
			inputV +=  "\"nome_pai\":\"" + nomePaiMotorista + "\",";			
		}
		if (nomeMaeMotorista != null ){
			inputV +=  "\"nome_mae\":\"" + nomeMaeMotorista + "\",";			
		}		
	
		if (logradouroMotorista != null ){
			inputV +=  "\"logradouro\":\"" + logradouroMotorista + "\",";			
		}
		if (cepMotorista != null ){
			inputV +=  "\"cep\":\"" + cepMotorista + "\",";			
		}
		if (numEnderecoMotorista != null ){
			inputV +=  "\"numero\":\"" + numEnderecoMotorista + "\",";			
		}		
		if (ufEnderecoMotorista != null ){
			inputV +=  "\"sigla_estado\":\"" + ufEnderecoMotorista + "\",";			
		}
		if (paisEnderecoMotorista != null ){
			inputV +=  "\"pais\":\"" + paisEnderecoMotorista + "\",";			
		}
		if (bairroEnderecoMotorista != null ){
			inputV +=  "\"bairro\":\"" + bairroEnderecoMotorista + "\",";			
		}		
		if (complEnderecoMotorista != null ){
			inputV +=  "\"complemento\":\"" + complEnderecoMotorista + "\",";			
		}	
		if (cidadeEnderecoMotorista != null ){
			inputV +=  "\"cidade\":\"" + cidadeEnderecoMotorista + "\",";			
		}					
		
		inputV +=  "\"vigilante\":null }";
		
		
		ClientResponse resp = webResource.type("application/json")
											.header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")
											.put(ClientResponse.class, inputV);	
		
		if ( resp.getStatus() == 201 ) {
			resp.close();
			// Caso atualize
			return true;
			
		}		
		// Caso não atualize
		resp.close();
		return false;
	}
	
	/**
	 * @author Francis Bueno
	 * Pesquisa se o motorista consultado existe na base de dados do sistema chp;
	 */
	public JSONObject getMotorista(String cpfMotorista){
		// [GET] http://ip.do.client:porta/ws_rest/public/api/motorista/
		String urlString = getHost() + "/" + cpfMotorista;
		
		Client restClient = Client.create();
		
		WebResource webResource = restClient.resource(urlString);
		ClientResponse resp = webResource.accept("application/json")
											.header("Authorization", "Basic V1NHTE9CQUw1OldTNDU4")
											.get(ClientResponse.class);
		
		if(resp.getStatus() != 200){
			resp.close();
			//System.err.println("Não é possivel conectar com o servidor");
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não é possível conectar com o servidor!") );
		}
	
		if(resp.getStatus() == 500){	
			resp.close();
			//System.err.println("Erro 500 na CHP. Pedir apoio ao suporte técnico: Consulta ao Transportador");
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Erro 500 na CHP. Pedir apoio ao suporte técnico: Consulta ao Transportador!") );		
		}
		
		String output = resp.getEntity(String.class);		
		if( !output.equals("")){
			JSONObject obj = new JSONObject(output);
				int returnObj = obj.toString().length();				
				if( returnObj > 20){
					JSONObject jsonObjectMotorista = obj.getJSONObject("motorista");						
					if(jsonObjectMotorista.length() >= 0){
						resp.close();
						return  jsonObjectMotorista;
					} else {
						return null;
					}				
				} else {
					return null;
				}			
		}				
		return null;  
	}
}
