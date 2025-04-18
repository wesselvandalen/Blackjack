package nl.hu.bep2.casino.blackjack.domain.exception;

public class NoObjectFoundException extends RuntimeException {

    public NoObjectFoundException(String message) {
        super(message);
    }

}