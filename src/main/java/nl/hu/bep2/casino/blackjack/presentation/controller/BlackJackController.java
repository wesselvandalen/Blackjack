package nl.hu.bep2.casino.blackjack.presentation.controller;

import nl.hu.bep2.casino.blackjack.application.BlackJackService;
import nl.hu.bep2.casino.blackjack.domain.game.Game;
import nl.hu.bep2.casino.blackjack.domain.game.Move;
import nl.hu.bep2.casino.blackjack.presentation.dto.GameRequestDTO;
import nl.hu.bep2.casino.security.domain.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class BlackJackController {

    private final BlackJackService blackJackService;

    public BlackJackController(BlackJackService blackJackService) {
        this.blackJackService = blackJackService;
    }

    @PostMapping
    public Game startGame(Authentication authentication, @RequestBody GameRequestDTO gameRequestDTO){
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return this.blackJackService.startGame(profile.getUsername(), gameRequestDTO.getBet());
    }

    @PatchMapping("/move")
    public Game makeMove(@RequestBody GameRequestDTO gameRequestDTO){
        return this.blackJackService.selectMove(Move.valueOf(gameRequestDTO.getMove()), gameRequestDTO.getGameID());
    }

    @PatchMapping("/removecard")
    public Game removeCard(@RequestBody GameRequestDTO gameRequestDTO) {
        return this.blackJackService.removeCard(gameRequestDTO.getGameID(), gameRequestDTO.getCardRank(), gameRequestDTO.getCardSuit());
    }

    @GetMapping
    public Game getGameResults(@RequestBody GameRequestDTO gameRequestDTO) {
        return this.blackJackService.getGameResults(gameRequestDTO.getGameID());
    }

    @DeleteMapping
    public void deleteAll(){
        this.blackJackService.deleteAll();
    }
}