package de.unistuttgart.iste.meitrex.common.util;

import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.Language;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GraphQlUtil {

    /**
     * This method is used to mark a string as a GraphQl.
     * This is useful for IntelliJ IDEA to provide syntax highlighting and code completion.
     *
     * @param graphQl the GraphQl string
     * @return the GraphQl string
     */
    public static String gql(@Language("GraphQl") final String graphQl) {
        return graphQl;
    }
}
