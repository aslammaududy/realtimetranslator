package com.aslammaududy.realtimetranslator.utility.translator.model;

import org.immutables.value.Value.Immutable;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Model represents information about available languages and translation directions.
 *
 * @author Vladislav Bauer
 */

@SuppressWarnings("serial")
@Immutable
public abstract class Languages implements Serializable {

    /**
     * Get collection of available languages.
     *
     * @return languages
     */
    @Nonnull
    public abstract Set<Language> languages();

    /**
     * Get collection of possible directions.
     * Each direction's language should be presented in {@link #languages()}.
     *
     * @return directions
     */
    @Nonnull
    public abstract Set<Direction> directions();


    /**
     * A factory method to create object using given languages and directions.
     *
     * @param languages collection of languages
     * @param directions collection of directions
     * @return "languages" object
     */
    @Nonnull
    public static Languages of(
        @Nonnull final Collection<Language> languages,
        @Nonnull final Collection<Direction> directions
    ) {
        return ImmutableLanguages.builder()
            .languages(languages)
            .directions(directions)
            .build();
    }

}
