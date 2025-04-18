package nl.hu.bep2.casino.blackjack.domain.exception;

public class WrongGameStateException extends RuntimeException {

    public WrongGameStateException(String message) {
        super(message);
    }

}