package dao;

import java.util.List;
import entidade.Telefone;

public interface TelefoneDAO {
	
	public void incluir(Telefone telefone);
	
	public void remover(Telefone telefone);

	public Telefone pesquisar(String email_usu);

	public void alterar(Telefone telefone);
	
	public List<Telefone> consultarTodos();

}
