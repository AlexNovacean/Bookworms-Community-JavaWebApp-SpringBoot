package ro.sci.bookwormscommunity.service;

import java.io.PrintWriter;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import ro.sci.bookwormscommunity.model.Book;

public class WriteDataToCsvBooks {

    /**
     * Way 1
     */
    public static void writeDataToCsvBooks(PrintWriter writer, List<Book> books) {
        String[] CSV_HEADER = { "ID", "Book Name", "Author Name", "Number Of Pages", "Type", "Language", "Description", "Condition", "Sell Price", "Rent Price"  };
        try (
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ){
            csvWriter.writeNext(CSV_HEADER);

            for (Book book: books) {
                String[] data = {
                        book.getId().toString(),
                        book.getAuthorName(),
                        book.getAuthorName(),
                        String.valueOf(book.getNumberOfPages()),
                        book.getType(),
                        book.getLanguage(),
                        book.getDescription(),
                        book.getCondition(),
                        String.valueOf(book.getSellPrice()),
                        String.valueOf(book.getRentPrice())


                };

                csvWriter.writeNext(data);
            }

            System.out.println("Write CSV using CSVWriter successfully!");
        }catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }

    /**
     * Way 2
     */
    public static void writeDataToCsvWithListObjects(PrintWriter writer,List<Book> books) {
        String[] CSV_HEADER = { "ID", "Book Name", "Author Name", "Number Of Pages", "Type", "Language", "Description", "Condition", "Book Rent", "Book Sell", "Sell Price", "Rent Price" };
        StatefulBeanToCsv<Book> beanToCsv = null;
        try (
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ){
            csvWriter.writeNext(CSV_HEADER);

            // write List of Objects
            ColumnPositionMappingStrategy<Book> mappingStrategy =
                    new ColumnPositionMappingStrategy<Book>();

            mappingStrategy.setType(Book.class);
            mappingStrategy.setColumnMapping(CSV_HEADER);

            beanToCsv = new StatefulBeanToCsvBuilder<Book>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(books);

            System.out.println("Write CSV using BeanToCsv successfully!");
        }catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }
}