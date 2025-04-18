package nl.hu.bep2.casino.blackjack.domain.speler;

import nl.hu.bep2.casino.blackjack.domain.exception.WrongPlayerNameException;

public class SpelerAuthorizatieService {
    public static boolean isAdmin(Player player) {
        if ("admin".equals(player.getName())) {
            return true;
        } else {
            throw new WrongPlayerNameException("Spillernavnet ække 'admin'. Denne funksjonen er derfor forbudt.");
        }
    }
}