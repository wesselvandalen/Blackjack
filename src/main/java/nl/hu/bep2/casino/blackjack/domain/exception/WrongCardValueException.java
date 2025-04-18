package nl.hu.bep2.casino.blackjack.domain.exception;

public class WrongCardValueException extends RuntimeException {

    public WrongCardValueException(String message) {
        super(message);
    }

}