package nl.hu.bep2.casino.blackjack.domain;
import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.deck.DeckFactorySingleton;
import nl.hu.bep2.casino.blackjack.domain.speler.Speler;
import static jakarta.persistence.CascadeType.ALL;

@Entity
public class Dealer {
    @OneToOne(cascade = ALL)
    private Hand hand = new Hand();
    @OneToOne(cascade = ALL)
    private Deck deck;
    @Id
    @GeneratedValue
    private Long id;

    public Dealer() {
        this.deck = DeckFactorySingleton.getDeckFactory().createDeck();
    }

    public Deck fetchDeck() {
        return deck;
    }

    // deal de kaarten uit
    public void dealCards(Speler speler){
        speler.addCard(deck.drawRandomCard());
        speler.addCard(deck.drawRandomCard());

        Card randomCard = deck.drawRandomCard();
        randomCard.changeCardVisibility(false);
        hand.addCard(deck.drawRandomCard());
        hand.addCard(randomCard);
    }

    // als het spel is geindigd krijgt de dealer extra kaarten tot de totale hand waarde
    // van de dealer >= 17 is.
    public void finalCards() {
        while (hand.fetchTotalHandValue() < 17) {
            if (hand.fetchTotalHandValue() >= 17) {
                break;
            } else {
                Card newCard = deck.drawRandomCard();
                newCard.changeCardVisibility(false);
                hand.addCard(newCard);
            }
        }
    }

    public Hand getHand() {
        return hand;
    }

    // draait alle kaarten hun waardes naar boven
    public void trueAllCards() {
        this.hand.trueAllCards();
    }

    public int returnHandValue() {
        return hand.fetchTotalHandValue();
    }
}