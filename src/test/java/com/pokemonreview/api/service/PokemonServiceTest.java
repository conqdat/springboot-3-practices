package com.pokemonreview.api.service;


import com.pokemonreview.api.dto.PokemonDto;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_SaveAll_ReturnSavedPokemon() {
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
}
