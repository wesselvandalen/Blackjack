package nl.hu.bep2.casino.blackjack.domain.deck;

public class DeckFactorySingleton {

    private static DeckFactory deckFactory;

    public static void setDeckFactory(DeckFactory factory) {
        deckFactory = factory;
    }

    public static DeckFactory getDeckFactory() {
        if (deckFactory == null) {
            deckFactory = new StandardDeckFactory();
        }
        return deckFactory;
    }
}