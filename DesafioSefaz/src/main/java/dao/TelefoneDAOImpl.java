package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidade.Telefone;
import util.JdbcUtil;

public class TelefoneDAOImpl implements TelefoneDAO {
	
	public Long recuperaId() {
		
		String sql = "SELECT S_TELEFONE.NEXTVAL FROM DUAL";
		
		Long idRetorno = null;
		
		Connection conexao;
		
		try {
			conexao = JdbcUtil.getConexao();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				idRetorno = res.getLong(1);
			 }
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idRetorno;
	}
	
	public void incluir(Telefone telefone) {
		
		String sql = "INSERT INTO TELEFONE (ID, DDD, NUMERO, TIPO, EMAIL_USU) VALUES (?, ?, ?, ?, ?)";
		
		Connection conexao;
		
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			Long id = this.recuperaId();
			
			ps.setLong(1, id);
			ps.setLong(2, telefone.getDdd());
			ps.setString(3,telefone.getNumero());
			ps.setString(4,telefone.getTipo());
			ps.setString(5, telefone.getEmail_usu());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void alterar(Telefone telefone) {
		
		String sql = "UPDATE TELEFONE SET DDD = ? , NUMERO = ?, TIPO = ? WHERE EMAIL_USU = ?";
		
		Connection conexao;
		
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setLong(1, telefone.getDdd());
			ps.setString(2, telefone.getNumero());
			ps.setString(3, telefone.getTipo());
			ps.setString(4, telefone.getEmail_usu());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void remover(Telefone telefone) {

		String sql = "DELETE FROM TELEFONE WHERE EMAIL_USU = ?";

		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, telefone.getEmail_usu());

			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Telefone pesquisar(String email_usu) {

		String sql = "SELECT T.EMAIL_USU, T.DDD, T.NUMERO, T.TIPO FROM TELEFONE T WHERE T.EMAIL_USU = ? ORDER BY T.EMAIL_USU";
		Telefone telefone = null;
		
		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, email_usu);

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				telefone = new Telefone();
				telefone.setEmail_usu(res.getString("EMAIL_USU"));
				telefone.setDdd(res.getLong("DDD"));
				telefone.setNumero(res.getString("NUMERO"));
				telefone.setTipo(res.getString("TIPO"));
				
		 }
			
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return telefone;
	}

	public List<Telefone> consultarTodos() {
		
		String sql = "SELECT T.EMAIL_USU, T.DDD, T.NUMERO, T.TIPO FROM TELEFONE T ORDER BY T.EMAIL_USU";
		
		List<Telefone> listaTelefones = new ArrayList<Telefone>();
		
		Connection conexao;
		try {
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				
				Telefone telefone = new Telefone();
				telefone.setEmail_usu(res.getString("EMAIL_USU"));
				telefone.setDdd(res.getLong("DDD"));
				telefone.setNumero(res.getString("NUMERO"));
				telefone.setTipo(res.getString("TIPO"));
				listaTelefones.add(telefone);

			 }
			
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaTelefones;

	}

}
