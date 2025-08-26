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
        String arschloch = "<p>Du bist ein Arschloch.</p>";
        assertThat(profanityFilter.containsProfanity(arschloch), is(true));
        String asshole = "<p>You are an asshole.</p>";
        assertThat(profanityFilter.containsProfanity(asshole), is(true));
        String good = "<p>You are a good person.</p>";
        assertThat(profanityFilter.containsProfanity(good), is(false));
    }

    @Test
    void profanityCensorTest() {
        String arschloch = "<p>Du bist ein Arschloch.</p>";
        String arschlochCensored = profanityFilter.censor(arschloch);
        assertThat(arschlochCensored, is("<p>Du bist ein *********.</p>"));
        String asshole = "<p>You are an asshole.</p>";
        String assholeCensored = profanityFilter.censor(asshole);
        assertThat(assholeCensored, is("<p>You are an *******.</p>"));
        String good = "<p>You are a good person.</p>";
        String goodCensored = profanityFilter.censor(good);
        assertThat(goodCensored, is(good));
    }
}
