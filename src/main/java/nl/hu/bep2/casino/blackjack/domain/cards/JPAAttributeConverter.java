package nl.hu.bep2.casino.blackjack.domain.cards;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class JPAAttributeConverter implements AttributeConverter<List<Card>, String> {

    @Override
    public String convertToDatabaseColumn(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return null;
        }

        List<String> cardStrings = cards.stream()
                .map(card -> card.getCardRank() + "_" + card.getCardSuit() + "_" + card.retrieveCardVisibility())
                .collect(Collectors.toList());

        return String.join(",", cardStrings);
    }

    @Override
    public List<Card> convertToEntityAttribute(String cardString) {
        if (cardString == null || cardString.isEmpty()) {
            return Collections.emptyList();
        }

        String[] cardStrings = cardString.split(",");
        List<Card> cards = new ArrayList<>();

        for (String cardStr : cardStrings) {
            String[] parts = cardStr.split("_");
            if (parts.length == 3) {
                CardRank cardRank = CardRank.valueOf(parts[0]);
                CardSuit cardSuit = CardSuit.valueOf(parts[1]);
                boolean isVisible = Boolean.parseBoolean(parts[2]);
                cards.add(new Card(cardRank, cardSuit, isVisible));
            }
        }

        return cards;
    }
}