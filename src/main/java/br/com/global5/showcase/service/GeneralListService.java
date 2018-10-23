package br.com.global5.showcase.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import br.com.global5.showcase.model.Parentesco;
import br.com.global5.showcase.model.TipoContato;

public class GeneralListService implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5113763563198718197L;
	private List<Parentesco> parentes;
	private List<TipoContato> tipo;
    
	@PostConstruct
    public void init() {
		
		//
		// Grau Parentesco
		//
		
        parentes = new ArrayList<Parentesco>();
        parentes.add(new Parentesco(1,"Amigo/Amiga"));
        parentes.add(new Parentesco(2,"Avô/Avó"));
        parentes.add(new Parentesco(3,"Esposo/Esposa"));
        parentes.add(new Parentesco(4,"Cunhado/Cunhada"));
        parentes.add(new Parentesco(5,"Filho/Filha"));
        parentes.add(new Parentesco(6,"Genro/Nora"));
        parentes.add(new Parentesco(7,"Irmão/Irmã"));
        parentes.add(new Parentesco(8,"Pai/Mãe"));
        parentes.add(new Parentesco(9,"Sobrinho/Sobrinha"));
        parentes.add(new Parentesco(10,"Sogro/Sogra"));
        parentes.add(new Parentesco(11,"Tio/Tia"));
        parentes.add(new Parentesco(12,"Vizinho/Vizinha"));
        
        //
        // Tipo Contato
        //
        
        tipo = new ArrayList<TipoContato>();
        tipo.add(new TipoContato(1, "Administração"));
        tipo.add(new TipoContato(2, "Outros"));
        tipo.add(new TipoContato(3, "Portaria"));
        tipo.add(new TipoContato(4, "Proprietário"));
        tipo.add(new TipoContato(5, "Telefonista"));
        
        
    }

	public List<Parentesco> getParentes() {
		return parentes;
	}

	public void setParentes(List<Parentesco> parentes) {
		this.parentes = parentes;
	}

	public List<TipoContato> getTipo() {
		return tipo;
	}

	public void setTipo(List<TipoContato> tipo) {
		this.tipo = tipo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

 
    
}
