package de.unistuttgart.iste.meitrex.common.profanity_filter;

import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfanityService {

    private final Trie trie;
    private final Set<String> allowlist = Set.of("Scunthorpe", "Mishit");

    public ProfanityService(@Value("classpath:profanity/de.txt") Resource de,
                            @Value("classpath:profanity/en.txt") Resource en) throws IOException {
        Set<String> words = new HashSet<>();
        words.addAll(readLines(de));
        words.addAll(readLines(en));

        Trie.TrieBuilder builder = Trie.builder().onlyWholeWords().ignoreCase();
        words.forEach(builder::addKeyword);
        this.trie = builder.build();
    }

    private static Set<String> readLines(Resource resource) throws IOException {
        if (!resource.exists()) {
            log.error("Resource {} does not exist", resource);
            return Set.of();
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return br.lines()
                    .map(line -> line.replace("\uFEFF", ""))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                    .collect(Collectors.toSet());
        }
    }

    public boolean containsProfanity(String inputRaw) {
        String input = normalize(inputRaw);
        for (String ok : allowlist) if (inputRaw.contains(ok)) return false;
        return !trie.parseText(input).isEmpty();
    }

    /**
     * Returns the input String with censored nsfw words.
     * @param inputRaw text to be censored
     * @return text with nsfw words censored
     */
    public String censor(String inputRaw) {
        String input = normalize(inputRaw);
        Collection<Emit> emits = trie.parseText(input);
        StringBuilder sb = new StringBuilder(inputRaw);
        for (Emit e : emits.stream().sorted(Comparator.comparingInt(Emit::getStart).reversed()).toList()) {
            int start = e.getStart(); int end = e.getEnd();
            for (int i = start; i <= end && i < sb.length(); i++) sb.setCharAt(i, '*');
        }
        return sb.toString();
    }

    /**
     * Converts string to lowercase and removes all special carracters and signs
     * @param s input string
     * @return normalized string
     */
    private String normalize(String s) {
        String t = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
        t = t.replace('0','o').replace('1','i').replace('@','a').replace('$','s');
        t = t.replaceAll("[^a-z0-9äöüß ]", "");
        t = t.replaceAll("(.)\\1{2,}", "$1$1");
        return t;
    }
}