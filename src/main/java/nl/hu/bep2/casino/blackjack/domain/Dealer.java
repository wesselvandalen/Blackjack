package nl.hu.bep2.casino.blackjack.domain;
import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.deck.DeckFactorySingleton;
import nl.hu.bep2.casino.blackjack.domain.speler.Player;
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

    // Del ut kortene
    public void dealCards(Player player){
        player.addCard(deck.drawRandomCard());
        player.addCard(deck.drawRandomCard());

        Card randomCard = deck.drawRandomCard();
        randomCard.changeCardVisibility(false);
        hand.addCard(deck.drawRandomCard());
        hand.addCard(randomCard);
    }

    // Hvis spillet er over, får dealeren ekstra kort frem til totalverdien til dealeren er >= 17.
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

    // Snur alle kort.
    public void trueAllCards() {
        this.hand.trueAllCards();
    }

    public int returnHandValue() {
        return hand.fetchTotalHandValue();
    }
}