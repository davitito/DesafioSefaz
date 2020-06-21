package entidade;


public class Telefone {

	private Long id;
	private Long ddd;
	private String numero;
	private String tipo;
	private String email_usu;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDdd() {
		return ddd;
	}
	public void setDdd(Long ddd) {
		this.ddd = ddd;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEmail_usu() {
		return email_usu;
	}
	public void setEmail_usu(String email_usu) {
		this.email_usu = email_usu;
	}

	
	
}
