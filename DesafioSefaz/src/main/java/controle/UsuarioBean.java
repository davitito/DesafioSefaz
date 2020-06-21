package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import entidade.Usuario;

import dao.TelefoneDAO;
import dao.TelefoneDAOImpl;
import entidade.Telefone;

@ManagedBean(name = "UsuarioBean")
@SessionScoped
public class UsuarioBean {
	
	private String txtNome;	
	private String txtEmail;
	private String txtSenha;	
	
	private Long txtTelDDD;
	private String txtTelNum;
	private String txtTelTipo;

	private Boolean emailValido;
	
	private Usuario usuarioLogado;

	private List<Usuario> listaUsuarios;
	private Usuario usuario;
	private UsuarioDAO usuarioDAO;

	private List<Telefone> listaTelefones;
	private Telefone telefone;
	private TelefoneDAO telefoneDAO;
	
	private List<Telefone> listaTelefonesAdicionais;
	
	private List<Telefone> listaTelefonesUsu = new ArrayList<Telefone>();

	
	public UsuarioBean() {
		this.listaUsuarios = new ArrayList<Usuario>();
		this.usuario = new Usuario();
		this.usuarioDAO = new UsuarioDAOImpl();
		
		this.listaTelefones = new ArrayList<Telefone>();
		this.telefone = new Telefone();
		this.telefoneDAO = new TelefoneDAOImpl();

		consultaTodos();
		
		this.atualizarUsuarioLogado();
		
		this.listaTelefonesAdicionais = new ArrayList<Telefone>();
	}
	
	public void atualizarUsuarioLogado() {
		HttpSession sessao =  (HttpSession)FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		this.usuarioLogado = (Usuario)sessao.getAttribute("usuarioLogado");
	}

	public static boolean validarEmail(String email) {
		boolean emailValido = false;
			if (email != null && email.length() > 0) {
		        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		        Matcher matcher = pattern.matcher(email);
		    	if (matcher.matches()) {
		    		emailValido = true;
		    	}
			}
		return emailValido;
	}
	
	public void incluirUsuario() throws IOException {
		emailValido = validarEmail(this.txtEmail);
		if (emailValido) {
			Usuario novo = new Usuario();
			novo.setNome(this.txtNome);
			novo.setEmail(this.txtEmail);
			novo.setSenha(this.txtSenha);
			boolean achou = false;
			this.listaUsuarios = this.usuarioDAO.consultarTodos();
			for (Usuario usuarioPesquisa : listaUsuarios) {
				if (usuarioPesquisa.getEmail().equals(this.usuario.getEmail())) {
					achou = true;
				}
			}
			if(achou) {
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Usuário já cadastrado, tente outro!!!"));
			}else {				
				this.usuarioDAO.incluir(novo);
				cadastrarTelefone();
				this.usuario = new Usuario();
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
			}
		}else {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Digite um e-mail válido!!!"));
		}
	}
	
	public void cadastrarTelefone() {
		Telefone novoTel = new Telefone();
		novoTel.setDdd(this.txtTelDDD);
		novoTel.setNumero(this.txtTelNum);
		novoTel.setTipo(this.txtTelTipo);
		novoTel.setEmail_usu(this.txtEmail);
		listaTelefonesAdicionais.add(novoTel);
		for (Telefone adicionaTel : listaTelefonesAdicionais) { 
		    this.telefoneDAO.incluir(adicionaTel);
	    }
		this.telefone = new Telefone();
	}
	
	public void adicionarTelefone() {
		Telefone addTel = new Telefone();
		addTel.setDdd(this.txtTelDDD);
		addTel.setNumero(this.txtTelNum);
		addTel.setTipo(this.txtTelTipo);
		addTel.setEmail_usu(this.txtEmail);
		listaTelefonesAdicionais.add(addTel);
		this.txtTelDDD = null;
		this.txtTelNum = null;
		this.txtTelTipo = null;
	}
	
	/**
	 * A função mostrar dados retornar os campos que poderão ser modificados.
	 */
	public void mostrarDados() {
		Usuario usua = new Usuario();
		Telefone tele = new Telefone();
		boolean achou = false;
		this.listaUsuarios = this.usuarioDAO.consultarTodos();
		this.listaTelefones = this.telefoneDAO.consultarTodos();
		for (Usuario usuarioPesquisa : listaUsuarios) {
			if (usuarioPesquisa.getEmail().equals(this.txtEmail)) {
				usua = usuarioPesquisa;
				achou = true;
			}
		}
		if(achou) {
			usua = this.usuarioDAO.pesquisar(this.txtEmail);
			tele = this.telefoneDAO.pesquisar(this.txtEmail);
			this.txtEmail = usua.getEmail();
			this.txtNome = usua.getNome();
			
			this.txtTelDDD = tele.getDdd();
			this.txtTelNum = tele.getNumero();
			this.txtTelTipo = tele.getTipo();
			
			listaTelefonesUsu = new ArrayList<Telefone>();
			for (Telefone telefonePesquisa : listaTelefones) {
				if (telefonePesquisa.getEmail_usu().equals(this.txtEmail)) {
					listaTelefonesUsu.add(telefonePesquisa);
				}
			}
		}else {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Usuário não cadastrado!!!"));
		}
	}

	/**
	 * As funções voltar/voltarInicio retornam para a página de funcionalidades/login e e zera os campos.
	 */	
	public void voltar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("funcionalidades.xhtml");
		zerarCampos();
	}
	
	public void voltarInicio() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		zerarCampos();
	}
	
	public void zerarCampos() {
		txtNome = "";	
		txtEmail = "";
		txtSenha = "";	
		txtTelDDD = null;
		txtTelNum = "";
		txtTelTipo = "";
		listaTelefonesUsu = null;
	}
	
	public void removerUsuario() throws IOException {
		Usuario u = new Usuario();
		Telefone t = new Telefone();
		boolean achou = true;
		u.setEmail(this.txtEmail);
		t.setEmail_usu(this.txtEmail);
		this.listaUsuarios = this.usuarioDAO.consultarTodos();
		for (Usuario usuarioPesquisa : listaUsuarios) {
			if (usuarioPesquisa.getEmail().equals(this.txtEmail)) {
				achou = true;
			}
		}
		if(achou) {
			this.telefoneDAO.remover(t);
			this.telefone = new Telefone();
			this.usuarioDAO.remover(u);
			this.usuario = new Usuario();
			zerarCampos();
			FacesContext.getCurrentInstance().getExternalContext().redirect("funcionalidades.xhtml");
		}else {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Usuário não encontrado!!!"));
		}
	}
	
	/**
	 * A função alterarUsuario pesquisa o usuário e telefone pelo e-mail (chave primária e estrangeira) e retorna os dados para serem alterados
	 */	
	public void alterarUsuario() throws IOException {
		Usuario usu = new Usuario();
		Telefone tel = new Telefone();
		this.listaUsuarios = this.usuarioDAO.consultarTodos();
		this.listaTelefones = this.telefoneDAO.consultarTodos();
		for (Usuario usuarioPesquisa : listaUsuarios) {
			if (usuarioPesquisa.getEmail().equals(this.txtEmail)) {
				usu = usuarioPesquisa;
				usu.setNome(this.txtNome);
				this.usuarioDAO.alterar(usu);
				this.usuario = new Usuario();
			}			
		}
		for (Telefone telefonePesquisa : listaTelefonesUsu) {
			if (telefonePesquisa.getEmail_usu().equals(this.txtEmail)) {
				tel = telefonePesquisa;
				tel.setDdd(telefonePesquisa.getDdd());
				tel.setNumero(telefonePesquisa.getNumero());
				tel.setTipo(telefonePesquisa.getTipo());
				this.telefoneDAO.alterar(tel);
				this.telefone = new Telefone();
			}
		}
		listaTelefones = this.telefoneDAO.consultarTodos();
		zerarCampos();
		FacesContext.getCurrentInstance().getExternalContext().redirect("funcionalidades.xhtml");
	}
	
	/**
	 * A função consultaTodos é chamada logo no início para preeencher a lista de usuários e seus telefones
	 */
	public void consultaTodos() {
		listaUsuarios = this.usuarioDAO.consultarTodos();
		listaTelefones = this.telefoneDAO.consultarTodos();
	}

	public String getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(String txtNome) {
		this.txtNome = txtNome;
	}

	public String getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(String txtEmail) {
		this.txtEmail = txtEmail;
	}

	public String getTxtSenha() {
		return txtSenha;
	}

	public void setTxtSenha(String txtSenha) {
		this.txtSenha = txtSenha;
	}

	public Long getTxtTelDDD() {
		return txtTelDDD;
	}

	public void setTxtTelDDD(Long txtTelDDD) {
		this.txtTelDDD = txtTelDDD;
	}

	public String getTxtTelNum() {
		return txtTelNum;
	}

	public void setTxtTelNum(String txtTelNum) {
		this.txtTelNum = txtTelNum;
	}

	public String getTxtTelTipo() {
		return txtTelTipo;
	}

	public void setTxtTelTipo(String txtTelTipo) {
		this.txtTelTipo = txtTelTipo;
	}

	public Boolean getEmailValido() {
		return emailValido;
	}

	public void setEmailValido(Boolean emailValido) {
		this.emailValido = emailValido;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Telefone> getListaTelefones() {
		return listaTelefones;
	}

	public void setListaTelefones(List<Telefone> listaTelefones) {
		this.listaTelefones = listaTelefones;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Telefone> getListaTelefonesAdicionais() {
		return listaTelefonesAdicionais;
	}

	public void setListaTelefonesAdicionais(List<Telefone> listaTelefonesAdicionais) {
		this.listaTelefonesAdicionais = listaTelefonesAdicionais;
	}

	public List<Telefone> getListaTelefonesUsu() {
		return listaTelefonesUsu;
	}

	public void setListaTelefonesUsu(List<Telefone> listaTelefonesUsu) {
		this.listaTelefonesUsu = listaTelefonesUsu;
	}
	
	
}
