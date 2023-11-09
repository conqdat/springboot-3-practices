package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest // This annotation tells Spring Boot to look for an embedded database configuration and to configure JPA for us.
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // This annotation tells Spring Boot to use H2 as the embedded database.
public class PokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        // Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        // Assert
        Assertions.assertEquals(pokemon.getName(), savedPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), savedPokemon.getType());
    }

    @Test
    public void PokemonRepository_GetAll_ReturnAllPokemon() {
        // Arrange
        Pokemon pokemon1 = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        Pokemon pokemon2 = Pokemon.builder()
                .name("Charmander")
                .type("Fire")
                .build();

        pokemonRepository.save(pokemon1);
        pokemonRepository.save(pokemon2);

        // Act
        List<Pokemon> list = pokemonRepository.findAll();

        // Assert
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void PokemonRepository_FindById_ReturnPokemon() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        pokemonRepository.save(pokemon);

        // Act
        Pokemon foundPokemon = pokemonRepository.findById(pokemon.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundPokemon);
        Assertions.assertEquals(pokemon.getName(), foundPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), foundPokemon.getType());
    }

    @Test
    public void PokemonRepository_FindByType_ReturnPokemon() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        pokemonRepository.save(pokemon);

        // Act
        Pokemon foundPokemon = pokemonRepository.findByType(pokemon.getType()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundPokemon);
        Assertions.assertEquals(pokemon.getName(), foundPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), foundPokemon.getType());
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnPokemon() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        pokemonRepository.save(pokemon);

        // Act
        pokemon.setName("Charmander");
        pokemon.setType("Fire");
        Pokemon updatedPokemon = pokemonRepository.save(pokemon);

        // Assert
        Assertions.assertEquals(pokemon.getName(), "Charmander");
        Assertions.assertEquals(pokemon.getType(), updatedPokemon.getType());
    }

    @Test
    public void PokemonRepository_DeletePokemon() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        pokemonRepository.save(pokemon);

        // Act
        pokemonRepository.delete(pokemon);

        // Assert
        Assertions.assertEquals(0, pokemonRepository.findAll().size());
    }
}
