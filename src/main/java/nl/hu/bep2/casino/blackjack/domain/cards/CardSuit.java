package nl.hu.bep2.casino.blackjack.domain.cards;

public enum CardSuit {

    HEARTS("HEARTS"),
    CLUBS("CLUBS"),
    SPADES("SPADES"),
    DIAMONDS("DIAMONDS");

    private String cardSuit;

    CardSuit(String cardSuit){
        this.cardSuit = cardSuit;
    }

    public String getCardSuit() {
        return cardSuit;
    }
}