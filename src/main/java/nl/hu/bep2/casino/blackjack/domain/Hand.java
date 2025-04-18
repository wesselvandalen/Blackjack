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
    private ArrayList<Card> cards = new ArrayList<>();
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
        int totalHandValue = 0;
        for (Card card : cards) {
            if (card.getCardRank() == ACE) {
                totalHandValue += card.fetchACERank();
            } else {
                totalHandValue += card.fetchCardRankValue();
            }
        }
        this.handValue = totalHandValue;
    }

    public ArrayList<Card> showVisibleCards(){
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : cards) {
            if (card.retrieveCardVisibility()) {
                cards.add(card);
            }
        }
        return cards;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void addCard(Card card){
        cards.add(card);
        updateHandValue();
    }

    public int getVisibleHandValue() {
        int visibleHandValue = 0;

        for (Card card : getCards()) {
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
        for (Card card : cards) {
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
        ArrayList<Card> allACECards = new ArrayList<>();
        boolean containsACE = false;

        for (Card card : showVisibleCards()) {
            if (card.getCardRank() == ACE) {
                allACECards.add(card);
                containsACE = true;
            }
        }

        if (containsACE) {
            return allACECards;
        }
        throw new WrongCardValueException("Spillerhånd inneholder ikke noe ACE kort.");
    }

    public void trueAllCards() {
        for (Card card : cards) {
            card.changeCardVisibility(true);
        }
    }

    public ArrayList<Card> getCards() {
        return showVisibleCards();
    }
}