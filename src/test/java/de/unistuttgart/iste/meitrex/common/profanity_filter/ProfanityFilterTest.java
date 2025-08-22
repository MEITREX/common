package de.unistuttgart.iste.meitrex.common.profanity_filter;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProfanityFilterTest {

    Resource deResource = new ClassPathResource("profanity/de.txt");

    Resource enResource = new ClassPathResource("profanity/en.txt");

    ProfanityFilter profanityFilter = new ProfanityFilter(deResource, enResource);

    public ProfanityFilterTest() throws IOException {

    }

    @Test
    void profanityCheckerTest() {
        String arschloch = "Du bist ein Arschloch.";
        assertThat(profanityFilter.containsProfanity(arschloch), is(true));
        String asshole = "You are an asshole.";
        assertThat(profanityFilter.containsProfanity(asshole), is(true));
        String good = "You are a good person.";
        assertThat(profanityFilter.containsProfanity(good), is(false));
    }

    @Test
    void profanityCensorTest() {
        String arschloch = "Du bist ein Arschloch.";
        String arschlochCensored = profanityFilter.censor(arschloch);
        assertThat(arschlochCensored, is("Du bist ein *********."));
        String asshole = "You are an asshole.";
        String assholeCensored = profanityFilter.censor(asshole);
        assertThat(assholeCensored, is("You are an *******."));
        String good = "You are a good person.";
        String goodCensored = profanityFilter.censor(good);
        assertThat(goodCensored, is(good));
    }
}
