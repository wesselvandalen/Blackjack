# 🃏 Blackjack API – Et kasinospill bygget med Java Spring

Velkommen til Blackjack API – et backend-basert Blackjack-spill utviklet i Java med Spring Boot. Spillet er strukturert etter solide designprinsipper og lar spillere samhandle med spillet gjennom enkle HTTP-forespørsler (f.eks. via Postman eller andre API-verktøy).

## 🚀 Funksjonalitet

Spilleren kan:
- Starte et nytt spill
- Sette en innsats
- Trekke kort (Hit)
- Stå (Stand)
- Se resultatet når spillet er ferdig

Dealeren følger standard Blackjack-regler og agerer automatisk.

## 🧠 Arkitektur og design

Prosjektet er bygget med et lagdelt design (layered architecture), hvor ansvar er tydelig delt opp:

- **Controller Layer**: Håndterer innkommende HTTP-forespørsler
- **Service Layer**: Inneholder spill-logikken
- **Model/Entity Layer**: Representerer kort, hender og spilltilstand
- **Repository Layer** (valgfritt): Dersom du utvider med persistens
- **Exception Handling**: Egne exception-klasser for spill-relaterte feil

### Eksempel: Lage en kortstokk

```java
public void fillDeck() {
    for (CardSuit cardSuit : CardSuit.values()) {
        for (CardRank cardRank : CardRank.values()) {
            allCards.add(new Card(cardRank, cardSuit, true));
        }
    }
}
