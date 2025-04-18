package nl.hu.bep2.casino.blackjack.domain.game;

public enum GameState {
    PLAYING("PLAYING"),
    WAITING("WAITING"),
    SURRENDER("SURRENDER"),
    WON("WON"),
    LOST("LOST"),
    PUSH("PUSH"),
    BLACKJACK("BLACKJACK"),
    BUST("BUST");

    private String gameState;

    GameState(String gameState){
        this.gameState = gameState;
    }
}