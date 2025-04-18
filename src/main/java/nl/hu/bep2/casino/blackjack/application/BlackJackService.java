package nl.hu.bep2.casino.blackjack.application;

import jakarta.transaction.Transactional;
import nl.hu.bep2.casino.blackjack.data.BlackJackRepository;
import nl.hu.bep2.casino.blackjack.domain.cards.CardSuit;
import nl.hu.bep2.casino.blackjack.domain.cards.CardRank;
import nl.hu.bep2.casino.blackjack.domain.exception.NoObjectFoundException;
import nl.hu.bep2.casino.blackjack.domain.game.Game;
import nl.hu.bep2.casino.blackjack.domain.game.Move;
import nl.hu.bep2.casino.blackjack.domain.speler.AdminDecorator;
import nl.hu.bep2.casino.blackjack.domain.speler.Speler;
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
        Speler speler = new Speler(playerName, bet);
        Game game = new Game(PLAYING, speler);
        game.start();
        blackJackRepository.save(game);
        if (game.gameIsGeeindigd()) {
            double verkregenBet = game.getAantalVerkregenGeld();
            chipsService.depositChips(playerName, (long) verkregenBet);
        }
        return game;
    }

    public Game selectMove(Move move, long gameID) {
        Optional<Game> gevondenGame = blackJackRepository.findById(gameID);
        if (gevondenGame.isPresent()) {
            Game game = gevondenGame.get();
            game.selectMove(move);
            return game;
        }
        throw new NoObjectFoundException("Er is geen game-object gevonden met game-id: " + gameID);
    }

    public Game getGameResults(long gameID) {
        Optional<Game> gevondenGame = blackJackRepository.findById(gameID);
        if (gevondenGame.isPresent()) {
            return gevondenGame.get();
        }
        throw new NoObjectFoundException("Er is geen game-object gevonden met game-id: " + gameID);
    }

    public Game removeCard(long gameID, CardRank cardRank, CardSuit cardSuit) {
        Optional<Game> gevondenGame = blackJackRepository.findById(gameID);
        if (gevondenGame.isPresent()) {
            Game game = gevondenGame.get();
            Speler speler = game.getSpeler();
            if (SpelerAuthorizatieService.isAdmin(speler)) {
                AdminDecorator adminSpeler = new AdminDecorator(speler);
                adminSpeler.removeCard(cardSuit, cardRank);
            }
            return game;
        }
        throw new NoObjectFoundException("Er is geen game-object gevonden met game-id: " + gameID);
    }

    public void deleteAll() {
        blackJackRepository.deleteAll();
    }
}