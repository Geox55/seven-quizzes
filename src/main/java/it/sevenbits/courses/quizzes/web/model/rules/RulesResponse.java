package it.sevenbits.courses.quizzes.web.model.rules;

import java.util.Objects;

/**
 * rules response
 */
public class RulesResponse {
    private String rules;

    /**
     * constructor
     * @param rules - rule
     */

    public RulesResponse(final String rules) {
        this.rules = rules;
    }

    /**
     * get rules
     * @return String
     */

    public String getRules() {
        return rules;
    }

    /**
     * set rule
     * @param rules - rules
     */

    public void setRules(final String rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RulesResponse that = (RulesResponse) o;
        return Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }
}
