package ro.sci.bookwormscommunity.model;

public enum BookCondition {
    AS_NEW("As new"), //the book is in the state that it should have been in when it was left by the publisher
    FINE("Fine"), //the book is "as new", but allowing for the normal effects of time on an unused book that has been protected
    VERY_GOOD("Very Good"), //describes a book that is worn, but untorn (any defects must be noted)
    GOOD("Good"), //describes the condition of an average used worn book that is complete (any defects must be noted)
    FAIR("Fair"), //shows wear and tear, but all the text pages and illustrations are present (it may lack end papers, half-title and even the title page)(any defects must be noted)
    POOR("Poor"); //describes a book  that has the complete text, but is so damaged that is only of interest to a buyer who seeks a reading copy


    private String condition;

    BookCondition(final String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
