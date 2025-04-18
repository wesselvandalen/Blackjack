package nl.hu.bep2.casino.blackjack.application;

import jakarta.transaction.Transactional;
import nl.hu.bep2.casino.blackjack.data.BlackJackRepository;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;
import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.exception.NoObjectFoundException;
import nl.hu.bep2.casino.blackjack.domain.game.Game;
import nl.hu.bep2.casino.blackjack.domain.game.Move;
import nl.hu.bep2.casino.blackjack.domain.speler.AdminDecorator;
import nl.hu.bep2.casino.blackjack.domain.speler.Player;
import nl.hu.bep2.casino.blackjack.domain.speler.SpelerAuthorizatieService;
import nl.hu.bep2.casino.chips.application.ChipsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nl.hu.bep2.casino.blackjack.domain.game.GameState.*;

@Transactional
@Service
public class BlackJackService {

    private ChipsService chipsService;
    private final BlackJackRepository blackJackRepository;

    public BlackJackService(ChipsService chipsService, BlackJackRepository blackJackRepository) {
        this.chipsService = chipsService;
        this.blackJackRepository = blackJackRepository;
    }

    public Game startGame(String playerName, double bet){
        chipsService.withdrawChips(playerName, (long) bet);
        Player player = new Player(playerName, bet);
        Game game = new Game(PLAYING, player);
        game.start();
        blackJackRepository.save(game);
        if (game.gameIsOver()) {
            double receivedBet = game.getReceivedMoney();
            chipsService.depositChips(playerName, (long) receivedBet);
        }
        return game;
    }

    public Game selectMove(Move move, long gameID) {
        Optional<Game> foundGame = blackJackRepository.findById(gameID);
        if (foundGame.isPresent()) {
            Game game = foundGame.get();
            game.selectMove(move);
            return game;
        }
        throw new NoObjectFoundException("Ingen spill ble funnet med id : " + gameID);
    }

    public Game getGameResults(long gameID) {
        Optional<Game> foundGame = blackJackRepository.findById(gameID);
        if (foundGame.isPresent()) {
            return foundGame.get();
        }
        throw new NoObjectFoundException("Ingen spill ble funnet med id : " + gameID);
    }

    public Game removeCard(long gameID, CardRank cardRank, CardSuit cardSuit) {
        Optional<Game> foundGame = blackJackRepository.findById(gameID);
        if (foundGame.isPresent()) {
            Game game = foundGame.get();
            Player player = game.getSpeler();
            if (SpelerAuthorizatieService.isAdmin(player)) {
                AdminDecorator adminSpeler = new AdminDecorator(player);
                adminSpeler.removeCard(cardSuit, cardRank);
            }
            return game;
        }
        throw new NoObjectFoundException("Ingen spill ble funnet med id : " + gameID);
    }

    public void deleteAll() {
        blackJackRepository.deleteAll();
    }
}