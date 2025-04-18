package nl.hu.bep2.casino.blackjack.domain.speler;

import nl.hu.bep2.casino.blackjack.domain.exception.WrongPlayerNameException;

public class SpelerAuthorizatieService {
    public static boolean isAdmin(Speler speler) {
        if ("admin".equals(speler.getNaam())) {
            return true;
        } else {
            throw new WrongPlayerNameException("Deze spelernaam is niet 'admin'. Deze functie is daarom verboden.");
        }
    }
}