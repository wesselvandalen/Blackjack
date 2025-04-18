package nl.hu.bep2.casino.blackjack.domain.deck;

public class StandardDeckFactory implements DeckFactory {

    @Override
    public Deck createDeck() {
        return new Deck();
    }

}