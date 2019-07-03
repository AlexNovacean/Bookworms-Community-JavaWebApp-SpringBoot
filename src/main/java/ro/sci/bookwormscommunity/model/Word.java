package ro.sci.bookwormscommunity.model;

/**
 * POJO class used in taking the searchPatter from the user input to the methods managing the search.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see ro.sci.bookwormscommunity.service.BookServiceImpl#searchForBookName(String)
 * @see ro.sci.bookwormscommunity.service.BookServiceImpl#searchForAuthors(String)
 * @see ro.sci.bookwormscommunity.service.BookServiceImpl#searchForBookType(String)
 */
public class Word {

    private String searchPattern;

    public String getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(String searchPattern) {
        this.searchPattern = searchPattern;
    }
}
