package nl.hu.bep2.casino.blackjack.domain.speler;

import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.exception.CardListException;
import nl.hu.bep2.casino.blackjack.domain.exception.NoObjectFoundException;

import java.util.List;

public class AdminDecorator implements PlayerInterface {

    private Player player;

    public AdminDecorator(Player player) {
        this.player = player;
    }

    public void removeCard(CardSuit cs, CardRank cr) {
        List<Card> allPlayerCards = player.showCards();
        boolean foundCard = false;

        if (allPlayerCards.size() == 1) { 
            throw new CardListException("Du kan ikke fjerne et kort, fordi det er bare ett kort igjen. Ta ett kort først.");
        }

        for (Card card : allPlayerCards) {
            if (card.getCardSuit().equals(cs) && card.getCardRank().equals(cr)) {
                foundCard = true;
                player.getHand().removeCard(card);
            }
        }

        if (!foundCard) {
            throw new NoObjectFoundException("Det er ikke blitt funnet et kort med CardSuit: " + cs + " og CardRank: " + cr);
        }
    }

    @Override
    public void addCard(Card card) {
        this.player.getHand().addCard(card);
    }

    @Override
    public Hand hit(Deck deck){
        this.player.getHand().addCard(deck.drawRandomCard());
        return this.player.getHand();
    }

    @Override
    public void doubleDown(Deck deck) {
        this.player.doubleBet();
        hit(deck);
    }
}