package dao;

import entity.Personne;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.Database;

public class DaoPersonne {

	/**
	 * Fournit la Personne par son ID
	 * 
	 * @param id
	 * @return Object Personne
	 * @throws SQLException
	 */
	public Personne getById(int id) throws SQLException {
		Personne result = null;
		Statement stmt = Database.getConnection().createStatement();
		String sql = "SELECT * FROM personne WHERE id_personne=" + id;
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			result = new Personne(-1, rs.getString("nom"), rs.getString("prenom"),
					rs.getString("mail"), rs.getString("telephone"), rs.getString("adresse"), rs.getString("code_postal"),
					rs.getString("ville"), rs.getString("mot_de_passe"), rs.getBoolean("est_formateur"),
					rs.getBoolean("est_administration"), rs.getTimestamp("date_inscription"));
		}
		return result;
	}

	/**
	 * Permet d'insérer en base de données une nouvelles personne
	 * 
	 * @param personne
	 * @return
	 * @throws SQLException
	 */
	public boolean inserer(Personne personne) throws SQLException {
		boolean result = false;
		Connection db = Database.getConnection();
		String sql = "INSERT INTO personne(prenom, nom, mail, tel, adresse, code_postal,"
				+ "ville, mot_de_passe, est_formateur, est_administration, date_inscription)" 
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, PASSWORD(?), ?, ?, ?)";
		PreparedStatement stmt = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, personne.getPrenom());
		stmt.setString(2, personne.getNom());
		stmt.setString(3, personne.getMail());
		stmt.setString(4, personne.getTelephone());
		stmt.setString(5, personne.getAdresse());
		stmt.setString(6, personne.getCp());
		stmt.setString(7, personne.getVille());
		stmt.setString(8, personne.getPassword());
		stmt.setBoolean(9, personne.isEstFormateur());
		stmt.setBoolean(10, personne.isEstAdmin()); 
		stmt.setTimestamp(11, personne.getDateInscription());

		// Renvoie le nombre de ligne affectées, si 1 alors insertion réalisée
		if (stmt.executeUpdate() > 0) {
			result = true;
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			personne.setId(rs.getInt(1));
		}
		return result;
	}
	
	public Personne getByLoginMdp(String mail, String mdp)  throws SQLException {
		Personne personne = null;
		Connection connection = Database.getConnection();
		String sql = " SELECT * FROM personne WHERE mail=? AND mot_de_passe=?";
		PreparedStatement requete = connection.prepareStatement(sql);
		requete.setString(1, mail);
		requete.setString(2, mdp);
		ResultSet rs = requete.executeQuery();
		if (rs.next() ) {
			personne = new Personne(
					rs.getInt("id_personne"), 
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("mail"),
					rs.getString("tel"), 
					rs.getString("adresse"),
					rs.getString("code_postal"),
					rs.getString("ville"), 
					rs.getString("mot_de_passe"), 
					rs.getBoolean("est_formateur"),
					rs.getBoolean("est_administration"), rs.getTimestamp("date_inscription"));
		}
		return personne;
	}
	
}
