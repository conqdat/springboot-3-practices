package com.pokemonreview.api.service;


import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_SaveAll_ReturnSavedPokemonDTO() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name(pokemon.getName())
                .type(pokemon.getType())
                .build();

        // Act
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        // Assert
        Assertions.assertNotNull(savedPokemon);
        Assertions.assertEquals(pokemon.getName(), savedPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), savedPokemon.getType());

    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnResponse() {
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse pokemonResponse = pokemonService.getAllPokemon(0, 10);

        Assertions.assertNotNull(pokemonResponse);
    }

    @Test
    public void PokemonService_GetPokemonById_ReturnPokemonDTO() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name(pokemon.getName())
                .type(pokemon.getType())
                .build();

        // Act
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(pokemon));

        PokemonDto foundPokemon = pokemonService.getPokemonById(1);

        // Assert
        Assertions.assertNotNull(foundPokemon);
        Assertions.assertEquals(pokemon.getName(), foundPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), foundPokemon.getType());
    }

    @Test
    public void PokemonService_UpdatePokemon_ReturnPokemonDTO() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        PokemonDto pokemonDto = PokemonDto.builder()
                .name(pokemon.getName())
                .type(pokemon.getType())
                .build();

        // Act
        when(pokemonRepository.findById(1)).thenReturn(java.util.Optional.of(pokemon));
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto foundPokemon = pokemonService.updatePokemon(pokemonDto, 1);

        // Assert
        Assertions.assertNotNull(foundPokemon);
        Assertions.assertEquals(pokemon.getName(), foundPokemon.getName());
        Assertions.assertEquals(pokemon.getType(), foundPokemon.getType());
    }

    @Test
    public void PokemonService_DeletePokemonById_ReturnPokemonDTO() {
        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        // Act
        when(pokemonRepository.findById(1)).thenReturn(java.util.Optional.of(pokemon));
        pokemonService.deletePokemonId(1);

        // Assert
        assertAll(() -> pokemonService.deletePokemonId(1));
    }
}
