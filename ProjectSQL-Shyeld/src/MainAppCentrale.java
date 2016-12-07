import java.util.Scanner;

public class MainAppCentrale {

	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		Centrale c = new Centrale();
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
				c.inscriptionAgent();
				break;
			case 2:
				c.supressionAgent();
				break;
			case 3:
				c.heroPerdu();
				break;
			case 4:
				c.suprimmerSuperHero();
				break;
			case 5:
				c.zoneDeDanger();
				break;
			case 6:
				c.releveAgent();
				break;
			case 7:
				c.statistique();
				break;
			default:
				break;
			}
		} while (choix != 0);
	}

}
