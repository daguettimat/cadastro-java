package br.com.global5.rastreamento.model.bi;

public class CheckBI {
	
	public static CheckBI instancia;
	
	protected CheckBI(){
		
	}

	public static CheckBI getInstancia() {
		
		if ( instancia == null)
			instancia = new CheckBI();
		
		return instancia;
	}

	protected Integer idBoletimAnterior;
	protected Integer idEvento;
	protected Character tipoEventoBoletim;
	protected Character tipoUltimoBoletimClone;
	protected boolean 	tipoBoletimFinal;
	
	public void recebeIdEv(Integer idEventoRecebido){
	
		idEvento 		  = idEventoRecebido;

	}
	
	public void recebeIdBI(Integer idBiRecebido){
		
		idBoletimAnterior = idBiRecebido;

	}
	
	public void recebeCharTpBoletim(char tipoBoletimEscolhido){
		tipoEventoBoletim = tipoBoletimEscolhido;
	}
	
	public void recebeBoletimFinal(boolean boletimFinal){
		tipoBoletimFinal = boletimFinal;
	}
	
	public void recebeUltimoTipoBoletimClone(char ultimoTipoBoletimClone) {
		tipoUltimoBoletimClone = ultimoTipoBoletimClone;		
	}
	
	public Integer checkBIRetorno(){
		
		return idBoletimAnterior;
	}
	
	public Integer checkEvRetorno(){
		return idEvento;
	}
	
	public Character tipoEventoBiRetorno(){
		return tipoEventoBoletim;
	}
	
	public boolean tipoBoletimFinalRetorno(){
		return tipoBoletimFinal;
	}
	
	public Character tipoUltimoBoletimCloneRetorno(){
		return tipoUltimoBoletimCloneRetorno();
	}


	
}
