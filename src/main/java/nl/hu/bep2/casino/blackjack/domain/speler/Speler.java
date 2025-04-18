package nl.hu.bep2.casino.blackjack.domain.speler;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;

import java.util.ArrayList;

@Entity
public class Speler implements SpelerInterface {

    private String naam;
    private double bet;
    @OneToOne(cascade = CascadeType.ALL)
    private Hand hand = new Hand();
    @Id
    @GeneratedValue
    private Long id;

    public Speler(String naam, double bet) {
        this.naam = naam;
        this.bet = bet;
    }

    public Speler() {}

    @Override
    public void addCard(Card card) {
        this.hand.addCard(card);
    }

    @Override
    public Hand hit(Deck deck){
        this.hand.addCard(deck.drawRandomCard());
        return this.hand;
    }

    @Override
    public void doubleDown(Deck deck) {
        verdubbelBet();
        hit(deck);
    }

    public void verdubbelBet() {
        this.bet *= 2;
    }

    public ArrayList<Card> showCards() {
        return this.hand.getKaarten();
    }

    public int returnHandValue() {
        return hand.fetchTotalHandValue();
    }

    public double getBet() {
        return this.bet;
    }

    public Hand getHand() {
        return this.hand;
    }

    public String getNaam() {
        return this.naam;
    }
}