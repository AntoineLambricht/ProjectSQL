import java.util.Scanner;

public class MainAppAgent {

	private final static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int choix;
		Agent a = new Agent();
		a.connect();
		do {
			System.out.println("---------------------------------");
			System.out.println("1 -> Info super-hero");
			System.out.println("2 -> Raport de combat");
			System.out.println("3 -> Rep�rage");
			System.out.println("0 -> Deconnexion");
			System.out.println("---------------------------------");
			choix = sc.nextInt();

			switch (choix) {
			case 1:
				a.infoSuperHero();
				break;
			case 2:
				a.ajouterCombat();
				break;
			case 3:
				a.reperage();
				break;
			default:
				break;
			}
		} while (choix != 0);
	}

}
