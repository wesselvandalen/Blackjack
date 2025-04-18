package nl.hu.bep2.casino.blackjack.domain.speler;

import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;
import nl.hu.bep2.casino.blackjack.domain.deck.Deck;
import nl.hu.bep2.casino.blackjack.domain.exception.CardListException;
import nl.hu.bep2.casino.blackjack.domain.exception.NoObjectFoundException;

import java.util.List;

public class AdminDecorator implements SpelerInterface {

    private Speler speler;

    public AdminDecorator(Speler speler) {
        this.speler = speler;
    }

    public void removeCard(CardSuit cs, CardRank cr) {
        List<Card> alleKaartenVanSpeler = speler.showCards();
        boolean kaartGevonden = false;

        if (alleKaartenVanSpeler.size() == 1) {
            throw new CardListException("De speelkaartenlijst bevat nog maar één kaart. U kunt daarom geen kaart verwijderen. HINT: Pak eerst een kaart voordat u kaarten wil wegdoen!");
        }

        for (Card card : alleKaartenVanSpeler) {
            if (card.getCardSuit().equals(cs) && card.getCardRank().equals(cr)) {
                kaartGevonden = true;
                speler.getHand().removeCard(card);
            }
        }

        if (!kaartGevonden) {
            throw new NoObjectFoundException("Er is geen kaart gevonden met CardSuit: " + cs + " en CardRank: " + cr);
        }
    }

    @Override
    public void addCard(Card card) {
        this.speler.getHand().addCard(card);
    }

    @Override
    public Hand hit(Deck deck){
        this.speler.getHand().addCard(deck.drawRandomCard());
        return this.speler.getHand();
    }

    @Override
    public void doubleDown(Deck deck) {
        this.speler.verdubbelBet();
        hit(deck);
    }
}