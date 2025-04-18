package nl.hu.bep2.casino.blackjack.domain.deck;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.Dealer;
import nl.hu.bep2.casino.blackjack.domain.cards.Card;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;
import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.cards.JPAAttributeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Deck implements Serializable {
    @Convert(converter = JPAAttributeConverter.class)
    @Column(length = 2000)
    private List<Card> allCards = new ArrayList<>();
    private Random random;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;
    @Id
    @GeneratedValue
    private Long id;

    public Deck(){
        random = new Random();
        fillDeck();
    }

    public void fillDeck(){
        for (CardSuit cardSuit : CardSuit.values()){
            for (CardRank cardRank : CardRank.values()){
                allCards.add(new Card(cardRank, cardSuit, true));
            }
        }
    }

    public Card drawRandomCard(){
        if (allCards.isEmpty()) { fillDeck(); }

        int randomIndex = random.nextInt(allCards.size());
        Card randomCard = allCards.get(randomIndex);
        allCards.remove(randomCard);
        return randomCard;
    }
}