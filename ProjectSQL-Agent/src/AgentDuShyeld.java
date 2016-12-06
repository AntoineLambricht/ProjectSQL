import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

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
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM projetshyeld.agents WHERE id_agent=? AND etat='actif'");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String nom = rs.getString("nom");
					String prenom = rs.getString("prenom");
					String mdpagent = rs.getString("mdp");
					if (BCrypt.checkpw(mdp, mdpagent)) {
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
					infoSuperHero(conn, id);
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
		int idCombat = 0;
		boolean combatCree = false;
		System.out.println("Ajout du Combat:");
		sc.nextLine();
		System.out.println(" *Coordonées:");
		System.out.print("   -X:");
		int x = sc.nextInt();
		System.out.print("   -Y:");
		int y = sc.nextInt();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterCombat(?,?,?,?)");
			ps.setInt(1, idAgent);
			ps.setInt(2, x);
			ps.setInt(3, y);
			ps.setTimestamp(4, timestamp);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				idCombat = rs.getInt(1);
				System.out.println("Combat n°" + idCombat + " en (" + x + ";" + y + ") enregisté !");
				combatCree = true;
			}

		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
		if (combatCree) {
			System.out.println();
			System.out.println(" *Participations:");
			int choixParticipation;
			do {
				sc.nextLine();
				System.out.print("Nom du hero :");
				String nomSuperHero = sc.nextLine();
				int choixResult;
				do {

					System.out.println("Quel est le resultat ?");
					System.out.println("1 -> Victoire");
					System.out.println("2 -> Défaite");
					System.out.println("3 -> Ni victoire ni défaite");
					choixResult = sc.nextInt();
				} while (choixResult < 1 || choixResult > 3);
				
				String result = null;
				if(choixResult == 1) result = "victoire";
				if(choixResult == 2) result = "defaite";
				
				try {

					PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterParticipation(?,?,?)");
					ps.setInt(1, idCombat);
					ps.setString(2, nomSuperHero);
					ps.setString(3, result);
					ps.executeQuery();
					

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					se.printStackTrace();
				}
				
				
				do{
				System.out.println("Ajouter une autre participation ?");
				System.out.println("1 -> Oui");
				System.out.println("2 -> Non");
				choixParticipation = sc.nextInt();
				}while (choixParticipation != 1 && choixParticipation != 2);
			} while (choixParticipation == 1);
			
			
		}
	}

	private static void infoSuperHero(Connection conn, int idAgent) {
		try {
			sc.nextLine();
			System.out.print("Nom du hero :");
			String nomSuperHero = sc.nextLine();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM projetshyeld.infoShVivant WHERE nom_sh=?");
			ps.setString(1, nomSuperHero);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println(rs.getString("nom_civil") + "-" + rs.getString("adresse_privee") + "-"
						+ rs.getString("origine") + "-" + rs.getString("type_pouvoir") + "-"
						+ rs.getInt("puissance_pouvoir") + "-" + rs.getString("faction") + "-"
						+ rs.getString("nb_victoire") + "-" + rs.getString("nb_defaite") + "-" + rs.getString("nb_part")
						+ "-" + rs.getString("Coordonées"));

			} else {
				System.out.println("Ce hero n'existe pas ou est mort!");
				int choix;
				do {
					System.out.println("Voulez vous ajouter " + nomSuperHero + " ?");
					System.out.println("1 -> Oui");
					System.out.println("2 -> Non");
					choix = sc.nextInt();
					if (choix == 1) {
						sc.nextLine();
						String temp;
						System.out
								.println("Quel est son nom civil ? (Tapez \"?\" si vous n'avez pas cette information)");
						temp = sc.nextLine();
						String nomCivil = null;
						if (!temp.equals("?"))
							nomCivil = temp;
						System.out.println(
								"Quel est son adresse privée ? (Tapez \"?\" si vous n'avez pas cette information)");
						temp = sc.nextLine();
						String adresse = null;
						if (!temp.equals("?"))
							adresse = temp;
						System.out.println("Quel est son origine ? (Tapez \"?\" si vous n'avez pas cette information)");
						temp = sc.nextLine();
						String origine = null;
						if (!temp.equals("?"))
							origine = temp;
						System.out.println(
								"Quel est son type de pouvoir ? (Tapez \"?\" si vous n'avez pas cette information)");
						temp = sc.nextLine();
						String type_pouvoir = null;
						if (!temp.equals("?"))
							type_pouvoir = temp;
						System.out.println("Quel est la puissance de son pouvoir?");
						int puissance_pouvoir = sc.nextInt();
						int choixFaction;
						String faction = "";
						do {
							System.out.println("Quel est sa faction ?");
							System.out.println("1 -> Marvelle");
							System.out.println("2 -> Dece");
							choixFaction = sc.nextInt();
							if (choixFaction == 1)
								faction = "marvelle";
							else if (choixFaction == 2)
								faction = "dece";
							else
								System.out.println("le choix de faction doit etre 1 ou 2");
						} while (choixFaction != 1 && choixFaction != 2);

						System.out.println("Où l'avez vous vu ?");
						System.out.println("Coordonées:");
						System.out.print("   -X:");
						int x = sc.nextInt();
						System.out.print("   -Y:");
						int y = sc.nextInt();
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						// sql
						try {

							PreparedStatement ps2 = conn
									.prepareStatement("SELECT * FROM projetshyeld.ajoutersh(?,?,?,?,?,?,?,?,?,?,?)");
							ps2.setString(1, nomCivil);
							ps2.setString(2, nomSuperHero);
							ps2.setString(3, adresse);
							ps2.setString(4, origine);
							ps2.setString(5, type_pouvoir);
							ps2.setInt(6, puissance_pouvoir);
							ps2.setString(7, faction);
							ps2.setInt(8, idAgent);
							ps2.setInt(9, x);
							ps2.setInt(10, y);
							ps2.setTimestamp(11, timestamp);
							ps2.executeQuery();

						} catch (SQLException se) {
							System.out.println(se.getMessage());
						}

					}
				} while (choix != 1 && choix != 2);

			}
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
	}

}
