package ro.sci.bookwormscommunity.service;

import java.io.PrintWriter;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import ro.sci.bookwormscommunity.model.User;

public class WriteDataToCSV {

    /**
     * Way 1
     */
    public static void writeDataToCsvUsers(PrintWriter writer, List<User> users) {
        String[] CSV_HEADER = { "ID", "First Name", "Last Name", "Email", "Nickname", "Location" };
        try (
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ){
            csvWriter.writeNext(CSV_HEADER);

            for (User user : users) {
                String[] data = {
                        user.getId().toString(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getLocation(),
                        user.getRoles().toString()
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
    public static void writeDataToCsvWithListObjects(PrintWriter writer,List<User> users) {
        String[] CSV_HEADER = { "ID", "First Name", "Last Name", "Email", "Nickname", "Location"};
        StatefulBeanToCsv<User> beanToCsv = null;
        try (
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ){
            csvWriter.writeNext(CSV_HEADER);

            // write List of Objects
            ColumnPositionMappingStrategy<User> mappingStrategy =
                    new ColumnPositionMappingStrategy<User>();

            mappingStrategy.setType(User.class);
            mappingStrategy.setColumnMapping(CSV_HEADER);

            beanToCsv = new StatefulBeanToCsvBuilder<User>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(users);

            System.out.println("Write CSV using BeanToCsv successfully!");
        }catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }
}