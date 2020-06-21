package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import entidade.Usuario;


@ManagedBean(name = "LoginBean")
@SessionScoped
public class LoginBean {
	
	//Variáveis do login.xhtml
	private String txtEmail;
	private String txtSenha;
	
	private List<Usuario> listaUsuarios;
	private Usuario usuario;
	private String msgCadastroUsuario;
	
	private UsuarioDAO usuarioDAO;
	
	public LoginBean() {
		this.listaUsuarios = new ArrayList<Usuario>();
		this.usuario = new Usuario();
		this.usuarioDAO = new UsuarioDAOImpl();

	}

	/**
	 * Validar o usuário no login para dar acesso as funcionalidades (Consultar todos - Remover e alterar)
	 * A funcionalidade Incluir pode ser feita antes do login
	 */
	public void entrar() throws IOException {
		Usuario usuarioLogado = null;
		this.listaUsuarios = this.usuarioDAO.consultarTodos();
		for (Usuario usuarioPesquisa : listaUsuarios) {
			if (usuarioPesquisa.getEmail().equals(this.txtEmail) && usuarioPesquisa.getSenha().equals(this.txtSenha)) {
				usuarioLogado = usuarioPesquisa;
			}
		}
		if (usuarioLogado != null) {
			HttpSession sessao =  (HttpSession)FacesContext.getCurrentInstance()
													.getExternalContext().getSession(true);
			sessao.setAttribute("usuarioLogado", usuarioLogado);
			FacesContext.getCurrentInstance().getExternalContext().redirect("funcionalidades.xhtml?faces-redirect=true&amp;includeViewParams=true");			
		} else {
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Usuário não cadastrado ou senha inválida!!!"));
		}
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

	public String getMsgCadastroUsuario() {
		return msgCadastroUsuario;
	}

	public void setMsgCadastroUsuario(String msgCadastroUsuario) {
		this.msgCadastroUsuario = msgCadastroUsuario;
	}
	
	
}
