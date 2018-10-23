package br.com.global5.infra.util;

import br.com.global5.manager.model.criminal.Consulta;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import com.fasterxml.jackson.jr.ob.JSON;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



/**
 * Classe de acesso aos web services da ViaCEP para Java SE e Android.
 * Utiliza Jackson-jr para a serialização de JSON para objetos Java.
 * 
 * @author J R Zielinski
 *
 * @since v.1.0.0
 */
public class ViaCEPClient {
	
	protected boolean usingHTTPS = false;
	protected JSON service;
	
	/**
	 * Construtor padrão.
	 */
	public ViaCEPClient(){
		service = JSON.std;
	}
	
	/**
	 * Construtor que permite que seja setado um {@link JSON} customizado.
	 */
	public ViaCEPClient(JSON service){
		this.service = service;
	}

	/**
	 * Executa a consulta de endereço a partir de um CEP.
	 * 
	 * @param cep CEP da localidade onde se quer consultar o endereço. Precisa ter 8 dígitos - a formatação é feita pelo cliente.
	 * CEPs válidos (que contém 8 dígitos): "20930-040", "abc0 1311000xy z", "20930 040". CEPs inválidos (que não contém 8 dígitos): "00000", "abc", "123456789"
	 * 
	 * @return O endereço encontrado para o CEP, ou <code>null</code> caso não tenha sido encontrado.
	 * @throws IOException em casos de erro de conexão.
	 * @throws IllegalArgumentException para CEPs que não possuam 8 dígitos.
	 */
	public ViaCEPEndereco getEndereco(String cep) throws IOException {
		char[] chars = cep.toCharArray();
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i< chars.length; i++){
			if (Character.isDigit(chars[i])){
				builder.append(chars[i]);
			}
		}
		cep = builder.toString();
		
		if (cep.length() != 8){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"CEP inválido!<br/>" ,"O sistema realiza a validação do cep, por favor verifique e tente novamente! "));

		}
		
		String urlString = getHost() + cep + "/json/";
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			ViaCEPEndereco obj = getService().beanFrom(ViaCEPEndereco.class, in);
			if (obj == null || obj.getCep() == null){
				return null;
			}
			return obj;
		}
		finally {
			urlConnection.disconnect();
		}
	}
	
	/**
	 * Executa a consulta de endereços a partir da UF, localidade e logradouro
	 * 
	 * @param uf Unidade Federativa. Precisa ter 2 caracteres.
	 * @param localidade Localidade (p.e. município). Precisa ter ao menos 3 caracteres.
	 * @param logradouro Logradouro (p.e. rua, avenida, estrada). Precisa ter ao menos 3 caracteres.
	 * 
	 * @return Os endereços encontrado para os dados enviados, nunca <code>null</code>. Caso não sejam encontrados endereços, uma lista vazia é retornada.
	 * @throws IOException em casos de erro de conexão.
	 * @throws IllegalArgumentException para localidades e logradouros com tamanho menor do que 3 caracteres.
	 */
	public List<ViaCEPEndereco> getEnderecos(String uf, String localidade, String logradouro) throws IOException {
		if (uf == null || uf.length() != 2){
			throw new IllegalArgumentException("UF inválida - deve conter 2 caracteres: " + uf);
		}
		if (localidade == null || localidade.length() < 3){
			throw new IllegalArgumentException("Localidade inválida - deve conter pelo menos 3 caracteres: " + localidade);
		}
		if (logradouro == null || logradouro.length() < 3){
			throw new IllegalArgumentException("Logradouro inválido - deve conter pelo menos 3 caracteres: " + logradouro);
		}
		
		String urlString = getHost() + uf + "/" + localidade + "/" + logradouro + "/json/";
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			List<ViaCEPEndereco> obj = getService().listOfFrom(ViaCEPEndereco.class, in);
			return obj;
		}
		finally {
			urlConnection.disconnect();
		}
	}
	
	/**
	 * Método interno que retorna o host dos webservices da ViaCEP. Por padrão é "http://viacep.com.br/ws/".
	 */
	protected String getHost(){
		String host = (isUsingHTTPS() ? "https://" : "http://") + ViaCEPConstants.SERVICE_HOST;
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
	
    @SuppressWarnings("unused")
	private static String hmacSha1(String value, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(value.getBytes());

            byte[] hexBytes = new Hex().encode(rawHmac);

            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }	
	
	//
	// Exemplo de chamada
	//
	public static void main(String[] args) throws IOException {
		
	    
//        URL url = new URL("http://sinespcidadao.sinesp.gov.br/sinesp-cidadao/mobile/consultar-placa?WSDL");
//        //QName qname = new QName("http://soap.ws.placa.service.sinesp.serpro.gov.br/","ConsultaPlacaMobileService");
//        ConsultaPlacaMobileService ws = new ConsultaPlacaMobileService(url);
//
//        ConsultaPlacaMobile consulta = ws.getPort(ConsultaPlacaMobile.class);
//
//        BindingProvider bindingProvider = (BindingProvider) consulta;
//        bindingProvider.getRequestContext().put(
//              BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//              "http://sinespcidadao.sinesp.gov.br/sinesp-cidadao/mobile/consultar-placa");
//
//        GetStatus parameters = new GetStatus();
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//
//        String placa = "AMG8885";
//
//        parameters.setA(placa);
//
//        GetStatusResponse response = consulta.getStatus(parameters
//        		        , "samsung GT-I9192"
//        		        , "ANDROID"
//        		        , "4.4.2"
//        		        , "SinespCidadao"
//        		        , "177.54.144.208"
//        		        , HmacUtils.hmacSha1Hex(placa, placa + "TRwf1iBwvCoSboSscGne")  // parametro
//        		        , ""
//        		        , "-38.5284057"
//        		        , ""
//        		        , "-3.7583078"
//        		        , dateFormat.format(date)  // parametro
//        		        ,"8797e74f0d6eb7b1ff3dc114d4aa12d3");
//
//        System.out.println(response.getReturn().toString());
        
		
//		ViaCEPClient client = new ViaCEPClient();
//		ViaCEPEndereco endereco = client.getEndereco("83305210");
//		System.out.println("Chamada via Cep :" + endereco.getUf() + " - " + endereco.getLocalidade() +" - " + endereco.getBairro() +" - " + endereco.getLogradouro()); 
//
//		
//		for(ViaCEPEndereco end : client.getEnderecos("PR","PIRAQUARA", "RUA MARTIMIANO SABINO DA SILVA")) {
//		    System.out.println(end.toString()); //São Paulo
//		}
		
		String teste = "{\"return\":{\"codigo_retorno\":\"08\",\"mensagem\":\"Constam resultados para esta solicitação\",\"dados_solicitacao\":{\"cpf_pessoa\":\"55222978915\",\"codigo_solicitacao\":352914,\"data_solicitacao\":\"2017-05-23 13:53:10\",\"login_solicitacao\":\"WS-GLOBAL5\"},\"processos\":{\"processo\":{\"numero_processo\":\"0300122-79.2016.8.24.0051\",\"uf\":\"SC\",\"data_processo\":\"2016-02-22\",\"area_processo\":\"CÍVEL\",\"classe_processual\":\"PROCEDIMENTO ORDINÁRIO\",\"assuntos_processo\":[\"FORNECIMENTO DE MEDICAMENTOS\",\"ASSISTÊNCIA JUDICIÁRIA GRATUITA\"],\"situacao_processo\":\"SUSPENSO\",\"comarca\":\"PONTE SERRADA\",\"parte\":[{\"nome_parte\":\"CLODINEY PEDRO CHRISTIANETTI FERREIRA\",\"parte_pesquisada\":true,\"cpf\":\"55222978915\",\"data_nascimento\":\"1966-07-05\",\"tipo_parte\":\"AUTOR\",\"situacao\":\"ATIVO\"},{\"nome_parte\":\"ESTADO DE SANTA CATARINA  PROCDOR:  MARIO SERGIO SIMAS\",\"parte_pesquisada\":false,\"tipo_parte\":\"RÉU\",\"situacao\":\"ATIVO\"}]}}}}";
        Gson gson = new Gson();
        Consulta obj =  gson.fromJson(teste, Consulta.class);


		System.out.println(obj.toString());
	}
	
}