package nl.hu.bep2.casino.blackjack.domain.speler;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;

import java.util.ArrayList;

@Entity
public class Player implements PlayerInterface {

    private String name;
    private double bet;
    @OneToOne(cascade = CascadeType.ALL)
    private Hand hand = new Hand();
    @Id
    @GeneratedValue
    private Long id;

    public Player(String naam, double bet) {
        this.name = naam;
        this.bet = bet;
    }

    public Player() {}

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
        doubleBet();
        hit(deck);
    }

    public void doubleBet() {
        this.bet *= 2;
    }

    public ArrayList<Card> showCards() {
        return this.hand.getCards();
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

    public String getName() {
        return this.name;
    }
}