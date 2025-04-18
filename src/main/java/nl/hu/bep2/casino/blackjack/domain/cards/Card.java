package nl.hu.bep2.casino.blackjack.domain.cards;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.exception.WrongCardValueException;
import static jakarta.persistence.GenerationType.IDENTITY;
import static nl.hu.bep2.casino.blackjack.domain.cards.CardRank.ACE;

public class Card {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Enumerated
    private CardRank cardRank;
    @Enumerated
    private CardSuit cardSuit;
    private boolean cardIsVisible;
    private int ACERank;

    public Card(CardRank cardRank, CardSuit cardSuit, boolean cardIsVisible){
        this.cardRank = cardRank;
        this.cardSuit = cardSuit;
        this.cardIsVisible = cardIsVisible;
        if (cardRank == ACE) {
            this.ACERank = 11;
        }
    }

    // snur et kort
    public boolean changeCardVisibility(boolean visibility) {
        cardIsVisible = visibility;
        return cardIsVisible;
    }

    public boolean retrieveCardVisibility() {
        return cardIsVisible;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public int fetchCardRankValue() {
        return cardRank.getCardRank();
    }

    public int fetchACERank() {
        return ACERank;
    }

    // endrer verdien på at ACE kort fra 1 til 11 (eller omvendt)
    public void switchACEValue() {
        if (cardRank == ACE) {
            if (ACERank == 11) {
                this.ACERank = 1;
            } else {
                this.ACERank = 11;
            }
        } else {
            throw new WrongCardValueException("Card is not of ACE rank");
        }
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }
}