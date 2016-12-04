import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class AgentDuShyeld {

	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
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
		boolean estConnecte = false;
		int id = 0;
		while (!estConnecte) {
			System.out.println("---LOGIN---");
			System.out.println("Id:");
			id = sc.nextInt();
			System.out.println("Mot de passe:");
			String mdp = sc.next();

			try {
				// bad select , agent can be dead or retreated
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.agents WHERE id_agent=?");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String nom = rs.getString("nom");
					String prenom = rs.getString("prenom");
					String mdpagent = rs.getString("mdp");
					if (mdp.equals(mdpagent)) {
						System.out.println("Bienvenue " + nom + " " + prenom + "!");
						estConnecte = true;
					} else {
						estConnecte = false;
					}
				}
				if (!estConnecte) {
					System.out.println("Mauvai id / mot de passe");
				}

			} catch (SQLException se) {
				System.out.println("Erreur lors de la selection de l'agent!");
				se.printStackTrace();
				System.exit(1);
			}

		}
		if (estConnecte) {
			int choix;

			do {
				System.out.println("---------------------------------");
				System.out.println("1 -> Info super-hero");
				System.out.println("2 -> Raport de combat");
				System.out.println("3 -> Repérage");
				System.out.println("0 -> Quitter");
				System.out.println("---------------------------------");
				choix = sc.nextInt();

				switch (choix) {
				case 1:
					infoSuperHero(conn);
					break;
				case 2:
					ajouterCombat(conn, id);
					break;
				case 3:
					reperage(conn, id);
					break;
				default:
					break;
				}
			} while (choix != 0);
		}

	}

	private static void reperage(Connection conn, int idAgent) {
		sc.nextLine();
		System.out.print("Nom du hero :");
		String nomSuperHero = sc.nextLine();
		System.out.println("Coordonées:");
		System.out.print("   -X:");
		int x = sc.nextInt();
		System.out.print("   -Y:");
		int y = sc.nextInt();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try {

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterreperage(?,?,?,?,?)");
			ps.setInt(1, idAgent);
			ps.setString(2, nomSuperHero);
			ps.setInt(3, x);
			ps.setInt(4, y);
			ps.setTimestamp(5, timestamp);
			ps.executeQuery();
			System.out.println("Reperage de " + nomSuperHero + " en (" + x + ";" + y + ") enregisté !");

		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}

	}

	private static void ajouterCombat(Connection conn, int idAgent) {

	}

	private static void infoSuperHero(Connection conn) {
		try {
			sc.nextLine();
			System.out.print("Nom du hero :");
			String nomSuperHero = sc.nextLine();
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM projetshyeld.infoShVivant WHERE nom_sh=?");
			ps.setString(1, nomSuperHero);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString("nom_civil") + "-" + rs.getString("adresse_privee") + "-"
						+ rs.getString("origine") + "-" + rs.getString("type_pouvoir") + "-"
						+ rs.getInt("puissance_pouvoir") + "-" + rs.getString("faction") + "-" + rs.getString("Coordonées"));
				
			} else {
				System.out.println("Ce hero n'existe pas ou est mort!");
				int choix;
				do {
					System.out.println("Voulez vous l'ajouter ?");
					System.out.println("1 -> Oui");
					System.out.println("2 -> Non");
					choix = sc.nextInt();
					if (choix == 1) {
						//TODO ajouter un sh et ajouter un reperage
					}
				} while (choix != 1 && choix != 2);

			}
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
	}

}
