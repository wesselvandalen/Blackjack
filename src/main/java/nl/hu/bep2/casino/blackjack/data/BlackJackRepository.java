package nl.hu.bep2.casino.blackjack.data;

import jakarta.transaction.Transactional;
import nl.hu.bep2.casino.blackjack.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional
public interface BlackJackRepository extends JpaRepository<Game, Long> {

    @Override
    Optional<Game> findById(Long gameID);

    @Override
    Game save(Game game);

    @Override
    void deleteById(Long id);
}