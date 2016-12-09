import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class Agent {

	private final static Scanner sc = new Scanner(System.in);
	private static int id = 0;
	private Connection conn;

	private PreparedStatement agents, ajouterreperage, ajouterCombat, ajouterParticipation, infoShVivant, ajoutersh;

	public Agent() {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver PostgreSQL manquant !");
			System.exit(1);
		}

		System.out.println("Connection...");
		String url = "jdbc:postgresql://localhost:5432/projetshyeld" + "?user=postgres&password=Pataques7";
		conn = null;
		try {
			conn = DriverManager.getConnection(url);
			System.out.println("Connection établie!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Impossible de joindre le server !");
			System.exit(1);
		}

		try {
			agents = conn.prepareStatement("SELECT * FROM projetshyeld.agents WHERE id_agent=? AND etat='actif'");
			ajouterreperage = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterreperage(?,?,?,?,?)");
			ajouterCombat = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterCombat(?,?,?,?)");
			ajouterParticipation = conn.prepareStatement("SELECT * FROM projetshyeld.ajouterParticipation(?,?,?)");
			infoShVivant = conn.prepareStatement("SELECT * FROM projetshyeld.infoShVivant WHERE nom_sh=?");
			ajoutersh = conn.prepareStatement("SELECT * FROM projetshyeld.ajoutersh(?,?,?,?,?,?,?,?,?,?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void connect() {
		boolean estConnecte = false;
		while (!estConnecte) {
			System.out.println("---LOGIN---");
			System.out.println("Id:");
			id = sc.nextInt();
			sc.nextLine();
			System.out.println("Mot de passe:");
			String mdp = sc.next();
			sc.nextLine();

			try {

				agents.setInt(1, id);
				ResultSet rs = agents.executeQuery();
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
	}

	public void reperage() {
		System.out.print("Nom du hero :");
		String nomSuperHero = sc.nextLine();
		System.out.println("Coordonées:");
		System.out.print("   -X:");
		int x = sc.nextInt();
		sc.nextLine();
		System.out.print("   -Y:");
		int y = sc.nextInt();
		sc.nextLine();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try {

			ajouterreperage.setInt(1, id);
			ajouterreperage.setString(2, nomSuperHero);
			ajouterreperage.setInt(3, x);
			ajouterreperage.setInt(4, y);
			ajouterreperage.setTimestamp(5, timestamp);
			ajouterreperage.executeQuery();
			System.out.println("Reperage de " + nomSuperHero + " en (" + x + ";" + y + ") enregisté !");

		} catch (SQLException se) {
			System.out.println(se.getMessage());
			//TODO Si hero n'existe pas , demander si l'ajouter
		}

	}

	public void ajouterCombat() {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int idCombat = 0;
		boolean combatCree = false;
		boolean aMarvelle = false;
		boolean aDece = false;
		System.out.println("Ajout du Combat:");
		System.out.println(" *Coordonées:");
		System.out.print("   -X:");
		int x = sc.nextInt();
		sc.nextLine();
		System.out.print("   -Y:");
		int y = sc.nextInt();
		sc.nextLine();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {

			ajouterCombat.setInt(1, id);
			ajouterCombat.setInt(2, x);
			ajouterCombat.setInt(3, y);
			ajouterCombat.setTimestamp(4, timestamp);
			ResultSet rs = ajouterCombat.executeQuery();
			if (rs.next()) {
				idCombat = rs.getInt(1);
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
				System.out.print("Nom du hero :");
				String nomSuperHero = sc.nextLine();
				int choixResult;
				do {

					System.out.println("Quel est le resultat ?");
					System.out.println("1 -> Victoire");
					System.out.println("2 -> Défaite");
					System.out.println("3 -> Ni victoire ni défaite");
					choixResult = sc.nextInt();
					sc.nextLine();
				} while (choixResult < 1 || choixResult > 3);

				String result = null;
				if (choixResult == 1)
					result = "victoire";
				if (choixResult == 2)
					result = "defaite";
				try {

					ajouterParticipation.setInt(1, idCombat);
					ajouterParticipation.setString(2, nomSuperHero);
					ajouterParticipation.setString(3, result);
					ResultSet rs = ajouterParticipation.executeQuery();
					if (rs.next()) {
						// fonction sql retourne 1 si la faction est marvelle ,
						// 2 si c'est Dece
						int faction = rs.getInt(1);
						if (faction == 1)
							aMarvelle = true;
						if (faction == 2)
							aDece = true;
					}

				} catch (SQLException se) {
					System.out.println(se.getMessage());
					// se.printStackTrace();
				}

				do {
					System.out.println("Ajouter une autre participation ?");
					System.out.println("1 -> Oui");
					System.out.println("2 -> Non");
					choixParticipation = sc.nextInt();
					sc.nextLine();
				} while (choixParticipation != 1 && choixParticipation != 2);
			} while (choixParticipation == 1);
			try {
				if (!combatCree) {
					System.out.println("Combat non créé. Rien n'a été enregistré");
					conn.rollback();
				} else if (!aDece || !aMarvelle) {
					System.out
							.println("Le combat doit au moins contenir 1 Marvelle et 1 Dece.  Rien n'a été enregistré");
					conn.rollback();
				} else {
					System.out.println("Combat n°" + idCombat + " en (" + x + ";" + y + ") enregisté !");
					conn.commit();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void infoSuperHero() {
		try {
			System.out.print("Nom du hero :");
			String nomSuperHero = sc.nextLine();

			infoShVivant.setString(1, nomSuperHero);
			ResultSet rs = infoShVivant.executeQuery();
			System.out.printf("%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s\n", "Nom Civil", "Adresse",
					"Origine", "Type de pouvoir", "Puissance", "Faction", "Victoires", "Defaites", "Participations",
					"Coordonées");
			System.out.println(
					"-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			if (rs.next()) {
				System.out.printf("%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s\n",
						rs.getString("nom_civil"), rs.getString("adresse_privee"), rs.getString("origine"),
						rs.getString("type_pouvoir"), rs.getInt("puissance_pouvoir"), rs.getString("faction"),
						rs.getString("nb_victoire"), rs.getString("nb_defaite"), rs.getString("nb_part"),
						rs.getString("Coordonées"));

			} else {
				System.out.println("Ce hero n'existe pas ou est mort!");
				int choix;
				do {
					System.out.println("Voulez vous ajouter " + nomSuperHero + " ?");
					System.out.println("1 -> Oui");
					System.out.println("2 -> Non");
					choix = sc.nextInt();
					sc.nextLine();
					if (choix == 1) {
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
						sc.nextLine();
						int choixFaction;
						String faction = "";
						do {
							System.out.println("Quel est sa faction ?");
							System.out.println("1 -> Marvelle");
							System.out.println("2 -> Dece");
							choixFaction = sc.nextInt();
							sc.nextLine();
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
						sc.nextLine();
						System.out.print("   -Y:");
						int y = sc.nextInt();
						sc.nextLine();
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						// sql
						try {
							ajoutersh.setString(1, nomCivil);
							ajoutersh.setString(2, nomSuperHero);
							ajoutersh.setString(3, adresse);
							ajoutersh.setString(4, origine);
							ajoutersh.setString(5, type_pouvoir);
							ajoutersh.setInt(6, puissance_pouvoir);
							ajoutersh.setString(7, faction);
							ajoutersh.setInt(8, id);
							ajoutersh.setInt(9, x);
							ajoutersh.setInt(10, y);
							ajoutersh.setTimestamp(11, timestamp);
							ajoutersh.executeQuery();

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
