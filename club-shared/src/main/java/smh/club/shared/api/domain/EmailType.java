package smh.club.shared.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * An email type enum
 */
@Getter
public enum EmailType {

    @JsonProperty(Names.HOME)
    Home(0, Names.HOME),

    @JsonProperty(Names.WORK)
    Work(1, Names.WORK),

    @JsonProperty(Names.OTHER)
    Other(2, Names.OTHER);

    private final int code;
    private final String emailTypeName;

    EmailType(int code, String emailTypeName) {
        this.code = code;
        this.emailTypeName = emailTypeName;
    }

    /**
     * returns an email type based on its code.
     */
    public static EmailType of (int code) {
        return Stream.of(EmailType.values())
                .filter(a -> a.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static class Names {
        public final static String HOME = "Home";
        public final static String WORK = "Work";
        public final static String OTHER = "Other";
    }
}
