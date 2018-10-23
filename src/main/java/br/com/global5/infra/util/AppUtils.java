package br.com.global5.infra.util;

import br.com.global5.infra.converter.HexStringConverter;
import br.com.global5.infra.json.JacksonUtil;
import br.com.global5.infra.model.wsCNPJ;
import br.com.global5.infra.model.wsCPF;
import br.com.global5.manager.model.cadastro.FichaCliente;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.model.UploadedFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AppUtils {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[]{'G', 'l', 'o', 'b', 'a', 'l', 'C', 'i', 'n', 'c', 'o', 'S', 'e', 'n', 'h', 'a'};


    private static Properties prop = new Properties();
    private static InputStream input = null;

    private static String ambiente;
    // load a properties file

	public static final String dirImagens = "/var/www/fotos/";
	public static final boolean enviarEmail = true;

    public static String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


	public static String gerarNovaSenha() {
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
				"y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z", "!", "@", "#", "$", "%", "ˆ", "&", "*" };

		String senha = "";

		for (int x = 0; x < 10; x++) {
			int j = (int) (Math.random() * carct.length);
			senha += carct[j];

		}

		return senha;
	}

	public static boolean checkFileFolder(String folder) {

		return Files.exists(Paths.get(folder).getFileName());
	}

	public static String saveImage(String path, UploadedFile upload, int id, String tipo) {
		if (upload == null) {
			return null;
		}
		String extesion = FilenameUtils.getExtension(upload.getFileName());
		try {
			
			InputStream is = upload.getInputstream();
			byte[] bytes = IOUtils.toByteArray(is);
			FileOutputStream fileOuputStream = new FileOutputStream(path + tipo + " - " + id + "." + extesion);
			fileOuputStream.write(bytes);
			fileOuputStream.close();

			return tipo + " - " +  id + "." + extesion;
			

		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Documentos  " + id
							+ " não terá sua imagem enviada por um erro não tratado.", null));

		}
		return null;
	}

    public static String foto(int id, String urlfoto) {
        String result = "";

        try {

        	if( urlfoto == null ) {
				Path dir = FileSystems.getDefault().getPath( AppUtils.dirImagens + "/");
				DirectoryStream<Path> stream = Files.newDirectoryStream( dir, id + ".{jpg,jpeg,png,gif,bmp,JPG,JPEG,PNG,GIF,BMP}" );
				for (Path path : stream) {
					result = AppUtils.imageName(AppUtils.dirImagens + "/" + path.getFileName().toString(), true);
					break;
				}
                stream.close();
				return result;
			} else {
				if (urlfoto.length() == 0) {
					result = AppUtils.imageName("sem_imagem.png", true);
				} else {
					result = AppUtils.imageName(urlfoto, true);
				}
			}


		} catch (Exception e ) {
			result = AppUtils.imageName("sem_imagem.png", true);
		}
		return result;

//
//
//        try {
//            if( urlfoto == null ) {
//                List<String> results = new ArrayList<String>();
//
//                File[] files = new File(AppUtils.dirImagens).listFiles(new FilenameFilter() {
//                    @Override public boolean accept(File dir, String name) {
//                        return name.startsWith(idMotorista);
//                    }
//                });
//                for (File file : files) {
//                    if (file.isFile()) {
//                        results.add(file.getName());
//                    }
//                }
//                result = AppUtils.imageName(results.get(0).toString(), true);
//            } else {
//                if (urlfoto == null) {
//                    result = AppUtils.imageName("sem_imagem.png", true);
//                } else {
//                    result = AppUtils.imageName(urlfoto, true);
//                }
//            }
//        } catch (Exception e ) {
//            result = AppUtils.imageName("sem_imagem.png", true);
//        }
//        return result;

    }

	public static String readFile( String file ) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader( new FileReader(file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}

		return stringBuilder.toString();
	}

	public static Map<String,String> NamedParams(String... params) {
	    Map<String, String> map = new HashMap<String, String>();
        for(int i=0;i<params.length;i++){
            map.put(params[i],params[++i]);
        }
	    return map;
    }

    public static Image qrCode(String codeText) {
		int size = 250;
		try {

			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, size,
					size, hintMap);
			int qrWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(qrWidth, qrWidth,
					BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, qrWidth, qrWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < qrWidth; i++) {
				for (int j = 0; j < qrWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			return image;
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public static String toMd5(String valor) {
        MessageDigest mDigest;
        try {
            //Instanciamos o nosso HASH MD5, poderíamos usar outro como
            //SHA, por exemplo, mas optamos por MD5.
            mDigest = MessageDigest.getInstance("MD5");

            //Convert a String valor para um array de bytes em MD5
            byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));

            //Convertemos os bytes para hexadecimal, assim podemos salvar
            //no banco para posterior comparação se senhas
            StringBuffer sb = new StringBuffer();
            for (byte b : valorMD5){
                sb.append(Integer.toHexString((b & 0xFF) |
                        0x100).substring(1,3));
            }
            String a = sb.toString();
            String b = a;
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return null;
        }
    }

	/**
	 * @param inputData
	 * @return
	 */
	public static String HashedValue(String inputData) {
		String sResp = null;
		try {
			byte byteHash[];
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.update(inputData.getBytes("utf-8"));
			byteHash = sha1.digest();
			sha1.reset();

			String hash = new BASE64Encoder().encode(byteHash);

			return hash.substring(0, (hash.length()/2));
		} catch (Exception e) {
			System.err.println("getHashedValue failed: " + e.getMessage());
			return null;
		}
	}

    public static boolean compareGroup(Collection<Integer> c, List<Integer> compare) {

	    for(Integer o : c) {
	        if( ! containsCondition(compare, o)) {
	            return false;
            }
        }
        return true;
    }


	public static boolean containsCondition(Collection<Integer> c, Integer compare) {
		for(Integer o : c) {
			if(o != null && o.equals(compare)) {
				return true;
			}
		}
		return false;
	}

	public static Integer statusFinal(Collection<Integer> cStatus) {

		List<Integer> lista = new ArrayList<>(Arrays.asList(35, 39, 139));

		if( containsCondition(cStatus, 37)) {
			return 143;
		} else {
			if(compareGroup(cStatus, lista) ) {
				return 142;
			} else {
				lista = new ArrayList<>(Arrays.asList(38, 39));
				if( compareGroup(cStatus, lista)) {
					return 5;
				} else {
					return 5;
				}
			}
		}
	}

	public static Integer statusRecord(Collection<Integer> cStatus) {

	    List<Integer> lista = new ArrayList<>(Arrays.asList(35, 39, 139));

        if( containsCondition(cStatus, 37)) {
            return 37;
        } else {
            if(compareGroup(cStatus, lista) ) {
                return 35;
            } else {
                lista = new ArrayList<>(Arrays.asList(38, 39));
                if( compareGroup(cStatus, lista)) {
                    return 38;
                } else {
                    return 138;
                }
            }
        }
	}

	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }


	public static Float parse(final String amount, final Locale locale) throws ParseException {
			final NumberFormat format = NumberFormat.getNumberInstance(locale);
			if (format instanceof DecimalFormat) {
				((DecimalFormat) format).setParseBigDecimal(true);
			}
			return ((BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""))).floatValue();
	}


	/**
	 * Get a time line date
	 * @param data the date to convert in string timeline
	 * @return the date in string value
	 */
    public static String toTimeline(String data) throws ParseException {

        String timeDisplay = "";
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        Date date = fmt.parse(data);

        Map<TimeUnit,Long> result = AppUtils.computeDiff(
                date,
                new Date()
        );


        if( result.get(TimeUnit.MINUTES) > 0 ) {
            timeDisplay = result.get(TimeUnit.MINUTES) + " Min";
        }

        if( result.get(TimeUnit.HOURS) > 0 ) {
            if(timeDisplay.length() > 0) {
                timeDisplay = result.get(TimeUnit.HOURS) + " Horas e  " + timeDisplay;
            } else {
                timeDisplay = result.get(TimeUnit.HOURS) + " Horas";
            }
        }

        if( result.get(TimeUnit.DAYS) > 0 ) {
            if(timeDisplay.length() > 0) {
                timeDisplay = result.get(TimeUnit.DAYS) + " Dias,  " + timeDisplay;
            } else {
                timeDisplay = result.get(TimeUnit.DAYS) + " Dias";
            }
        }

        if(timeDisplay.length() > 0) {
            timeDisplay += " atrás";
        } else {
            SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
            try {
                timeDisplay =  fmtHora.parse(data).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return timeDisplay;

    }

	public static boolean isInteger(String str) {
	    return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
	}

	public static String Left(String str, Integer n){
		if (n <= 0)
		    return "";
		else if (n > str.length())
		    return str;
		else
		    return str.substring(0,n);
	}
	
	public static String Right(String str, Integer n){
	    if (n <= 0)
	       return "";
	    else if (n > str.length())
	       return str;
	    else {
	       int iLen = str.length();
	       return str.substring(iLen, iLen - n);
	    }
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] HexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	
	/**
	 * Grava arquivos no Servidor
	 * @param arquivo java.io.File
	 * @param arqNome nome do arquivo
	 * @param dirNome diret�rio a ser gravado
	 * @throws EsferaException
	 */
//	@SuppressWarnings("resource")
//	public static void copiarArquivo
//		(File arquivo, String arqNome, String dirNome) 
//			throws Exception {
//		
//		try {
//			
//			FileChannel source       = new FileInputStream(arquivo).getChannel();
//			FileChannel destionation = new FileOutputStream(new File((dirNome == null 
//				? AppConstantes.PATH_ARQUIVOS : dirNome) + arqNome)).getChannel();
//			
//			source.transferTo(0, source.size(), destionation);
//			
//			source.close();
//			destionation.close();
//			
//		} catch (IOException e) {
//			throw new Exception(e);
//		}
//	}
	
	/**
	 * Exclui um arquivo do servidor
	 * @param arqNome nome do arquivo
	 * @param dirNome diret�rio do arquivo
	 * @return true se sucesso, false caso erro
	 */
	public static boolean excluirArquivo(String arqNome, String dirNome){
		return new File((dirNome+arqNome)).delete();
	}
	
	/**
	 * Valida e Formata uma String
	 * @param valor String a formatar
	 * @return valor se diferente de nulo e tamanho maior que 0, caso contr�rio nulo 
	 */
	public static String validarString(String valor) {
		
		String string = null;
		
		if (valor == null || valor.trim().length() == 0) string = null; 
		if (valor != null && valor.trim().length() > 0) string = valor.trim(); 
		
		return string;
	}
	
	/**
	 * Converte uma String em Double
	 * @param valor String para converter
	 * @return Double
	 */
	public static Double toDecimal(String valor){
		
		Locale brasil       = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(brasil));
		
		try {			
			return new Double(format.parse(valor.trim()).doubleValue());
		} catch (Exception e) {
			return null;
		}
	}

	
	public static String blankWhenNull(Object object){
		
		if (object == null) return "";
		else return object.toString();
	}
	
	public static String currency(Double valor){
		
		return NumberFormat.getCurrencyInstance().format(valor).replace("R$ ", "");
	}
	
	public static String fromCharCode(int code) {
		
		String str = "abcdefghijklmnopqrstuvxywzABCDEFGHIJKLMNOPQRSTUVXYWZ";
		String ret = "";
		
		try {
			
			ret = Character.toString(str.charAt(code));
			
		} catch (IndexOutOfBoundsException e) {
			
			code -= str.length();
			ret = fromCharCode(code)+fromCharCode(str.length()/2);
		}
		
		return ret;
	}
	
	/**
	 * Esse metodo remove todos os elementos null e tambem os elementos que o conteudo � ""
	 * @param param - Recebe um Array de String
	 * @return		- Retorna um Array de String redimencionado
	 */
	public static String[] redimencionaArray(String[] param){
		ArrayList<String> arrayList = new ArrayList<String>();
		for(String s: param){
			if((s!=null) && (s.length()>0) && (!(s.equalsIgnoreCase("")))) {
				arrayList.add(s);
			}
		}		
		String[] retorno = new String[arrayList.size()];
		return  (arrayList.toArray(retorno).length >0  ? arrayList.toArray(retorno) : null);
	}

	/**
	 * Converte uma horario no formato HH:mm em Double
	 * Obs.: Arredonda casa decimal
	 * Retorna null caso paramentro igual a null
	 */
	public static Double horario(String horario) {
		
		Double valor = null;
		
		if (horario != null) {
		
			Integer hh = Integer.parseInt(horario.split(":")[0]);
			Double  mm = (Double.parseDouble(horario.split(":")[1]) / 60);
			
			BigDecimal decimal = new BigDecimal(mm);
			
			valor = hh + decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		return valor;
	}
	
	/**
	 * Converte um horario no formato Double em HH:mm
	 * Obs.: Arredonda casa decimal
	 * Retorna null caso paramentro igual a null
	 */
	public static String horario(Double horario) {
		
		String valor = null;
		
		if (horario != null) {
		
			Integer hh = Integer.parseInt(horario.toString().split("\\.")[0]);
			Double  mm = new Double("0." + horario.toString().split("\\.")[1]) * 60;
	
			BigDecimal decimal = new BigDecimal(mm);
			
			String hhString = "0" + hh;
			String mmString = "0" + decimal.setScale(0, BigDecimal.ROUND_HALF_UP);
			
			valor = hhString.substring(hhString.length()-2, hhString.length()) + ":" + mmString.substring(mmString.length()-2, mmString.length());
		}
		
		return valor;
	}
	
	public static boolean regex(String valor, String regex) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(valor);
		
		boolean found = false;
		
		while (matcher.find()) {
			
			found = true;
		}
		
		return found;
	}
	
	/**
	 * Converte uma String para um objeto Date. Caso a String seja vazia ou nula, 
	 * retorna null - para facilitar em casos onde formul�rios podem ter campos
	 * de datas vazios.
	 * @param data String no formato dd/MM/yyyy a ser formatada
	 * @return Date Objeto Date ou null caso receba uma String vazia ou nula
	 * @throws Exception Caso a String esteja no formato errado
	 */
	public static Date formataData(String data) throws Exception { 
		if (data == null || data.equals(""))
			return null;
		
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = formatter.parse(data);
        } catch (ParseException e) {            
            throw e;
        }
        return date;
	}

	/**
	 * Executa pesquisa na Receita do CNPJ
	 */
	public static wsCNPJ consultaWSCNPJ(String CNPJ) throws Exception {
		// http://ws.hubdodesenvolvedor.com.br/cnpj/?cnpj=10336846000150&token=1154316497089051ABV8428651457.5
        //https://ws.hubdodesenvolvedor.com.br/cpf/?cpf=87630443900&data=05/03/1972&token=1154316497089051ABV8428651457.5

		 String retorno = executeGet("http://ws.hubdodesenvolvedor.com.br/cnpj/?cnpj="+ CNPJ + "&token=1154316497089051ABV8428651457.5");

		 Gson gson = new Gson();
		 return gson.fromJson(retorno, wsCNPJ.class);
	}



    /**
     * Executa pesquisa na Receita por CPF
     */
    public static wsCPF consultaWSCPF(String CPF, String data) {
        // http://ws.hubdodesenvolvedor.com.br/cnpj/?cnpj=10336846000150&token=1154316497089051ABV8428651457.5
        //https://ws.hubdodesenvolvedor.com.br/cpf/?cpf=87630443900&data=05/03/1972&token=1154316497089051ABV8428651457.5

        try {
            String retorno = executeGet("https://ws.hubdodesenvolvedor.com.br/cpf/?cpf=" + CPF + "&data=" + data + "&token=1154316497089051ABV8428651457.5");
            Gson gson = new Gson();
            return gson.fromJson(retorno, wsCPF.class);
        } catch (Exception e) {

        }
        return null;
    }

    // HTTP GET request
    private static String executeGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

	/**
	 * Executa um POST, em endereço web especificado 
	 * retorna null - para facilitar em casos caso nao tenha achado o endereco
	 * @param targetURL - URL de Post, urlParameters - Parametros que a URL necessita
	 * @return Um string de resposta com o conteudo retornado pela pagina
	 * @throws Exception Caso a String esteja no formato errado
	 */	
	public static String executePost(String targetURL, String urlParameters) throws Exception {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();
	
	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();
	
	    } catch (Exception e) {
	
	      e.printStackTrace();
	      return null;
	
	    } finally {
	
	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}
	


        /**
         * Formata o String s com a máscara informada
         * @param s
         * @param mascara
         * @return
         */
        public static String formata(String s, String mascara) {
            MaskFormatter formatter;
            try {
                formatter = new MaskFormatter(mascara);
                formatter.setValueContainsLiteralCharacters(false);
                return formatter.valueToString(s);
            } catch (ParseException e) {
                return s;
            }
        }

        public static String formataCnpj(String s) {
            String entr = StringUtils.leftPad(s, 14, "0");
            return formata(entr, "##.###.###/####-##");
        }

        public static String formataCpf(String s) {
            String entr = StringUtils.leftPad(s, 11, "0");
            return formata(entr, "###.###.###-##");
        }

        public static String formataInscricao(String s) {
            String entr = StringUtils.leftPad(s, 8, "0");
            return AppUtils.formata(entr, "######-#-#");

        }

        /**
         * Formata competência AAAAMM em MM/AAAA
         * @param competencia
         * @return
         */
        public static String formataCompetencia(Integer competencia) {

            Calendar calAux = toCalendar(competencia);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");

            return sdf.format( calAux.getTime() );
        }

		/**
		 * Formata competência dd/mm/AAAA em AAAA/MM
		 * @param date
		 * @return
		 */
		public static String formataCompetencia(Date date) {


			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

			return sdf.format( date );
		}


        /**
         * Valida o penúltimo dígito da inscrição e o DV
         * @param inscricao
         * @return
         */
        public static String validaInscricao(String inscricao, char tipo) {
            if (inscricao.length() < 2) {
                return "Inscrição inválida";
            }

            String penultimo = penultimoDigito(inscricao);
            if ( ! "2".equals(penultimo) && ! "3".equals(penultimo)){
                return "Inscrição inválida";
            }

            if ( "3".equals(penultimo) &&  tipo == 'P'){
                return "Inscrição inválida";
            }

            if (! isDvMod11Ok(inscricao)) {
                return "Inscrição inválida";
            }

            return "";
        }

        public static String penultimoDigito(String inscricao) {
            return StringUtils.substring(inscricao, inscricao.length() -2, inscricao.length() -1);
        }

        public static boolean isDvMod11Ok(String inscricao) {
            return isDvMod11Ok(StringUtils.substring(inscricao, 0, inscricao.length()-1),
                    StringUtils.substring(inscricao, inscricao.length()-1, inscricao.length()),
                    1,
                    9);
        }

        /**

         * Calcula o dígito em Módulo 11 do código dado
         * Retorna o(s) NumDig Dígitos de Controle Módulo 11 do codigo, limitando
         * o valor de multiplicação em qtdLimiteMultiplicacao
         *
         * @param codigo Código cujo dígito se deseja calcular
         * @param qtdDigitosRetorno Quantidade de dígitos verificadores a retornar
         * @param qtdLimiteMultiplicacao Quantidade de dígitos a
         * @return
         */
        public static String calculaDvMod11(String codigo, int qtdDigitosRetorno, int qtdLimiteMultiplicacao) {
            for(int n=1; n<=qtdDigitosRetorno; n++) {
                char[] digito = codigo.toCharArray();
                int soma = 0;
                int multiplicador = 2;

                for(int i=codigo.length()-1; i>=0; i--) {
                    soma += (multiplicador * Integer.parseInt(String.valueOf(digito[i])));
                    if(++multiplicador > qtdLimiteMultiplicacao) {
                        multiplicador = 2;
                    }
                }

                codigo += Integer.valueOf(((soma * 10) % 11) % 10).toString();
            }

            return codigo.substring(codigo.length()-qtdDigitosRetorno);

        }

        /**
         * Calcula o dígito em Módulo 10 do código dado
         * @param codigo codigo do qual se deseja obter o dígito verificador (sem dígito)
         * @return o dígito verificador
         */

        public static String calculaDvMod10(String codigo) {

            char[] digito = codigo.toCharArray();
            int multiplicador = 2;
            String stringAux = "";

            for (int i=codigo.length()-1; i>=0; i--) {

                stringAux = ( multiplicador * Integer.parseInt( String.valueOf(digito[i]) ) ) + stringAux;

                if (--multiplicador<1) {

                    multiplicador = 2;

                }

            }

            int soma = 0;

            digito = stringAux.toCharArray();

            for (int i=0; i<stringAux.length(); i++) {

                soma = soma + Integer.parseInt(String.valueOf(digito[i]));

            }

            soma = soma % 10;

            if (soma != 0) {

                soma = 10 - soma;

            }
            return Integer.valueOf(soma).toString();
        }
        /**
         * Verifica se o dígito é correto para o código fornecido
         * @param codigo Código sem dígito
         * @param digito Dígito que se deseja verificar
         * @param qtdDigitosRetorno Quantidade de dígitos verificadores esperados
         * @param qtdLimiteMultiplicacao Quantidade de dígitos para multiplicar
         * @return true ou false
         */
        public static boolean isDvMod11Ok(String codigo, String digito, int qtdDigitosRetorno, int qtdLimiteMultiplicacao) {
            return calculaDvMod11(codigo, qtdDigitosRetorno, qtdLimiteMultiplicacao).equals(digito);
        }

        /**
         * transforma Integer no formato YYYYMM em Calendar com 01 no dia
         * @param competencia
         * @return
         */
        public static Calendar toCalendar(Integer competencia) {
            String cpt = competencia.toString();
            Calendar calendar = DateUtils.truncate(Calendar.getInstance(), Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Integer.parseInt(StringUtils.substring(cpt, 4, 6)) -1);
            calendar.set(Calendar.YEAR, Integer.parseInt(StringUtils.substring(cpt, 0, 4)));

            return calendar;
        }

        /**
         * transforma String no formato YYYYMMDD em Calendar
         * @param data
         * @param lenient - levanta exceção caso a data seja inválida
         * @return null caso string vazio
         */
        public static Calendar stringToCalendar(String data, boolean lenient) {
            if (StringUtils.isBlank(data)) {
                return null;
            }
            Calendar calendar = DateUtils.truncate(Calendar.getInstance(), Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(StringUtils.substring(data, 6, 8)));
            calendar.set(Calendar.MONTH, Integer.parseInt(StringUtils.substring(data, 4, 6)) -1);
            calendar.set(Calendar.YEAR, Integer.parseInt(StringUtils.substring(data, 0, 4)));
            calendar.setLenient(lenient);
            return calendar;
        }

        /**
         * transforma String no formato YYYYMMDD em Date
         * @param data
         * @param lenient - levanta exceção caso a data seja inválida
         * @return null caso string vazio
         */
        public static Date stringToDate(String data, boolean lenient) {
            if (StringUtils.isBlank(data)) {
                return null;
            }
            return stringToCalendar(data, lenient).getTime();
        }

        /**
         * Calcula a competência imediatamente anterior
         * @param competencia
         * @return
         */
        public static Integer calculaCompetenciaAnterior(Integer competencia) {
            String cpt = competencia.toString();
            String ano = StringUtils.substring(cpt, 0, 4);
            String mes = StringUtils.substring(cpt, 4, 6);
            if ("01".equals(mes)) {
                mes = "12";
                ano =  "" + (Integer.parseInt(ano) -1);
            } else {
                mes =  StringUtils.leftPad("" + (Integer.parseInt(mes) -1), 2, "0");
            }

            return Integer.valueOf(ano + mes);
        }

        /**
         * Calcula a próxima competência
         * @param competencia
         * @return
         */
        public static Integer calculaProximaCompetencia(Integer competencia) {
            String cpt = competencia.toString();
            String ano = StringUtils.substring(cpt, 0, 4);
            String mes = StringUtils.substring(cpt, 4, 6);
            if ("12".equals(mes)) {
                mes = "01";
                ano =  "" + (Integer.parseInt(ano) +1);
            } else {
                mes =  StringUtils.leftPad("" + (Integer.parseInt(mes) +1), 2, "0");
            }

            return Integer.valueOf(ano + mes);
        }

        /**
         * Verifica se a dataDocumento é anterior a competência informada
         * @param dataDocumento
         * @param competencia
         * @return
         */
        public static boolean isAntesCompetencia(Date dataDocumento, Integer competencia) {
            Date dataDoc = DateUtils.truncate(dataDocumento, Calendar.DAY_OF_MONTH);
            Calendar compet = toCalendar(competencia);
            return dataDoc.before(compet.getTime());
        }

        /**
         * Verifica se a dataDocumento é posterior a competência informada
         * @param dataDocumento
         * @param competencia
         * @return
         */
        public static boolean isDepoisCompetencia(Date dataDocumento,
                                                  Integer competencia) {
            Date dataDoc = DateUtils.setDays(dataDocumento,1);
            dataDoc = DateUtils.truncate(dataDoc, Calendar.DAY_OF_MONTH);
            Calendar compet = toCalendar(competencia);
            //compet.set(Calendar.MONTH, Calendar.JANUARY);

            return isData1MaiorIgualData2(dataDoc, compet.getTime() );
//		compet.add(Calendar.DAY_OF_MONTH, -1); //último dia do mês da competência
//		return dataDoc.after(compet.getTime());
        }

        /**
         * Cria competência no formato AAAAMM
         * @param ano
         * @param mes
         * @return
         */
        public static Integer toCompetencia(String ano, String mes) {
            return Integer.valueOf((mes.length() == 1 ? ano + "0" + mes : ano + mes));
        }

        /**
         * Cria competência no formato AAAAMM
         * @param cal
         * @return
         */
        public static Integer toCompetencia(Calendar cal) {
            return Integer.valueOf(getYear(cal) * 100 + getMonth(cal));
        }

        /**
         * Retorna valor com scale = 2 e RoundingMode.FLOOR
         * @param valor
         * @return
         */
        public static BigDecimal scale2(BigDecimal valor) {
            if (null != valor)
                return valor.setScale(2, RoundingMode.FLOOR);
            else
                return valor;
        }

        /**
         * Retorna valor com scale = 4 e RoundingMode.FLOOR
         * @param valor
         * @return
         */
        public static BigDecimal scale4(BigDecimal valor) {
            if (null != valor)
                return valor.setScale(4, RoundingMode.FLOOR);
            else
                return valor;
        }

        /**
         * @param calendarOriginal
         * @return Calendar com o primeiro dia do mês para a data informada
         */
        public static Calendar primeiroDiaMes(Calendar calendarOriginal) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarOriginal.getTime());
            calendar.setLenient(false);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar;
        }

        public static Integer dataToInteger(Date data ){
            if( null == data ) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return Integer.valueOf( sdf.format( data ) );
        }

        /**
         * @param ano
         * @param mes
         * @return Data com primeiro dia do mês para ano/mês informado
         */
        public static Date primeiroDiaMes(String ano, String mes) {
            int anoI = Integer.parseInt(ano);
            int mesI = Integer.parseInt(mes);
            return primeiroDiaMes(anoI, mesI);
        }

        /**
         * @param ano
         * @param mes
         * @return Data com primeiro dia do mês para ano/mês informado
         */
        public static Date primeiroDiaMes(int ano, int mes) {
            Calendar cal = DateUtils.truncate(Calendar.getInstance(), Calendar.DAY_OF_MONTH );
            cal.setLenient(false);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, mes - 1);
            cal.set(Calendar.YEAR, ano);
            return DateUtils.truncate( cal, Calendar.DAY_OF_MONTH).getTime();
        }

        /**
         * @param calendarOriginal
         * @return Calendar com o último dia do mês para a data informada
         */
        public static Calendar ultimoDiaMes(Calendar calendarOriginal){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarOriginal.getTime());
            calendar.setLenient(false);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return calendar;
        }


        /**
         * @param calendarOriginal data que sera analisada
         * @return Calendar com o primeiro dia do ano para a data informada
         */
        public static Calendar primeiroDiaAno(Calendar calendarOriginal) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarOriginal.getTime());
            calendar.setLenient(false);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            return calendar;
        }

        /**
         * @param calendarOriginal data que sera analisada
         * @return Caledar com o último dia do ano para a data informada
         */
        public static Calendar ultimoDiaAno(Calendar calendarOriginal) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarOriginal.getTime());
            calendar.setLenient(false);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            return calendar;
        }



        /**
         * @param
         * @return Caledar com o último dia do ano para a data informada
         */
        public static Calendar ultimoDiaUtilDoAno() {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTime(calendarOriginal.getTime());
            calendar.setLenient(false);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek == Calendar.SATURDAY) {
                calendar.add(Calendar.DATE, -1);
            } else if (dayOfWeek == Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, -2);
            }

            return calendar;
        }

        /**
         * @param calendarOriginal
         * @return Calendar com o último dia do ano anterior
         */
        public static Calendar ultimoDiaAnoAnterior(Calendar calendarOriginal) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarOriginal.getTime());
            calendar = ultimoDiaAno(calendar);
            calendar.add(Calendar.YEAR, -1);
            return calendar;
        }

        /** transforma string AAAAMMDD em Date
         * @param aaaammdd
         * @return Data a partir do string informado
         */
        public static Date toDate(String aaaammdd) {
            if ("0".equals(aaaammdd) ||
                    StringUtils.isEmpty(aaaammdd)){
                return null;
            }

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setLenient(false);

            int dia = Integer.parseInt( StringUtils.substring(aaaammdd, 6, 8 ) );
            int mes = Integer.parseInt( StringUtils.substring(aaaammdd, 4, 6 ) );
            int ano = Integer.parseInt( StringUtils.substring(aaaammdd, 0, 4 ) );

            cal.set(Calendar.DAY_OF_MONTH, dia);
            cal.set(Calendar.MONTH, mes - 1);
            cal.set(Calendar.YEAR, ano);
            return cal.getTime();
        }

        /**
         * @param calendar
         * @return int com o ano do Calendar passado
         */
        public static int getYear(Calendar calendar) {
            return calendar.get(Calendar.YEAR);
        }


        /**
         * @param calendar
         * @return int com o mês do Calendar passado
         */
        public static int getMonth(Calendar calendar) {
            return calendar.get(Calendar.MONTH) + 1;
        }

        /**
         * @param calendar
         * @return int com o dia do Calendar passado
         */
        public static int getDay(Calendar calendar) {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * @param calendar
         * @return true caso seja final de semana (Sábado ou Domingo)
         */
        public static boolean isFinalDeSemana(Calendar calendar) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
        }

        /**
         * @param data
         * @return true caso seja final de semana (Sábado ou Domingo)
         */
        public static boolean isFinalDeSemana(Date data) {
            Calendar dataCal =Calendar.getInstance();
            dataCal.setTime(data);
            return isFinalDeSemana( dataCal );
        }

        /**
         * @param calendar
         * @return Long no formato AAAAMMDD
         */
        public static Long toLong(Calendar calendar) {
            String ano = "" + getYear(calendar);
            String mes = "" + getMonth(calendar);
            if (mes.length() == 1) {
                mes = "0" + mes;
            }
            String dia = "" + getDay(calendar);
            if (dia.length() == 1){
                dia = "0" + dia;
            }

            return Long.valueOf(ano + mes + dia);
        }

        /**
         * Valida uma data informada no formato DDMMAAAA
         * @param DDMMAAAA
         * @return
         */
        public static boolean ValidateDMA(String DDMMAAAA) {
            Calendar data = Calendar.getInstance();
            int dia = Integer.parseInt(DDMMAAAA.substring(0, 2));
            int mes = Integer.parseInt(DDMMAAAA.substring(2, 4))-1;
            int ano = Integer.parseInt(DDMMAAAA.substring(4));
            data.set(ano, mes, dia);
            return data.get(Calendar.DAY_OF_MONTH)==dia && data.get(Calendar.MONTH)== mes;
        }

        /**
         * Valida uma data informada no formato AAAAMMDD
         * @param AAAAMMDD
         * @return
         */
        public static boolean ValidateAMD(String AAAAMMDD) {
            Calendar data = Calendar.getInstance();
            int dia = Integer.parseInt(AAAAMMDD.substring(6));
            int mes = Integer.parseInt(AAAAMMDD.substring(4, 6))-1;
            int ano = Integer.parseInt(AAAAMMDD.substring(0, 4));
            data.set(ano, mes, dia);
            return data.get(Calendar.DAY_OF_MONTH)==dia && data.get(Calendar.MONTH)== mes;
        }

        /**
         * Verifica se a Data1 é posterior a Data2
         * @param data1 Data Inicial
         * @param data2 Data Final
         * @return
         */
        public static boolean isData1MaiorData2(Date data1, Date data2) {
            if (data1 == null || data2 == null) {
                return false;
            }
            Calendar cal1 = Calendar.getInstance();
            cal1.clear();
            cal1.setTime(data1);
            Calendar cal2 = Calendar.getInstance();
            cal2.clear();
            cal2.setTime(data2);
            return cal1.after(cal2);
        }

        /**
         * Verifica se a Data1 é maior ou igual a Data2
         * @param data1 Data Inicial
         * @param data2 Data Final
         * @return
         */
        public static boolean isData1MaiorIgualData2(Date data1, Date data2) {
            if (data1 == null || data2 == null) {
                return false;
            }
            Calendar cal1 = Calendar.getInstance();
            cal1.clear();
            cal1.setTime(data1);

            Calendar cal2 = Calendar.getInstance();
            cal2.clear();
            cal2.setTime(data2);

            return (toLong( cal1 ).compareTo(  toLong( cal2 ) ) > 0);
        }

        /**
         * @param data
         * @return int com o mês do Date passado
         */
        public static int getMonth(Date data) {
            Calendar dataCal =Calendar.getInstance();
            dataCal.setTime(data);
            return dataCal.get(Calendar.MONTH)+1;
        }

        /**
         * @param data
         * @return int com o dia do Date passado
         */
        public static int getDay(Date data) {
            Calendar dataCal = Calendar.getInstance();
            dataCal.setTime(data);
            return dataCal.get(Calendar.DAY_OF_MONTH);
        }
        /**
         * @param data
         * @return int com o ano do Date passado
         */
        public static int getYear(Date data) {
            Calendar dataCal = Calendar.getInstance();
            dataCal.setTime(data);
            return dataCal.get(Calendar.YEAR);
        }

        /**
         * Verifica se a competencia informada é válida para processamento
         * 	O formato deve ser YYYYMM
         * 	YYYY deve estar entre 2004 e o ano atual
         * 	não pode ser menor que 200407
         * 	MM deve ser entre 01 e 12
         * @param competencia
         * @return
         */
        public static boolean isCompetenciaValidaParaProcessamento(Integer competencia) {
            if (competencia < 200407) {
                return false;
            }
            String cpt = competencia.toString();
            int ano = Integer.parseInt(StringUtils.left(cpt, 4));
            int mes = Integer.parseInt(StringUtils.right(cpt, 2));

            int anoAtual = Calendar.getInstance().get(Calendar.YEAR);

            if (ano > anoAtual) {
                return false;
            }

            return !(mes < 1 || mes > 12);
        }


        public static boolean isUltimoDiaMes(Date data) {
            boolean isUltimo;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);
            //pega ultimo dia do mes
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            isUltimo = calendar.get(Calendar.DAY_OF_MONTH) == maxDay;
            return isUltimo;
        }




        public static String gerarMD5 (String str) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                BigInteger hash;
                hash = new BigInteger(1, md.digest(str.getBytes("UTF-8")));
                String crypto = hash.toString(16);
                if (crypto.length() %2 != 0)
                    crypto = "0" + crypto;
                crypto = crypto.toUpperCase();
                String str2 = " ";
                for (int i = 0; i < crypto.length(); i++) {
                    str2 = str2 + crypto.charAt(i);
                    if (i%2!=0){
                        str2 = str2 + " ";
                    }
                }
                return str2;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

	public static String typeFlag(Integer id) {
		String path = "";
		try {

			switch (id) {
				case 1:
					path = "images/book.png";
					break;
				case 2:
					path = "images/info.png";
					break;
				case 3:
					path = "images/refresh.png";
					break;
				default:
					path = "images/ref_sign_question.png";
					break;
			}
			return path;
		} catch (Exception e ) {
			return "images/ref_sign_question.png";
		}
	}

	public static String flagPendencia(Integer id) {
		String path = "";
		try {

			switch (id) {
				case 205:
				case 211:
					path = "images/flag-red.png";
					break;
				case 206:
				case 208:
					path = "images/flag-yellow.png";
					break;
				case 209:
					path = "images/flag-green.png";
					break;
				case 210:
					path = "images/flag-blue.png";
					break;
				case 207:
				case 212:
					path = "images/flag-black.png";
					break;
				default:
					path = "images/flag-black.png";
					break;
			}
			return path;
		} catch (Exception e ) {
			return "images/flag-green.png";
		}
	}

	public static String pathFlag(Integer id) {
		String path = "";
		try {

			switch (id) {
				case 1:
					path = "images/cabinet.png";
					break;
				case 2:
					path = "images/question.png";
					break;
				case 3:
					path = "images/ref_sign_sync.png";
					break;
				case 4:
					path = "images/ref_dell_keyboard.png";
					break;
				case 5:
					path = "images/windows_blue_screen_3.png";
					break;
				case 6:
					path = "images/ref_client.png";
					break;
				case 8:
					path = "images/ref_sign_check.png";
					break;
				case 9:
					path = "images/ref_sign_error.png";
					break;
				case 28:
					path = "images/ref_sign_sync.png";
					break;
				case 35:
					path = "images/green-ok.png";
					break;
				case 36:
					path = "images/windows_blue_screen_1.jpg";
					break;
				case 37:
					path = "images/red-cross.png";
					break;
				case 38:
					path = "images/blueflag.png";
					break;
				case 39:
					path = "images/ref_sign_ban.png";
					break;
				case 138:
					path = "images/status.png";
					break;
				case 139:
					path = "images/ref_sign_info.png";
					break;
				default:
					path = "images/ref_sign_question.png";
					break;
			}
			return path;
		} catch (Exception e ) {
			return "images/reference.png";
		}
	}

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        return encryptedValue;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

	public static String imageName(String filename, boolean encode) {
        try {
            if (encode) {
                return HexStringConverter.getHexStringConverterInstance().stringToHex(encrypt(filename));
            } else {
                return decrypt(HexStringConverter.getHexStringConverterInstance().hexToString(filename));
            }
        } catch (Exception e) {

        }
        return null;
    }
	
	public static String pdfName(String filename, boolean encode){
		String bk = filename;
        try {
            if (encode) {
                return HexStringConverter.getHexStringConverterInstance().stringToHex(encrypt(filename));
            } else {
                return decrypt(HexStringConverter.getHexStringConverterInstance().hexToString(filename));
            }
        } catch (Exception e) {

        }
        return null;
	}

    public static String Ambiente() {
		try {
			ClassLoader classLoader = AppUtils.class.getClassLoader();
			input = new FileInputStream(classLoader.getResource("config/admin-config.properties").getFile());
			prop.load(input);
			// get the property value and print it out
			ambiente = prop.getProperty("geral.ambiente").toUpperCase();

		} catch (IOException e) {
			e.printStackTrace();
			ambiente = "PRODUCAO";
		}

		return ambiente;

	}

    public static String readConfig(String properties) {

        return prop.getProperty(properties);

    }

	public static void checkValues(Object object){
		Class<?> classe = object.getClass();
		Field[] campos = classe.getDeclaredFields();

		String nomeAtributo = "";
		Object valorAtributo = null;
		for (Field campo : campos) {
			try {
				Class<?> clazz = campo.getType();
				campo.setAccessible(true); //Necessário por conta do encapsulamento das variáveis (private)
				valorAtributo = campo.get(object);

                if(clazz.equals(Integer.class) ||  clazz.getName().equals("int")) {
                    if(valorAtributo != null ) {
                        String str = Integer.toString((int) valorAtributo);
                        campo.set(object, Integer.valueOf(str.replaceAll("[^\\d]", "")));
                    }
                }

                if(clazz.equals(Long.class) ||  clazz.getName().equals("long")) {
                    if(valorAtributo != null ) {
                        String str = (String) valorAtributo;
                        campo.set(object, Long.valueOf(str.replaceAll("[ˆ\\d]", "")));
                    }
                }

				if(clazz.equals(String.class) ||  clazz.getName().equals("String")) {
				    if(valorAtributo != null ) {
                        String str = (String) valorAtributo;
                        campo.set(object, str.replaceAll("[^\\dA-Za-z ]", ""));
                    }
                }

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static String formatJSON(String texto) {

		try {

			String file = UUID.randomUUID().toString();

			String retorno = "";
			String s;

			PrintWriter writer = new PrintWriter("/tmp/"+ file , "UTF-8");

			writer.println(texto);

			writer.close();

			String[] cmd = {
					"/bin/bash",
					"-c",
					"python /opt/gcadastro/jsonhtml.py /tmp/" + file
			};
			Process p =  Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			while ((s = stdInput.readLine()) != null) {
				retorno += s;
			}

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			while ((s = stdError.readLine()) != null) {
				retorno += s;
			}

			return retorno;


		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Sem resposta";

	}



    public static void main(String[] args) {


	}



}
