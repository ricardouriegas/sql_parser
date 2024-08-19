package edu.upvictoria.fpoo.SQL;

import static edu.upvictoria.fpoo.SQL.TokenType.IDENTIFIER;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.nio.file.Path;

import com.xml_parser.DTDParser.DTDInterpreter;
import com.xml_parser.DTDParser.DTDParser;
import com.xml_parser.DTDParser.DTDRestrictions;
import com.xml_parser.XMLParser.*;

// TODO: When creating the XML all the types are saved as type String

public class Table {
    // List[1] <Key, Value> the key is the column name and the value is the value of
    // the row
    // example:
    // table = [
    // {name: "Juan", age: 20},
    // {name: "Pedro", age: 30},
    // {name: "Maria", age: 25}
    // ]
    private List<HashMap<String, Object>> table;
    private List<String> columnNames;
    private HashMap<String, String> columnTypes;

    // ? table constraints
    private String table_primary = null;
    private List<Token> table_foreign = new ArrayList<>();
    private String table_unique = null;
    private Pair<Token, Expression> table_check = new Pair<>(null, null);

    // ? column constraints
    private HashMap<String, ColumnConstraints> columnConstraints = new HashMap<>();

    // ! temporal function to see if there are all the constraints
    public void printConstraints() {
        System.out.println("Table constraints:");
        System.out.println("Primary: " + table_primary);
        System.out.println("Foreign: " + table_foreign);
        System.out.println("Unique: " + table_unique);
        System.out.println("Check: " + table_check);
    }

    public static class ColumnConstraints {
        String primaryKey = null;
        String unique = null;
        String notNull = null;
        Token defaultValue = null;
        Pair<Token, Expression> check = new Pair<>(null, null);
    }

    // compile regex pattern
    private static final Pattern number_pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public Table() {
        // Initialize table, columnNames, and columnTypes
        table = new ArrayList<HashMap<String, Object>>();
        columnNames = new ArrayList<>();
        columnTypes = new HashMap<>();
    }

    // Method to load data from a CSV file into a Table object
    public static Table load(Path csvFile) {
        Table table_obj = new Table();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile.toFile()))) {
            String[] columnNames = reader.readLine().split(",");
            for (String columnName : columnNames) {
                table_obj.columnNames.add(columnName.toUpperCase());
            }
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                HashMap<String, Object> row = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    row.put(table_obj.columnNames.get(i), parseValue(values[i]));
                }
                table_obj.table.add(row);
            }
    
            // Parse XML metadata
            XMLParser parser = new XMLParser();
            XMLTree tree = parser.parse(csvFile.toString().replace(".csv", ".xml"));
            TagNode root = tree.getRoot();
            
            // TODO: validate DTD
    
            // Parse column definitions and constraints within <columns>
            for (TagNode columnsNode : root.getChildren()) {
                if (columnsNode.getName().equals("columns")) {
                    for (TagNode columnNode : columnsNode.getChildren()) {
                        if (columnNode.getName().equals("column")) {
                            String columnName = null;
                            ColumnConstraints constraints = new ColumnConstraints();
    
                            for (TagNode attributeNode : columnNode.getChildren()) {
                                String nodeName = attributeNode.getName();
                                String content = attributeNode.getContent();
    
                                switch (nodeName) {
                                    case "name":
                                        columnName = content;
                                        break;
                                    case "type":
                                        table_obj.columnTypes.put(columnName, content);
                                        break;
                                    case "primary_key":
                                        constraints.primaryKey = content;
                                        table_obj.table_primary = content; // assuming only one primary key
                                        break;
                                    case "unique":
                                        constraints.unique = content;
                                        break;
                                    case "not_null":
                                        constraints.notNull = content;
                                        break;
                                    case "default":
                                        constraints.defaultValue = new Token(IDENTIFIER, content, null, 0, 0, 0);
                                        break;
                                    case "check":
                                        Token checkColumn = new Token(IDENTIFIER, attributeNode.getAttributes().get(0).getValue(), null, 0, 0, 0);
                                        Lexer lexer = new Lexer(attributeNode.getAttributes().get(1).getValue());
                                        Expression checkCondition = new Parser(lexer.scanTokens()).expression();
                                        constraints.check = new Pair<>(checkColumn, checkCondition);
                                        break;
                                }
                            }
    
                            if (columnName != null) {
                                table_obj.columnConstraints.put(columnName, constraints);
                            }
                        }
                    }
                }
            }
    
            // Parse table-level constraints (like foreign keys) outside <columns>
            for (TagNode child : root.getChildren()) {
                if (child.getName().equals("foreign_key")) {
                    List<Token> foreign = new ArrayList<>();
                    for (Attribute attr : child.getAttributes()) {
                        foreign.add(new Token(IDENTIFIER, attr.getValue(), null, 0, 0, 0));
                    }
                    table_obj.table_foreign = foreign;
                }
            }
    
        } catch (IOException e) {
            ErrorHandler.error("Error reading .csv file: " + e.getMessage());
        } catch (Exception e) {
            ErrorHandler.error("Error reading .xml file: " + e.getMessage());
        }
    
        return table_obj;
    }
    
    private static Object parseValue(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        // Evaluate if the value is a number
        else if (number_pattern.matcher(value).matches()) {
            return Double.parseDouble(value);
        }
        // In the rare case of not being able to
        // parse the value, return it as is
        else {
            return null;
        }
    }

    // Method to write data to a CSV file
    public void writeToCSV(Path file) {
        // verify theres not amissing row with

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {

            // Write the column names
            writer.write(String.join(",", columnNames));

            // Write the rows
            for (HashMap<String, Object> row : table) {
                writer.newLine();
                List<String> values = new ArrayList<>();
                for (String columnName : columnNames) {
                    Object value = row.get(columnName);
                    if (value == null) {
                        values.add(null);
                    } else if (value instanceof String) {
                        values.add("\"" + value + "\"");
                    } else {
                        values.add(value.toString());
                    }
                }
                writer.write(String.join(",", values));
            }
        } catch (SecurityException e) {
            throw new RuntimeException("The program does not have permission to write the database .csv file", e);
        } catch (IOException e) {
            throw new RuntimeException("Error writing .csv file", e);
        }
    }

    // Method to write metadata to a TABLE.xml file
    public void writeToMeta(Path file) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toString().replace(".csv", ".xml")))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE table SYSTEM \".DATABASE.dtd\">\n\n");
            writer.write("<table>\n");

            writer.write("\t<columns>\n");
            for (String columnName : columnNames) {
                writer.write("\t<column>\n");
                writer.write("\t\t<name>" + columnName + "</name>\n");
                writer.write("\t\t<type>" + columnTypes.get(columnName) + "</type>\n");

                ColumnConstraints constraints = columnConstraints.get(columnName);
                if (constraints != null) {
                    if (constraints.primaryKey != null) {
                        writer.write("\t\t<primary_key>" + constraints.primaryKey + "</primary_key>\n");
                    }
                    if (constraints.unique != null) {
                        writer.write("\t\t<unique>" + constraints.unique + "</unique>\n");
                    }
                    if (constraints.notNull != null) {
                        writer.write("\t\t<not_null>" + constraints.notNull + "</not_null>\n");
                    }
                    // TODO: on the XML parser it should be able of parsing the default value even if is written
                    // with quotes, but for now it will be saved without quotes
                    if (constraints.defaultValue != null) {
                        writer.write("\t\t<default>" + constraints.defaultValue.lexeme.replace("\"", "") + "</default>\n");
                    }
                    if (constraints.check.getX() != null && constraints.check.getY() != null) {
                        writer.write("\t\t<check column_name=\"" + constraints.check.getX().lexeme + "\" condition=\""
                                + constraints.check.getY() + "\"/>\n");
                    }
                }
                writer.write("\t</column>\n");
            }
            writer.write("\t</columns>\n");

            // Write table constraints
            if (table_primary != null) {
                writer.write("\t<primary_key>" + table_primary + "</primary_key>\n");
            }

            if (table_foreign != null && table_foreign.size() == 3) {
                writer.write("\t<foreign_key column_name=\"" + table_foreign.get(0).lexeme + "\" table_name=\""
                        + table_foreign.get(1).lexeme + "\" referenced_column=\"" + table_foreign.get(2).lexeme
                        + "\"/>\n");
            }

            if (table_unique != null) {
                writer.write("\t<unique>" + table_unique + "</unique>\n");
            }

            if (table_check.getX() != null && table_check.getY() != null) {
                writer.write(
                        "\t<check column_name=\"" + table_check.getX().lexeme + "\" condition=\"" + table_check.getY()
                                + "\"/>\n");
            }

            writer.write("</table>");
        } catch (SecurityException e) {
            throw new RuntimeException("The program does not have permission to write the database .meta file", e);
        } catch (IOException e) {
            throw new RuntimeException("Error writing .meta file", e);
        }
    }

    // Method to save data to a CSV file and its metadata to a .meta file
    public void save(Path csvFile) {
        try {
            writeToCSV(csvFile);
        } catch (SecurityException e) {
            throw new RuntimeException("The program does not have permission to write the database files");
        } catch (RuntimeException e) {
            throw new RuntimeException("Error writing database files");
        }
    }

    // Method to add column constraints to the table
    public void addColumnConstraint(String columnName, Object... constraint) {
        /*
         * if (check(PRIMARY)) //? String -> <column_name>
         * 
         * if (check(UNIQUE)) //? String -> <column_name>
         * 
         * if (check(NOT)) //? String -> <column_name>
         * 
         * if (check(DEFAULT)) //? Token -> <value>
         * 
         * if (check(CHECK)) //? Pair<Token, Expression> -> <column_name, expression>
         */
        if (!columnConstraints.containsKey(columnName)) {
            columnConstraints.put(columnName, new ColumnConstraints());
        }

        ColumnConstraints constraints = columnConstraints.get(columnName);

        for (Object c : constraint) {
            if (c instanceof String) {
                if (c.equals("PRIMARY KEY")) {
                    constraints.primaryKey = columnName;
                } else if (c.equals("UNIQUE")) {
                    constraints.unique = columnName;
                } else if (c.equals("NOT NULL")) {
                    constraints.notNull = columnName;
                }
            } else if (c instanceof Token) {
                constraints.defaultValue = (Token) c;
            } else if (c instanceof Pair) {
                constraints.check = (Pair<Token, Expression>) c;
            }
        }
    }

    // Method to add table constraints to the table
    public void addTableConstraint(Object... constraint) {
        /*
         * if (check(PRIMARY)) //? String -> <column_name>
         * 
         * if (check(FOREIGN)) //? List<Token> -> <column_name, table_name,
         * referenced_column>
         * 
         * if (check(UNIQUE)) //? String -> <column_name>
         * 
         * if (check(CHECK)) //? Pair<Token, Expression> -> <column_name, expression>
         */
        for (Object c : constraint) {
            if (c instanceof String) { // can be PRIMARY or UNIQUE
                if (c.equals("PRIMARY KEY")) {
                    table_primary = (String) c;
                } else if (c.equals("UNIQUE")) {
                    table_unique = (String) c;
                }
            } else if (c instanceof List) { // FOREIGN
                table_foreign = (List<Token>) c;
            } else if (c instanceof Pair) { // CHECK
                table_check = (Pair<Token, Expression>) c;
            }
        }

    }

    // Method to get the table constraints
    public List<Object> getTableConstraints() {
        List<Object> constraints = new ArrayList<>();
        if (table_primary != null) {
            constraints.add(table_primary);
        }
        if (table_foreign != null) {
            constraints.add(table_foreign);
        }
        if (table_unique != null) {
            constraints.add(table_unique);
        }
        if (table_check.getX() != null && table_check.getY() != null) {
            constraints.add(table_check);
        }
        return constraints;
    }

    // Method to add a row to the table
    public void addRow(HashMap<String, Object> row) {
        // add the row to the table
        table.add(row);
    }

    // Mehot to get the column values
    public List<Object> getColumnValues(String columnName) {
        List<Object> values = new ArrayList<>();
        for (HashMap<String, Object> row : table) {
            values.add(row.get(columnName));
        }
        return values;
    }

    // Method to add a column to the table (for the select)
    public void setColumn(String columnName, List<Object> values) {
        // Check if the column name already exists
        if (columnNames.contains(columnName)) {
            ErrorHandler.error("Column '" + columnName + "' already exists.");
        }

        // Add the column name and data type to the table
        columnNames.add(columnName);
        columnTypes.put(columnName, "String");

        // Add the values to the rows
        for (int i = 0; i < table.size(); i++) {
            table.get(i).put(columnName, values.get(i));
        }
    }

    public void deleteRow(int index) {
        if (index < 0 || index >= table.size()) {
            System.out.println("Index out of bounds: " + index);
            return;
        }
        table.remove(index);
    }

    public void deleteRows(List<Integer> indexes) {
        for (int i = indexes.size() - 1; i >= 0; i--) {
            int index = indexes.get(i);
            if (index < 0 || index >= table.size()) {
                System.out.println("Index out of bounds: " + index);
                continue;
            }
            table.remove(index);
        }
    }

    public void distinct() {
        // create a set to store unique rows
        Set<HashMap<String, Object>> uniqueRows = new HashSet<>();

        // iterate over the rows to find duplicates
        for (HashMap<String, Object> row : table) {
            uniqueRows.add(row);
        }

        // set the table to the unique rows
        table = new ArrayList<>(uniqueRows);
    }

    public List<Object> getColumn(String columnName) {
        // Check if the column name exists
        if (!columnNames.contains(columnName)) {
            ErrorHandler.error("Column '" + columnName + "' does not exist.");
        }

        // Create a list to store the values of the column
        List<Object> column = new ArrayList<>();

        // Get the values of the column
        for (HashMap<String, Object> row : table) {
            column.add(row.get(columnName));
        }

        return column;
    }

    public void removeDuplicates(String columnName) {
        // Check if the column name exists
        if (!columnNames.contains(columnName)) {
            ErrorHandler.error("Column '" + columnName + "' does not exist.");
        }

        // Create a set to store unique values
        Set<Object> uniqueValues = new HashSet<>();

        // Create a list to store the rows to remove
        List<HashMap<String, Object>> rowsToRemove = new ArrayList<>();

        // Iterate over the rows to find duplicates
        for (HashMap<String, Object> row : table) {
            Object value = row.get(columnName);
            if (uniqueValues.contains(value)) {
                rowsToRemove.add(row);
            } else {
                uniqueValues.add(value);
            }
        }

        // Remove the duplicate rows
        table.removeAll(rowsToRemove);
    }

    // Method to delete a column
    public void deleteColumn(String columnName) {
        if (!columnNames.contains(columnName)) {
            return;
        }
        columnNames.remove(columnName);
        columnTypes.remove(columnName);
        for (HashMap<String, Object> row : table) {
            row.remove(columnName);
        }
    }

    // Method to modify a column
    public void modifyColumn(String columnName, String dataType) {
        if (!columnNames.contains(columnName)) 
            ErrorHandler.error("Column '" + columnName + "' does not exist.");
        
        // Modify the data type of the column
        columnTypes.put(columnName, dataType);

    }

    // Method to add a column name and data type to the table
    public void addColumn(String columnName, String dataType) {
        // check if the column name already exists
        if (columnNames.contains(columnName)) {
            ErrorHandler.error("Column '" + columnName + "' already exists.");
        }

        columnNames.add(columnName);
        columnTypes.put(columnName, dataType);
    }

    // Method to update a row in the table
    public void updateRow(HashMap<String, Object> row, int index) {
        if (index < 0 || index >= table.size()) {
            return;
        }
        table.set(index, row);
    }

    public void updateRow(String key, Object value, int index) {
        if (index < 0 || index >= table.size()) {
            return;
        }

        table.get(index).put(key, value);
    }

    // Method to get all rows in the table
    public List<HashMap<String, Object>> getTable() {
        return table;
    }

    // Method to get column names
    public List<String> getColumnNames() {
        return columnNames;
    }

    // Method to get column name by index
    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    // Method to get data type of a column
    public String getColumnType(String columnName) {
        return columnTypes.get(columnName);
    }

    // Method to get row
    public HashMap<String, Object> getRow(int index) {
        return table.get(index);
    }

    // Method get rows (list of rows)
    public List<HashMap<String, Object>> getRows() {
        return table;
    }


    // Method to get a column constraints
    public ColumnConstraints getColumnConstraints(String columnName) {
        return columnConstraints.get(columnName);
    }

    // Mehtod to limit the number of rows
    public void limit(int limit) {
        // handle the case where the limit is greater than the number of rows
        if (limit > table.size()) {
            return;
        }
        table = table.subList(0, limit);
    }

    // Method sort using java's vanilla function
    public void sort(String columnName) {

        // Define a custom comparator to compare rows based on the specified column
        Comparator<HashMap<String, Object>> comparator = (row1, row2) -> {
            Object value1 = row1.get(columnName);
            Object value2 = row2.get(columnName);

            // Handle null values by considering them greater than non-null values
            if (value1 == null && value2 == null) {
                return 0;
            } else if (value1 == null) {
                return 1;
            } else if (value2 == null) {
                return -1;
            }

            // Compare values based on their types
            if (value1 instanceof Comparable && value2 instanceof Comparable) {
                return ((Comparable) value1).compareTo(value2);
            } else {
                throw new IllegalArgumentException("Values in column '" + columnName + "' are not comparable.");
            }
        };

        // Sort the table using the specified comparator
        Collections.sort(table, comparator);
    }

    // Method to sort in reverse order
    public void sortReverse(String columnName) {
        // Define a custom comparator to compare rows based on the specified column
        Comparator<HashMap<String, Object>> comparator = (row1, row2) -> {
            Object value1 = row1.get(columnName);
            Object value2 = row2.get(columnName);

            // Handle null values by considering them greater than non-null values
            if (value1 == null && value2 == null) {
                return 0;
            } else if (value1 == null) {
                return 1;
            } else if (value2 == null) {
                return -1;
            }

            // Compare values based on their types in reverse order
            if (value1 instanceof Comparable && value2 instanceof Comparable) {
                return ((Comparable) value2).compareTo(value1); // Note the reversed order here
            } else {
                throw new IllegalArgumentException("Values in column '" + columnName + "' are not comparable.");
            }
        };

        // Sort the table using the specified comparator
        Collections.sort(table, comparator);
    }

    // Method to delete a row
    public void deleteRow(HashMap<String, Object> rowToDelete) {
        Iterator<HashMap<String, Object>> iterator = table.iterator();
        while (iterator.hasNext()) {
            HashMap<String, Object> row = iterator.next();
            if (row.equals(rowToDelete)) {
                iterator.remove();
                return; // Row found and deleted, exit the method
            }
        }
        // If the row is not found, throw an exception or handle it as needed
        throw new IllegalArgumentException("Row not found in the table.");
    }

    // Method to print the table
    public void print() {
        System.out.println(columnNames);
        for (HashMap<String, Object> row : table) {
            System.out.println(row);
        }
    }

    // Method to write the column name at the beginning of the table
    public void writeColumnNames(List<String> columnName) {
        // write the column names at the beginning of the table
        columnNames = columnName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(columnNames);
        sb.append("\n");
        for (HashMap<String, Object> row : table) {
            sb.append(row);
            sb.append("\n");
        }
        return sb.toString();
    }

}
