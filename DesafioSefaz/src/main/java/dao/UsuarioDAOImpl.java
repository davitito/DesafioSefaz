package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidade.Telefone;
import entidade.Usuario;
import util.JdbcUtil;

public class UsuarioDAOImpl implements UsuarioDAO {

	public void incluir(Usuario usuario) {

		String sql = "INSERT INTO USUARIO (EMAIL, NOME, SENHA) VALUES (?, ?, ?)";

		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, usuario.getEmail());
			ps.setString(2, usuario.getNome());
			ps.setString(3, usuario.getSenha());

			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void alterar(Usuario usuario) {
		
		String sql = "UPDATE USUARIO SET NOME = ? where EMAIL = ?";

		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, usuario.getNome());
			ps.setString(2, usuario.getEmail());

			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void remover(Usuario usuario) {

		String sql = "DELETE FROM USUARIO WHERE EMAIL = ?";

		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, usuario.getEmail());

			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Usuario pesquisar(String email) {

		String sql = "SELECT U.EMAIL, U.NOME, U.SENHA FROM USUARIO U WHERE U.EMAIL = ?  ORDER BY U.EMAIL";
		
		Usuario usuario = null;
		
		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, email);

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				usuario = new Usuario();
				usuario.setEmail(res.getString("EMAIL"));
				usuario.setNome(res.getString("NOME"));
				usuario.setSenha(res.getString("SENHA"));				
			 }
			
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usuario;
	}

	public List<Usuario> consultarTodos() {

		String sql = "SELECT U.EMAIL, U.NOME, U.SENHA FROM USUARIO U  ORDER BY U.EMAIL";
	
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		
		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				
				Usuario usuario = new Usuario();
				usuario.setEmail(res.getString("EMAIL"));
				usuario.setNome(res.getString("NOME"));
				usuario.setSenha(res.getString("SENHA"));
				
				listaUsuarios.add(usuario);
			 }
			
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaUsuarios;

	}
	
	public List<Usuario> consultarTodosUsuTel() {
		
		String sql = "SELECT * FROM USUARIO U INNER JOIN TELEFONE T ON U.EMAIL = T.EMAIL_USU ORDER BY U.EMAIL";
		
		List<Usuario> listaUsuariosTelefones = new ArrayList<Usuario>();
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ResultSet res = ps.executeQuery();
			
			while (res.next()) {
				
				Usuario usuario = new Usuario();
				usuario.setEmail(res.getString("EMAIL"));
				usuario.setNome(res.getString("NOME"));
				
				Telefone telefone = new Telefone();
				telefone.setDdd(res.getLong("DDD"));
				telefone.setNumero(res.getString("NUMERO"));
				telefone.setTipo(res.getString("TIPO"));
				usuario.setTelefone(telefone);
				listaUsuariosTelefones.add(usuario);
			 }
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaUsuariosTelefones;

	}
}
