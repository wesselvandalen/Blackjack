package nl.hu.bep2.casino.blackjack.domain.speler;

import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;

public interface SpelerInterface {
    void addCard(Card card);
    Hand hit(Deck deck);
    void doubleDown(Deck deck);
}