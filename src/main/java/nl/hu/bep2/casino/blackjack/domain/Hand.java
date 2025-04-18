package nl.hu.bep2.casino.blackjack.domain;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.cards.JPAAttributeConverter;
import nl.hu.bep2.casino.blackjack.domain.exception.WrongCardValueException;
import java.util.ArrayList;
import static nl.hu.bep2.casino.blackjack.domain.cards.CardRank.ACE;

@Entity
public class Hand {

    @Convert(converter = JPAAttributeConverter.class)
    @Column(length = 2000)
    private ArrayList<Card> kaarten = new ArrayList<>();
    private int handValue;
    @Id
    @GeneratedValue
    private Long id;

    public Hand(){}

    public boolean ACEFix() {
        if (handValue > 21) {
            returnACECard().get(0).switchACEValue();
            updateHandValue();
            if (handValue > 21) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void updateHandValue() {
        int totaleHandValue = 0;
        for (Card card : kaarten) {
            if (card.getCardRank() == ACE) {
                totaleHandValue += card.fetchACERank();
            } else {
                totaleHandValue += card.fetchCardRankValue();
            }
        }
        this.handValue = totaleHandValue;
    }

    public ArrayList<Card> showVisibleCards(){
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : kaarten) {
            if (card.retrieveCardVisibility()) {
                cards.add(card);
            }
        }
        return cards;
    }

    public void removeCard(Card card) {
        kaarten.remove(card);
    }

    public void addCard(Card card){
        kaarten.add(card);
        updateHandValue();
    }

    public int getVisibleHandValue() {
        int visibleHandValue = 0;

        for (Card card : getKaarten()) {
            if (card.retrieveCardVisibility()) {
                if (card.getCardRank() == ACE) {
                    visibleHandValue += card.fetchACERank();
                } else {
                    visibleHandValue += card.fetchCardRankValue();
                }
            }
        }
        return visibleHandValue;
    }

    public int fetchTotalHandValue(){
        int totalValue = 0;
        for (Card card : kaarten) {
            if (card.getCardRank() == ACE) {
                totalValue += card.fetchACERank();
            } else {
                totalValue += card.fetchCardRankValue();
            }
        }
        return totalValue;
    }

    public boolean handContainsACE(){
        for (Card card : showVisibleCards()) {
            if (card.getCardRank() == ACE) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Card> returnACECard(){
        ArrayList<Card> alleACEKaarten = new ArrayList<>();
        boolean containsACE = false;

        for (Card card : showVisibleCards()) {
            if (card.getCardRank() == ACE) {
                alleACEKaarten.add(card);
                containsACE = true;
            }
        }

        if (containsACE) {
            return alleACEKaarten;
        }
        throw new WrongCardValueException("Spelerhand bevat geen ACE kaart.");
    }

    public void trueAllCards() {
        for (Card card : kaarten) {
            card.changeCardVisibility(true);
        }
    }

    public ArrayList<Card> getKaarten() {
        return showVisibleCards();
    }
}