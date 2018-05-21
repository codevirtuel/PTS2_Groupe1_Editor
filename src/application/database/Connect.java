package application.database;

import java.sql.*;

public class Connect {

	Statement stt;
	PreparedStatement pst;
	Connection con;

	public Connect(String chemin, String nom, String mdp) throws SQLException {
		con = DriverManager.getConnection("jdbc:h2:" + chemin, nom, mdp);
		stt = con.createStatement();
	}

	public ResultSet executeQueryCmd(String requete) throws SQLException {
		pst = con.prepareStatement(requete);
		ResultSet res = pst.executeQuery();
		return res;
	}

	public boolean executeUpdateCmd(String requete) throws SQLException {
		pst = con.prepareStatement(requete);
		boolean res = pst.execute();
		return res;
	}

	public void Affichage(ResultSet res, String nom) throws SQLException {
		while (res.next()) {
			System.out.println(res.getString(nom));
		}

	}

	public void Disconnect() throws SQLException {
		con.close();

	}

}