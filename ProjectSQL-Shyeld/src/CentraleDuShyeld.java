import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class CentraleDuShyeld {

	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// chargement du driver
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
		int choix;

		do {
			System.out.println("---------------------------------");
			System.out.println("1 -> Inscription d'un agent");
			System.out.println("2 -> Supression d'un agent");
			System.out.println("3 -> Heros perdu");
			System.out.println("4 -> Suprimmer un super-hero");
			System.out.println("5 -> Zones de danger");
			System.out.println("6 -> Relevés d'un agent");
			System.out.println("7 -> Statistiques");
			System.out.println("0 -> Quitter");
			System.out.println("---------------------------------");

			choix = sc.nextInt();

			switch (choix) {
			case 1:
				inscriptionAgent(conn);
				break;
			case 2:
				supressionAgent(conn);
				break;
			case 3:
				heroPerdu(conn);
				break;
			case 4:
				suprimmerSuperHero(conn);
				break;
			case 5:
				zoneDeDanger(conn);
				break;
			case 6:
				releveAgent(conn);
				break;
			case 7:
				statistique(conn);
				break;
			default:
				break;
			}
		} while (choix != 0);
	}

	private static void heroPerdu(Connection conn) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.heroperdu");
			ResultSet rs = ps.executeQuery();
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

	private static void suprimmerSuperHero(Connection conn) {
		System.out.println("Nom du super-hero:");
		sc.nextLine();
		String nom = sc.nextLine();
		try {

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.mortsh(?)");
			ps.setString(1, nom);
			ps.executeQuery();
			System.out.println(nom + " est mort! RIP");

		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}

	}

	private static void zoneDeDanger(Connection conn) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.zonededanger");
			ResultSet rs = ps.executeQuery();
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

	private static void releveAgent(Connection conn) {
		// TODO Auto-generated method stub

	}

	private static void statistique(Connection conn) {
		// TODO Auto-generated method stub

	}

	private static void supressionAgent(Connection conn) {
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

					PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.retraiteAgent(?)");
					ps.setInt(1, id);
					ps.executeQuery();
					System.out.println("Agent n°" + id + " est maintenant à la retraite!");

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					ok = false;
				}
			} else if (choix == 2) {
				System.out.println("Id de l'agent:");
				int id = sc.nextInt();
				try {

					PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.mortAgent(?)");
					ps.setInt(1, id);
					ps.executeQuery();
					System.out.println("Agent n°" + id + " est maintenant déclaré mort! RIP");

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					ok = false;
				}
			}
		} while (!ok);

	}

	private static void inscriptionAgent(Connection conn) {
		System.out.println("-------Ajout d'un agent----------");
		System.out.print("Nom:");
		sc.nextLine();
		String nom = sc.nextLine();
		System.out.print("Prenom:");
		String prenom = sc.nextLine();
		System.out.print("Mot de passe:");
		String mdp = sc.nextLine();
		String hashed = BCrypt.hashpw(mdp, BCrypt.gensalt());
		try {

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.inscrireAgent(?,?,?)");
			ps.setString(1, nom);
			ps.setString(2, prenom);
			ps.setString(3, hashed);
			ResultSet rs = ps.executeQuery();
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
