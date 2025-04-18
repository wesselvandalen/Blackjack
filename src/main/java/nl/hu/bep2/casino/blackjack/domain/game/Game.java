package nl.hu.bep2.casino.blackjack.domain.game;

import jakarta.persistence.*;
import nl.hu.bep2.casino.blackjack.domain.*;
import nl.hu.bep2.casino.blackjack.domain.exception.WrongGameStateException;
import nl.hu.bep2.casino.blackjack.domain.speler.Speler;
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
    private double aantalVerkregenGeld;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "speler_id")
    private Speler speler;
    @OneToOne(cascade = ALL)
    @JoinColumn(name = "dealer_id")
    private Dealer dealer = new Dealer();

    public Game(GameState gameState, Speler speler){
        this.gameState = gameState;
        this.speler = speler;
    }
    public Game() {}

    public void start() {
        dealer.dealCards(speler);
        calculateFirstState();
    }

    public void calculateFirstState() {
        int spelerHandValue = speler.returnHandValue();
        int dealerHandValue = dealer.returnHandValue();
        int maximaleScore = 21;

        if (spelerHandValue > maximaleScore && speler.getHand().ACEFix()) {
            spelerHandValue = speler.returnHandValue();
        } if (spelerHandValue > maximaleScore) {
            uitslag(BUST, 0.0, true);
        } else if (spelerHandValue == maximaleScore && dealerHandValue != maximaleScore) {
            uitslag(BLACKJACK, 1.50, false);
        } else if (spelerHandValue != maximaleScore && dealerHandValue == maximaleScore) {
            uitslag(LOST, 0.00, true);
        } else if (spelerHandValue == dealerHandValue && spelerHandValue == maximaleScore) {
            uitslag(PUSH, 1.00, false);
        }
    }

    public void selectMove(Move move){
        if (!gameState.equals(PLAYING)) {
            throw new WrongGameStateException("Game state is niet 'PLAYING'. Game is al beindigd.");
        } else {
            switch (move) {
                case HIT -> {
                    speler.hit(dealer.fetchDeck());
                    calculateWinner(true);
                }
                case STAND -> {
                    dealer.finalCards();
                    calculateWinner(false);
                }
                case DOUBLEDOWN -> {
                    speler.doubleDown(dealer.fetchDeck());
                    dealer.finalCards();
                    calculateWinner(false);
                }
                case SURRENDER -> {
                    dealer.finalCards();
                    uitslag(SURRENDER, 0.5, false);
                }
                default -> throw new IllegalArgumentException("Ongeldige zet: Move " + move + " bestaat niet.");
            }
        }
    }

    public void calculateWinner(boolean meerdereZettenMogelijk) {
        int spelerHandValue = speler.returnHandValue();
        int dealerHandValue = dealer.returnHandValue();
        int maximaleScore = 21;

        if (spelerHandValue > maximaleScore && dealerHandValue > maximaleScore) {
            uitslag(PUSH, 1.00, false);
        }
        if (spelerHandValue > maximaleScore) {
            if (speler.getHand().handContainsACE()) {
                if (speler.getHand().ACEFix()) {
                    calculateWinner(true);
                } else {
                    uitslag(BUST, 0.0, true);
                }
            } else {
                uitslag(BUST, 0.0, true);
            }
            return;
        }
        if (dealerHandValue > maximaleScore) {
            if (dealer.getHand().handContainsACE()) {
                if (dealer.getHand().ACEFix()) {
                    calculateWinner(true);
                } else {
                    uitslag(WON, 2.0, false);
                }
            } else {
                uitslag(WON, 2.0, false);
            }
            return;
        }
        if (meerdereZettenMogelijk) {
            if (spelerHandValue > maximaleScore) {
                uitslag(BUST, 0.0, true);
            } else if (spelerHandValue == dealerHandValue && spelerHandValue >= maximaleScore) {
                uitslag(PUSH, 1.00, false);
            } else if (spelerHandValue == maximaleScore && dealerHandValue != maximaleScore) {
                uitslag(WON, 1.50, false);
            } else if (spelerHandValue != maximaleScore && dealerHandValue == maximaleScore) {
                uitslag(LOST, 0.00, true);
            }
        } else {
            if (spelerHandValue > maximaleScore) {
                uitslag(BUST, 0.0, true);
            } else if (dealerHandValue <= maximaleScore && dealerHandValue > spelerHandValue) {
                uitslag(LOST, 0.00, true);
            } else if (spelerHandValue <= maximaleScore && spelerHandValue > dealerHandValue) {
                uitslag(WON, 2.00, false);
            } else if (spelerHandValue == dealerHandValue) {
                uitslag(PUSH, 1.00, false);
            }
        }
    }

    public void uitbetaling(double factor, boolean noMoney){
        if (!noMoney) {
            this.aantalVerkregenGeld = speler.getBet()*factor;
        } else {
            this.aantalVerkregenGeld = 0.00;
        }
    }

    public boolean gameIsGeeindigd(){
        if (this.gameState.equals(BUST) || this.gameState.equals(LOST) || this.gameState.equals(WON) || this.gameState.equals(SURRENDER) || this.gameState.equals(BLACKJACK) || this.gameState.equals(PUSH)) {
            return true;
        } return false;
    }

    public void uitslag(GameState gameState, double factor, boolean noMoney){
        this.gameState = gameState;
        uitbetaling(factor, noMoney);
        dealer.trueAllCards();
    }

    public GameState getGameState() {return gameState;}

    public Speler getSpeler() {return speler;}

    public Dealer getDealer() {return dealer;}

    public double getAantalVerkregenGeld() {return aantalVerkregenGeld;}

    public long getGameID() {return gameID;}
}