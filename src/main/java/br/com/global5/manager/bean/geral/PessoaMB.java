package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.ViaCEPClient;
import br.com.global5.infra.util.ViaCEPEndereco;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.manager.model.cadastro.PessoaQry;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.model.ect.Logradouro;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.service.cadastro.PessoaService;
import br.com.global5.manager.service.ect.LogradouroService;
import br.com.global5.manager.service.enums.DocumentoTipoService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class PessoaMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Pessoa> pesList;
	private List<Pessoa> filteredValue;
	private Integer id;
	private Pessoa pessoa;
	private Filter<Pessoa> filter = new Filter<Pessoa>(new Pessoa());
	private List<AreaFuncao> lstFuncao;
	private List<DocumentoTipo> lstDocTipo;
	private String documento;
	private String nome;
	private boolean mostrarExcluidos = false;

	private Date dtInicial;
	private Date dtFinal;

	// pesquisa Pessoa
	private String nomePesq;
	private List<PessoaQry> lstPessoaQry;
	
	@Inject
	PessoaService pesService;

	@Inject
	LogradouroService logrService;

	@Inject
	DocumentoTipoService docTipoService;

	@Inject
	UsuarioService usuService;
	
	@PostConstruct
	public void init() {

		clear();
		lstDocTipo = docTipoService.crud().isNull("exclusao").listAll();
		

	}

	public void buscaCep(ValueChangeEvent e) {
        ViaCEPEndereco endereco = new ViaCEPEndereco();
        ViaCEPClient client = new ViaCEPClient();
        Localizador loc = new Localizador();
		Logradouro logr = logrService.getCep(e.getNewValue().toString());
        if( logr == null ) {
            try {
                endereco = client.getEndereco(e.getNewValue().toString());
                loc.setBairro(endereco.getBairro().toUpperCase());
                loc.setCidade(endereco.getLocalidade().toUpperCase());
                loc.setUf(endereco.getUf().toUpperCase());
                loc.setLogradouro(endereco.getLogradouro().toUpperCase());
                loc.setPais("BRASIL");
            } catch (IOException exception) {}

        } else {
            Hibernate.initialize(logr.getCidade());
            Hibernate.initialize(logr.getCidade().getUf());
            Hibernate.initialize(logr.getCidade().getPais());
            loc.setLogradouro((logr.getTipo().toUpperCase() + " " + logr.getNome()).toUpperCase());
            loc.setBairro(logr.getIniBairro().getNome().toUpperCase());
            loc.setCidade(logr.getCidade().getNome().toUpperCase());
            loc.setUf(logr.getCidade().getUf().getSigla().toUpperCase());
            loc.setPais(logr.getCidade().getPais().getNome().toUpperCase());
        }
        if(loc != null) {
            pessoa.setEndereco(loc);
        }

	}

	public LazyDataModel<Pessoa> getPesList() {
		if( pesList == null ) {
			pesList = new LazyDataModel<Pessoa>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Pessoa> load(int first, int pageSize,
											  String sortField, SortOrder sortOrder,
											  Map<String, Object> filters) {

					br.com.global5.infra.enumerator.SortOrder order = null;
					if( sortOrder != null ) {
						order = sortOrder.equals(SortOrder.ASCENDING) ? br.com.global5.infra.enumerator.SortOrder.ASCENDING
								: sortOrder.equals(SortOrder.DESCENDING) ? br.com.global5.infra.enumerator.SortOrder.DESCENDING
								: br.com.global5.infra.enumerator.SortOrder.UNSORTED;
					}
					filter.setFirst(first).setPageSize(pageSize)
							.setSortField(sortField).setSortOrder(order)
							.setParams(filters);
					List<Pessoa> list = pesService.paginate(filter);
					setRowCount(pesService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Pessoa getRowData(String key) {
					return pesService.findById(new Integer(key));
				}
			};

		}
		return pesList;
	}

	public void remove() {
		if( pessoa != null && pessoa.getId() != null) {
			pesService.remove(pessoa);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pessoa " + pessoa.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if( pessoa.getEndereco().getLogradouro() == null ||
                pessoa.getEndereco().getBairro() == null ||
                pessoa.getEndereco().getCidade() == null ||
                pessoa.getEndereco().getUf()     == null ) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro no Endereço","O endereço deve ser preenchido completamente!"));

        }

        if( pessoa.getTipoDoc1() == null ) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Tipo de Documento","O tipo de documento deve ser preenchido!"));
        }

        if( pessoa.getTipoDoc1() != null && pessoa.getTipoDoc2() != null && pessoa.getTipoDoc1() == pessoa.getTipoDoc2()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Tipo de Documento","O tipo de documento não pode ser igual!"));
        }


		if (pessoa.getId() == null) {
			pesService.insert(pessoa);
			msg = "acCadastro da Pessoa " + pessoa.getNome() + " criado com sucesso!";
		} else {
			pesService.update(pessoa);
			msg = "acCadastro da Pessoa " + pessoa.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/pessoaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir pessoas " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		checkUsuario.valid();
		Calendar now = Calendar.getInstance();

		now.add(Calendar.DATE, -1);
		pessoa = new Pessoa();
		filter = new Filter<Pessoa>(new Pessoa());

		dtInicial = now.getTime();

		now.add(Calendar.DATE, 2);
		dtFinal = now.getTime();

		pessoa.setEndereco(new Localizador());
		pessoa.setFuncao(new AreaFuncao());

		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Pessoa é obrigatório");
		}
		pessoa = pesService.crud().get(id);
		if (pessoa == null) {
			throw new BusinessException("Pessoa não foi encontrado pelo id: " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = pesService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Pessoa) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/pessoaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pessoa " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}

	public void btnPesquisar() {

		filter.getEntity().setId(id);
		filter.getEntity().setDocumento1(documento);
		filter.getEntity().setNome(nome);
		filter.getEntity().setDtInicial(dtInicial);
		filter.getEntity().setDtFinal(dtFinal);
		filter.getEntity().setMostrarExcluidos(mostrarExcluidos);
		RequestContext.getCurrentInstance().execute("PF('pesTable').filter()");

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/pessoalst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do cadastro de pessoas " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}
	
	public void btnPespCliente(){
		boolean temCriteriosPesq;
		try{
			EntityManager em = pesService.crud().getEntityManager();
			
			String parametrosSql = " ";
			
			try{
				// Nome do Cliente a ser pesquisado
				if( nomePesq == ""){
					temCriteriosPesq = false;
				} else {
					parametrosSql += " AND pes_nome ~* '" + nomePesq + "' AND ";
					temCriteriosPesq = true;
				}
			} catch ( Exception e) {
				e.printStackTrace();
				temCriteriosPesq = false;
			}
		
			parametrosSql += " 0 = 0 " ;
			
			EntityManager emPes = usuService.crud().getEntityManager();
			
			/*"  where pesoid in (select area_pesoid_sacado from area where area_anvloid = 2 )  and   pes_pessoa_fisica = false "*/			
			
			
			String query = 	" select  pesoid as idcliente, " +
					" pes_nome as razao, " +
					" pes_nome_fantasia as fantasia, " +
					" pes_documento1 as cnpj, " +
					" pes_email as email, " +
					" (select a.afun_areaoid from area_funcao a where a.afunoid = pes_afunoid) as idArea ," +
					" (select (t.telddd || '-' || SUBSTRING(t.telfone,1 , 4 ) || '-' || SUBSTRING(t.telfone,5, 10 ) ) as numTel from telefone t where t.teloid = pes_teloid) as fone " +
					" from pessoa  " +
					"  where pesoid in ( select area_pesoid_sacado from area )  and   pes_pessoa_fisica = false "
					+ parametrosSql ;
		
		lstPessoaQry =  emPes.createNativeQuery(query,"ListPessoaClienteMapping").getResultList();
		
		Integer registrosPessoa = lstPessoaQry.size();
		
		if(registrosPessoa == 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Nenhum registro encontrado." ));
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formPesqCliente:tablePesqCli");
		}
		try {
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formPesqCliente:tablePesqCli");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			temCriteriosPesq = false;
		}
		
	}	

	public void onRowUnselect(UnselectEvent event) {
		pessoa = new Pessoa();
	}

	public Filter<Pessoa> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Pessoa> filter) {
		this.filter = filter;
	}

	public List<Pessoa> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Pessoa> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setPesList(LazyDataModel<Pessoa> pesList) {
		this.pesList = pesList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<AreaFuncao> getLstFuncao() {
        return lstFuncao;
    }

    public void setLstFuncao(List<AreaFuncao> lstFuncao) {
        this.lstFuncao = lstFuncao;
    }

    public PessoaService getPesService() {
        return pesService;
    }

    public void setPesService(PessoaService pesService) {
        this.pesService = pesService;
    }

    public LogradouroService getLogrService() {
        return logrService;
    }

    public void setLogrService(LogradouroService logrService) {
        this.logrService = logrService;
    }

	public List<DocumentoTipo> getLstDocTipo() {
		return lstDocTipo;
	}

	public void setLstDocTipo(List<DocumentoTipo> lstDocTipo) {
		this.lstDocTipo = lstDocTipo;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

    public boolean isMostrarExcluidos() {
        return mostrarExcluidos;
    }

    public void setMostrarExcluidos(boolean mostrarExcluidos) {
        this.mostrarExcluidos = mostrarExcluidos;
    }

	public String getNomePesq() {
		return nomePesq;
	}

	public void setNomePesq(String nomePesq) {
		this.nomePesq = nomePesq;
	}

	public List<PessoaQry> getLstPessoaQry() {
		return lstPessoaQry;
	}

	public void setLstPessoaQry(List<PessoaQry> lstPessoaQry) {
		this.lstPessoaQry = lstPessoaQry;
	}
    
    
}
