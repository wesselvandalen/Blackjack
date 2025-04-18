package nl.hu.bep2.casino.blackjack.domain.game;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.*;
import nl.hu.bep2.casino.blackjack.domain.exception.WrongGameStateException;
import nl.hu.bep2.casino.blackjack.domain.speler.Player;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static nl.hu.bep2.casino.blackjack.domain.game.GameState.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long gameID;
    @Enumerated
    private GameState gameState;
    private double receivedMoney;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "player_id")
    private Player player;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "dealer_id")
    private Dealer dealer = new Dealer();

    public Game(GameState gameState, Player player){
        this.gameState = gameState;
        this.player = player;
    }
    public Game() {}

    public void start() {
        dealer.dealCards(player);
        calculateFirstState();
    }

    public void calculateFirstState() {
        int playerHandValue = player.returnHandValue();
        int dealerHandValue = dealer.returnHandValue();
        int maxScore = 21;

        if (playerHandValue > maxScore && player.getHand().ACEFix()) {
            playerHandValue = player.returnHandValue();
        } if (playerHandValue > maxScore) {
            result(BUST, 0.0, true);
        } else if (playerHandValue == maxScore && dealerHandValue != maxScore) {
            result(BLACKJACK, 1.50, false);
        } else if (playerHandValue != maxScore && dealerHandValue == maxScore) {
            result(LOST, 0.00, true);
        } else if (playerHandValue == dealerHandValue && playerHandValue == maxScore) {
            result(PUSH, 1.00, false);
        }
    }

    public void selectMove(Move move){
        if (!gameState.equals(PLAYING)) {
            throw new WrongGameStateException("Gamestate er ikke 'PLAYING'. Spillet er allerede over.");
        } else {
            switch (move) {
                case HIT -> {
                    player.hit(dealer.fetchDeck());
                    calculateWinner(true);
                }
                case STAND -> {
                    dealer.finalCards();
                    calculateWinner(false);
                }
                case DOUBLEDOWN -> {
                    player.doubleDown(dealer.fetchDeck());
                    dealer.finalCards();
                    calculateWinner(false);
                }
                case SURRENDER -> {
                    dealer.finalCards();
                    result(SURRENDER, 0.5, false);
                }
                default -> throw new IllegalArgumentException("Ugyldig move: Move " + move + " finnes ikke.");
            }
        }
    }

    public void calculateWinner(boolean moreSetsPossible) {
        int playerHandValue = player.returnHandValue();
        int dealerHandValue = dealer.returnHandValue();
        int maxScore = 21;

        if (playerHandValue > maxScore && dealerHandValue > maxScore) {
            result(PUSH, 1.00, false);
        }
        if (playerHandValue > maxScore) {
            if (player.getHand().handContainsACE()) {
                if (player.getHand().ACEFix()) {
                    calculateWinner(true);
                } else {
                    result(BUST, 0.0, true);
                }
            } else {
                result(BUST, 0.0, true);
            }
            return;
        }
        if (dealerHandValue > maxScore) {
            if (dealer.getHand().handContainsACE()) {
                if (dealer.getHand().ACEFix()) {
                    calculateWinner(true);
                } else {
                    result(WON, 2.0, false);
                }
            } else {
                result(WON, 2.0, false);
            }
            return;
        }
        if (moreSetsPossible) {
            if (playerHandValue > maxScore) {
                result(BUST, 0.0, true);
            } else if (playerHandValue == dealerHandValue && playerHandValue >= maxScore) {
                result(PUSH, 1.00, false);
            } else if (playerHandValue == maxScore && dealerHandValue != maxScore) {
                result(WON, 1.50, false);
            } else if (playerHandValue != maxScore && dealerHandValue == maxScore) {
                result(LOST, 0.00, true);
            }
        } else {
            if (playerHandValue > maxScore) {
                result(BUST, 0.0, true);
            } else if (dealerHandValue <= maxScore && dealerHandValue > playerHandValue) {
                result(LOST, 0.00, true);
            } else if (playerHandValue <= maxScore && playerHandValue > dealerHandValue) {
                result(WON, 2.00, false);
            } else if (playerHandValue == dealerHandValue) {
                result(PUSH, 1.00, false);
            }
        }
    }

    public void pay(double factor, boolean noMoney){
        if (!noMoney) {
            this.receivedMoney = player.getBet()*factor;
        } else {
            this.receivedMoney = 0.00;
        }
    }

    public boolean gameIsOver(){
        if (this.gameState.equals(BUST) || this.gameState.equals(LOST) || this.gameState.equals(WON) || this.gameState.equals(SURRENDER) || this.gameState.equals(BLACKJACK) || this.gameState.equals(PUSH)) {
            return true;
        } return false;
    }

    public void result(GameState gameState, double factor, boolean noMoney){
        this.gameState = gameState;
        pay(factor, noMoney);
        dealer.trueAllCards();
    }

    public GameState getGameState() {return gameState;}

    public Player getSpeler() {return player;}

    public Dealer getDealer() {return dealer;}

    public double getReceivedMoney() {return receivedMoney;}

    public long getGameID() {return gameID;}
}