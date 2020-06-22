package entidade;

import java.util.List;

public class Usuario {

	private String nome;
	private String email;
	private String senha;
	private Telefone telefone;
	private List<Telefone> listaTelefonesUsu;

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Telefone> getListaTelefonesUsu() {
		return listaTelefonesUsu;
	}

	public void setListaTelefonesUsu(List<Telefone> listaTelefonesUsu) {
		this.listaTelefonesUsu = listaTelefonesUsu;
	}


}
