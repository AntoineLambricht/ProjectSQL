import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class Centrale {

	private final static Scanner sc = new Scanner(System.in);
	private PreparedStatement heroperdu, mortsh, zonededanger, retraiteAgent, mortAgent, inscrireAgent, reperageParDate;

	public Centrale() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver PostgreSQL manquant !");
			System.exit(1);
		}
		System.out.println("Connection...");
		String url = "jdbc:postgresql://localhost:5432/projetshyeld" + "?user=postgres&password=Pataques7";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			System.out.println("Connection établie!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Impossible de joindre le server !");
			System.exit(1);
		}

		try {
			heroperdu = conn.prepareStatement("SELECT * FROM projetshyeld.heroperdu");
			mortsh = conn.prepareStatement("SELECT * FROM projetshyeld.mortsh(?)");
			zonededanger = conn.prepareStatement("SELECT * FROM projetshyeld.zonededanger");
			retraiteAgent = conn.prepareStatement("SELECT * FROM projetshyeld.retraiteAgent(?)");
			mortAgent = conn.prepareStatement("SELECT * FROM projetshyeld.mortAgent(?)");
			inscrireAgent = conn.prepareStatement("SELECT * FROM projetshyeld.inscrireAgent(?,?,?)");
			reperageParDate = conn.prepareStatement("SELECT * FROM projetshyeld.reperageParDate(?,?,?)");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void heroPerdu() {
		try {
			ResultSet rs = heroperdu.executeQuery();
			System.out.println("Nom de super-hero  -  Coordonnée");
			while (rs.next()) {

				String nom = rs.getString(1);
				String coord = rs.getString(2);

				System.out.println(nom + " | " + coord);
			}
		} catch (SQLException se) {
			System.out.println("Erreur lors de l'utilisation de la vue hero perdu!");
			se.printStackTrace();
			System.exit(1);
		}

	}

	public void suprimmerSuperHero() {
		System.out.println("Nom du super-hero:");
		String nom = sc.nextLine();
		try {
			mortsh.setString(1, nom);
			mortsh.executeQuery();
			System.out.println(nom + " est mort! RIP");

		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}

	}

	public void zoneDeDanger() {
		try {
			ResultSet rs = zonededanger.executeQuery();
			System.out.println("Zone 1  -  Zone 2");
			while (rs.next()) {

				String zone1 = rs.getString("Zone 1");
				String zone2 = rs.getString("Zone 2");

				System.out.println(zone1 + " - " + zone2);
			}
		} catch (SQLException se) {
			System.out.println("Erreur lors de l'utilisation de la vue zone de danger!");
			se.printStackTrace();
			System.exit(1);
		}

	}

	public void releveAgent() {
		boolean date1ok = true;
		boolean date2ok = true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		System.out.print("Id de l'agent :");
		int id = sc.nextInt();

		System.out.println("Date 1 (\"jour/mois/année\"):");
		String sDate1 = sc.next();

		Date date1 = null;
		try {
			date1 = sdf.parse(sDate1);
		} catch (ParseException e) {
			System.out.println("Format de la date incorrecte");
			date1ok = false;
		}

		System.out.println("Date 2 (\"jour/mois/année\"):");
		String sDate2 = sc.next();
		Date date2 = null;
		if (date1ok) {

			try {
				date2 = sdf.parse(sDate2);
			} catch (ParseException e) {
				System.out.println("Format de la date incorrecte");
				date2ok = false;
			}
		}
		if (date2ok) {
			try {
				reperageParDate.setInt(1, id);
				reperageParDate.setDate(2, new java.sql.Date(date1.getTime()));
				reperageParDate.setDate(3, new java.sql.Date(date2.getTime()));
				ResultSet rs = reperageParDate.executeQuery();
				while (rs.next()) {
					System.out.println(rs.toString());

				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public void statistique() {
		// TODO Auto-generated method stub

	}

	public void supressionAgent() {
		boolean ok;
		do {
			ok = true;
			System.out.println("-------Desactivation d'un agent----------");
			System.out.println("Pourquoi voulez vous désactiver un agent?");
			System.out.println("1 -> Retraite");
			System.out.println("2 -> Mort");
			System.out.println("autre -> Annuler");

			int choix = sc.nextInt();
			if (choix == 1) {
				System.out.println("Id de l'agent:");
				int id = sc.nextInt();
				try {
					retraiteAgent.setInt(1, id);
					retraiteAgent.executeQuery();
					System.out.println("Agent n°" + id + " est maintenant à la retraite!");

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					ok = false;
				}
			} else if (choix == 2) {
				System.out.println("Id de l'agent:");
				int id = sc.nextInt();
				try {
					mortAgent.setInt(1, id);
					mortAgent.executeQuery();
					System.out.println("Agent n°" + id + " est maintenant déclaré mort! RIP");

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					ok = false;
				}
			}
		} while (!ok);

	}

	public void inscriptionAgent() {
		System.out.println("-------Ajout d'un agent----------");
		System.out.print("Nom:");
		String nom = sc.nextLine();
		System.out.print("Prenom:");
		String prenom = sc.nextLine();
		System.out.print("Mot de passe:");
		String mdp = sc.nextLine();
		String hashed = BCrypt.hashpw(mdp, BCrypt.gensalt());
		try {

			inscrireAgent.setString(1, nom);
			inscrireAgent.setString(2, prenom);
			inscrireAgent.setString(3, hashed);
			ResultSet rs = inscrireAgent.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("inscrireagent");

				System.out.println("Id de l'agent : " + id);
			}
		} catch (SQLException se) {
			System.out.println("Erreur lors de l'utilisation de l'inscription d'un agent!");
			se.printStackTrace();
			System.exit(1);
		}

	}

}
