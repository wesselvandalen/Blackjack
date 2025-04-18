package nl.hu.bep2.casino.blackjack.presentation.dto;

import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;

/**
 * Een Data Transfer Object om alle gegevens uit de JSON's in Postman te 'ontcijferen' en te gebruiken in code.
 */
public class GameRequestDTO {

    private double bet;
    private String move;
    private long gameID;
    private CardRank cardRank;
    private CardSuit cardSuit;

    public GameRequestDTO() {

    }

    public double getBet() {
        return bet;
    }

    public long getGameID() {
        return gameID;
    }

    public String getMove() {
        return move;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }
}