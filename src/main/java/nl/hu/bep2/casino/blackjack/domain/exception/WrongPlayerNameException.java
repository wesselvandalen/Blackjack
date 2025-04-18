package nl.hu.bep2.casino.blackjack.domain.exception;

public class WrongPlayerNameException extends RuntimeException {

    public WrongPlayerNameException(String message) {
        super(message);
    }

}