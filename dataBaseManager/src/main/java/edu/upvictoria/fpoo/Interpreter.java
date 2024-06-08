package edu.upvictoria.fpoo;

import static edu.upvictoria.fpoo.TokenType.*;

import java.io.File;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TODO: right now is not necessary to implement things for check the constraints
 * TODO: throw an error when using somthing like "select id from example where max(id) = 1;"
 */
/**
 * This class will interpret the abstract syntax tree generated by the parser
 */
/**
 * Voy a llamar a tabla, tabla debe de recibir un archivo csv, luego debe de
 * transformar el archivo csv en una tabla y menjarla segun la sentencia
 */
public class Interpreter
        implements Clause.Visitor<Void>, Expression.Visitor<Object> {

    // set of aggregation functions
    private static final Set<TokenType> AGGREGATION_FUNCTIONS = new HashSet<TokenType>(Arrays.asList(
            COUNT, MIN, MAX, SUM, AVG));
    // variables
    private Path folder = null;
    Table table;
    HashMap<String, Object> currentRow = null;
    private String result;

    /**
     * Getter and setters
     */
    public Path getDataBase() {
        return folder;
    }
    
    public void setDataBase(Path path) {
        this.folder = path;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Interpret the clause
     * @param clause
     */
    public void interpret(Clause clause) {
        excecute(clause);
    }

    private void excecute(Clause clause) {
        clause.accept(this);
    }

    // Use clause
    @Override
    public Void useClause(Clause.UseClause clause) {
        String path = "";

        // eliminate the quotes
        path = clause.path;
        path = path.replace("\"", "");
        path = path.replace("\'", "");

        if (!path.startsWith("/")) {
            path = System.getProperty("user.dir") + "/" + path;
        }

        // check if the path exists
        Path pathy = Paths.get(path);
        boolean exists = Files.exists(pathy);
        if (!exists) {
            ErrorHandler.error("The path" + clause.path + " does not exist");
        }

        // verify write access to the folder
        if (!Files.isWritable(pathy)) {
            ErrorHandler.error("You do not have write access to the folder");
        }

        // set the path
        // this.path = path;
        this.folder = pathy;
        System.out.println("DataBase path set to: " + folder.toString());
        result = "DataBase path set to: " + folder.toString();

        return null;
    }

    // delete Clause
    @Override
    public Void deleteClause(Clause.DeleteClause clause) {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        Path file = folder.resolve(clause.table_name.lexeme + ".csv");

        if (!file.toFile().exists()) {
            // throw new RuntimeException("The table does not exist");
            ErrorHandler.error(clause.table_name, "The table does not exist");
        }

        table = Table.load(file);
        if (table == null) {
            ErrorHandler.error(clause.table_name, "Error loading the table");
        }

        // delete the row in the table
        visit_where_clause_delete(clause);

        // save the table
        table.save(file);

        result = "Rows deleted";

        return null;
    }

    private void visit_where_clause_delete(Clause.DeleteClause clause) {
        Object list = evaluateClause(clause.where_expression);
        if (!(list instanceof List)) {
            ErrorHandler.error("The WHERE clause must return a list of booleans");
        }

        // filter the table based on the boolean list received from evaluate
        List<Boolean> booleans = (List<Boolean>) list;
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i))
                indexes.add(i);
        }

        // delete the rows
        table.deleteRows(indexes);

    }

    // create Clause
    @Override
    public Void createClause(Clause.CreateClause clause) {
        /**
         * check if the table exists
         * if it does not exist create it
         * if it exists throw an error
         */
        // check that use was called
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        Path file = folder.resolve(clause.name.lexeme + ".csv");
        if (file.toFile().exists()) {
            ErrorHandler.error("The table already exists on " + file.toString());
        }

        // create the table
        Table table = new Table();

        // add the columns
        // clause.columnsDefinition =
        // [1][1] = Column name
        // [1][2] = Column type
        // [1][2 ... n] = Constraints
        // [2][1] = Column name
        // [2][2] = Column type
        // [2][2 ... n] = Constraints
        for (List<String> column : clause.columnsDefinition) {
            table.addColumn(column.get(0), column.get(1));
        }

        // save the table
        table.save(file);

        result = "Table " + clause.name.lexeme + " created";

        return null;
    }

    // insert clause
    @Override
    public Void insertClause(Clause.InsertClause clause) {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        Path file = folder.resolve(clause.token.lexeme + ".csv");

        if (!file.toFile().exists()) {
            ErrorHandler.error(clause.token, "The table does not exist");
        }

        Table table = Table.load(file);
        if (table == null) {
            ErrorHandler.error(clause.token, "Error loading the table");
        }

        // column names to complement the things that are not in the insert
        List<String> columnNames = table.getColumnNames();

        // evaluate and then put into the table
        HashMap<String, Object> row = new HashMap<>();
        for (String key : clause.valuesMap.keySet()) {
            // evaluate the value
            Object value = evaluate(clause.valuesMap.get(key));
            row.put(key, value);
        }

        // check if the row has more columns than the table
        for (String columnName : row.keySet()) {
            if (!columnNames.contains(columnName)) {
                ErrorHandler.error(
                        "The column " + columnName
                                + " does not exist in the table");
            }
        }

        // check if the row has all the columns
        for (String columnName : columnNames) {
            if (!row.containsKey(columnName)) {
                // TODO: when adding constraints we should validate if the column is nullable
                row.put(columnName, null);
            }
        }

        table.addRow(row);

        // save the table
        table.save(file);

        result = "Row inserted";

        return null;
    }

    // update clause
    @Override
    public Void updateClause(Clause.UpdateClause clause) {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        Path file = folder.resolve(clause.table_name.lexeme + ".csv");

        if (!file.toFile().exists()) {
            ErrorHandler.error(clause.table_name, "The table does not exist");
        }

        table = Table.load(file);
        if (table == null) {
            ErrorHandler.error(clause.table_name, "Error loading the table");
        }

        // update the row in the table
        visit_where_clause_update(clause);

        // save the table
        table.save(file);

        result = "Row updated";

        return null;
    }

    private void visit_where_clause_update(Clause.UpdateClause clause) {
        Object list = evaluateClause(clause.where_expression);
        if (!(list instanceof List)) {
            ErrorHandler.error("The WHERE clause must return a list of booleans (should be a comparation)");
        }

        // filter the table based on the boolean list received from evaluate
        List<Boolean> booleans = (List<Boolean>) list;
        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                currentRow = table.getRow(i);
                for (String key : clause.valuesMap.keySet()) {
                    // evaluate the value
                    Object value = evaluate(clause.valuesMap.get(key));

                    // if the value is a string put it in quotes
                    table.updateRow(key, value, i);
                }
            }
        }

        currentRow = null;

    }

    // drop clause
    @Override
    public Void dropClause(Clause.DropClause clause) {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        Path file = folder.resolve(clause.token.lexeme + ".csv");
        if (!file.toFile().exists()) {
            ErrorHandler.error(clause.token, "The table [" + clause.token.lexeme + "] not exist");
        }

        // delete the file
        file.toFile().delete();

        result = "Table " + clause.token.lexeme + " deleted";

        return null;
    }

    // select clause
    @Override
    public Void selectClause(Clause.SelectClause clause) {
        // load table using the from clause
        if (clause.table_name != null)
            visit_from_clause(clause);

        // filter with the where clause
        if (clause.where_expression != null)
            visit_where_clause(clause);

        // order the table using ORDER_BY
        if (clause.columns_order != null)
            visit_order_by_clause(clause.columns_order);

        // select the column on the table using select
        visit_select_clause(clause);

        // limit the table using LIMIT
        if (clause.limit != -1)
            visit_limit_clause(clause);

        // print the table
        table.print();

        result = table.toString();

        // reset the table
        table = null;

        return null;
    }

    private void visit_from_clause(Clause.SelectClause clause) {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        // load table using the from clause
        Path csvFile = folder.resolve(clause.table_name.lexeme + ".csv");

        if (!csvFile.toFile().exists()) {
            ErrorHandler.error(clause.table_name, "The table does not exist");
        }

        Table table = Table.load(csvFile);
        if (table == null) {
            ErrorHandler.error(clause.table_name, "Error loading the table");
        }

        this.table = table;

    }

    /**
     * 
     * @param clause
     */
    private void visit_where_clause(Clause.SelectClause clause) {
        Table partialTable = new Table();
        // List<Boolean> list = evaluate(clause.where_expression);
        Object list = evaluateClause(clause.where_expression);

        if (!(list instanceof List)) {
            ErrorHandler.error("The WHERE clause must return a list of booleans");
        }

        // filter the table based on the boolean list received from evaluate
        List<Boolean> booleans = (List<Boolean>) list;
        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i))
                partialTable.addRow(table.getRows().get(i));
        }

        // write column names
        partialTable.writeColumnNames(table.getColumnNames());

        this.table = partialTable;
    }

    /**
     * VisitOrderBy
     * 
     * @param expr
     * @return
     */
    private void visit_order_by_clause(List<String> columns_order) {
        ArrayList<String> column_order = new ArrayList<String>(columns_order);

        // iterate by 2 bc the list is in this order (column_to_order, ASC/DESC)
        for (int i = 0; i < column_order.size(); i += 2) {
            String column = column_order.get(i);
            String order = column_order.get(i + 1);

            // sort the table
            if (order == "ASC") {
                table.sort(column);
            } else {
                table.sortReverse(column);
            }
        }

    }

    private void visit_limit_clause(Clause.SelectClause clause) {
        // limit the table using LIMIT
        if (clause.limit != -1) {
            table.limit(clause.limit);
        }

    }

    private void visit_select_clause(Clause.SelectClause clause) {
        // defines the columns or expressions you want to include
        // in the final result set.
        // It can reference columns from the FROM clause or
        // results of expressions involving filtering and grouping operations.

        // select is a map with the columns (this columns are a list of expressions
        // becaouse it can be a function call or an expression) and a boolean that
        // indicates if it have the DISTINCT clause
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        if (table == null) { // this means is only going to do an operation
            table = new Table();

            // create empty row with column names
            HashMap<String, Object> row = new HashMap<>();
            for (Pair<Expression, Token> pair : clause.columns.getX()) {
                // row.put(expr.toString(), null);
                if (pair.getY() != null) {
                    row.put(pair.getY().lexeme, null);
                } else {
                    row.put(pair.getX().toString(), null);
                }
            }

            table.addRow(row);

            // add column headers
            table.writeColumnNames(new ArrayList<String>(row.keySet()));

            for (Pair<Expression, Token> pair : clause.columns.getX()) {
                // evaluate result
                Object value = evaluateClause(pair.getX());
                // table.getRow(0).put(expr.toString(), value);
                if (pair.getY() != null) {
                    table.getRow(0).put(pair.getY().lexeme, value);
                } else {
                    table.getRow(0).put(pair.getX().toString(), value);
                }
            }

            return;

        }

        Table aggregate_function_table = new Table();
        Boolean is_aggregate = false;
        Boolean have_distinct = clause.columns.getY();

        // get the evaluations
        Table partial_table = new Table();
        // List<Expression> expressionList = clause.columns.getX();
        List<Pair<Expression, Token>> expressionList = clause.columns.getX();

        // create empty row with column names
        HashMap<String, Object> row = new HashMap<>();
        for (Pair<Expression, Token> pair : expressionList) {
            // if the expression have an alias
            if (pair.getY() != null) {
                row.put(pair.getY().lexeme, null);
            } else {
                row.put(pair.getX().toString(), null);
            }
        }
        // add all the empty rows to the new table
        for (HashMap<String, Object> r : table.getRows()) {
            partial_table.addRow(new HashMap<String, Object>(row));
        }
        // add column headers
        partial_table.writeColumnNames(new ArrayList<String>(row.keySet()));

        // ================== FOR AGGREGATE FUNCTION TABLE ======================
        // set a table with n (number of columns) and with only 1 row
        // to store the results of the aggregate functions
        HashMap<String, Object> row_aggregate = new HashMap<String, Object>();
        aggregate_function_table.addRow(row_aggregate);
        // column headers
        aggregate_function_table.writeColumnNames(new ArrayList<String>(row_aggregate.keySet()));

        for (Pair<Expression, Token> pair : expressionList) {
            // evaluate result
            Object listOfValues = evaluateClause(pair.getX());
            String columnName = pair.getY() != null ? pair.getY().lexeme : pair.getX().toString();
            if (!(listOfValues instanceof List)) { // ? Aggregate function

                // add the value to the table
                row_aggregate.put(columnName, listOfValues);
                // column headers
                aggregate_function_table.writeColumnNames(new ArrayList<String>(row_aggregate.keySet()));

                is_aggregate = true;
            } else { // ? Row-level expressions
                for (int i = 0; i < ((List) listOfValues).size(); i++) {
                    Object value = ((List) listOfValues).get(i);
                    if (value == "*") {
                        if (have_distinct) {
                            ErrorHandler.error("The * operator cannot be used with DISTINCT");
                        }
                        return;
                    }
                    partial_table.getRow(i).put(columnName, value);
                }
            }
        }

        if (have_distinct) {
            // remove duplicates
            partial_table.distinct();
        }

        if (is_aggregate) {
            table = aggregate_function_table;
        } else {
            table = partial_table;
        }
        return;
    }

    /**
     * Evaluate a expression
     * 
     * @param expr
     * @return Object
     */
    private Object evaluateClause(Expression expr) {

        // check if theres a function call
        if (AGGREGATION_FUNCTIONS.contains(expr)) {
            return expr.accept(this);
        }
        if (expr instanceof Expression.FunctionCall &&
                AGGREGATION_FUNCTIONS.contains(
                        ((Expression.FunctionCall) expr).name.type)) {
            return expr.accept(this);
        }

        if (table.getRows().size() == 0) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            table.addRow(row);
        }

        // if is a function call
        List<Object> values = new ArrayList<Object>();
        for (HashMap<String, Object> row : table.getRows()) {
            this.currentRow = row;
            values.add(expr.accept(this));
        }

        return values;
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    /**
     * Show clause
     */
    @Override
    public Void showClause() {
        if (folder == null) {
            ErrorHandler.error("You must USE a database first");
        }

        File folder = new File(this.folder.toString());
        File[] files = folder.listFiles();

        // check if the folder is empty
        if (files.length == 0) {
            ErrorHandler.error("The database (folder) is empty");
        }

        // check for csv's
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            if (file.getName().endsWith(".csv")) {
                sb.append(file.getName().replace(".csv", "") + "\n");
                System.out.println(file.getName().replace(".csv", ""));
            }
        }

        result = sb.toString();

        return null;
    }

    /**
     * Query clause
     * It should return a table with the result of the query
     */
    @Override
    public Void queryClause(Clause.QueryClause clause) {
        // TODO: Implement the query clause

        return null;
    }

    /**************************************************************************/
    /****************************** Expressions Visitors **********************/
    /**************************************************************************/
    // SELECT * FROM tablita WHERE nombre = "Juan" AND edad = 20 OR edad = 30;
    @Override
    public Object visitBinaryExpression(Expression.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left + (double) right;
            case MINUS:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left - (double) right;
            case SLASH:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left / (double) right;
            case DIV:
            if (left == null || right == null)
                    return null;
                // should be integer division
                checkNumberOperands(left, right);
                Double result = (double) left / (double) right;

                if (result % 1 == 0)
                    return result;
                else
                    ErrorHandler.error("DIV can only result in an integer");

            case MOD:
                if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left % (double) right;
            case PORCENTAJE:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left % (double) right;
            case STAR:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left * (double) right;
            case GREATER:
            if (left == null || right == null)
                    ErrorHandler.error("Cannot use null in a comparation");
                checkNumberOperands(left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
            if (left == null || right == null)
                    ErrorHandler.error("Cannot use null in a comparation");
                checkNumberOperands(left, right);
                return (double) left > (double) right;
            case LESS:
            if (left == null || right == null)
                    ErrorHandler.error("Cannot use null in a comparation");
                checkNumberOperands(left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
            if (left == null || right == null)
                    return null;
                checkNumberOperands(left, right);
                return (double) left <= (double) right;
            // TODO: Should work for strings too (i.e. checkStrNumOperands)
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case EQUAL:
                return isEqual(left, right);
            case AND:
                return (boolean) left && (boolean) right;
            case OR:
                return (boolean) left || (boolean) right;
            default:
                ErrorHandler.error("Unknown operator");
        }

        return null;
    }

    /**
     * Checks for the visitBinary expression
     */
    private void checkNumberOperands(Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        ErrorHandler.error("Operands [" + left + ", " + right + "] must be numbers");
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;
        return a.equals(b);
    }

    @Override
    public Object visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        if (expression.isColumnName) {
            // verify if the column exists
            List<String> columnNames = table.getColumnNames();

            if (!columnNames.contains(expression.value)) {
                ErrorHandler.error("The column " + expression.value + " does not exist");
            }

            return currentRow.get(expression.value);
        } else {
            return expression.value;
        }
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        Object right = expression.right.accept(this);

        switch (expression.operator.type) {
            case MINUS:
                // validate if the value is a number
                if (!(right instanceof Double)) {
                    ErrorHandler.error("When using - the value must be a number");
                }
                return -(double) right;
            case BANG:
                // validate if the value is a boolean
                if (!(right instanceof Boolean)) {
                    ErrorHandler.error("When using ! the value must be a boolean");
                }
                return !((boolean) right);
        }

        return null;
    }

    @Override
    public Object visitFunctionCallExpression(Expression.FunctionCall expression) {
        // == Supported functions ==
        // UCASE, FLOOR, ROUND, RAND, COUNT, MIN, MAX, SUM, AVG
        switch (expression.name.type) {
            // scalar functions
            case UCASE:
                return ucase(expression);
            case LCASE:
                return lcase(expression);
            case CAPITALIZE:
                return capitalize(expression);
            case FLOOR:
                return floor(expression);
            case ROUND:
                return round(expression);
            case CEIL:
                return ceil(expression);
            case RAND:
                return rand(expression);
            case COUNT:
                return count(expression);

            // table-valued functions
            case MIN:
                return min(expression);
            case MAX:
                return max(expression);
            case SUM:
                return sum(expression);
            case AVG:
                return avg(expression);
            default:
                ErrorHandler.error(expression.name, "Function does not exist");
                return null;
        }
    }

    /******************************************************************************/
    /********************************* Functions **********************************/
    /******************************************************************************/
    // UCASE: Convert a string to uppercase
    private Object ucase(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "UCASE function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof String)) {
            ErrorHandler.error(expression.name, "UCASE function expects a string argument");
            return null;
        }
        return ((String) argValue).toUpperCase();
    }

    // LCASE: Convert a string to lowercase
    private Object lcase(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "LCASE function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof String)) {
            ErrorHandler.error(expression.name, "LCASE function expects a string argument");
            return null;
        }
        return ((String) argValue).toLowerCase();
    }

    // CAPITALIZE: Capitalize the first letter of a string
    private Object capitalize(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "CAPITALIZE function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof String)) {
            ErrorHandler.error(expression.name, "CAPITALIZE function expects a string argument");
            return null;
        }
        String str = (String) argValue;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // FLOOR: Round a number down to the nearest integer
    private Object floor(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "FLOOR function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof Double)) {
            ErrorHandler.error(expression.name, "FLOOR function expects a numeric argument");
            return null;
        }
        return Math.floor((Double) argValue);
    }

    // CEIL: Round a number up to the nearest integer
    private Object ceil(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "CEIL function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof Double)) {
            ErrorHandler.error(expression.name, "CEIL function expects a numeric argument");
            return null;
        }
        return Math.ceil((Double) argValue);
    }

    // ROUND: Round a number to the nearest integer
    private Object round(Expression.FunctionCall expression) {
        List<Expression> arguments = expression.arguments;
        if (arguments.size() != 1) {
            ErrorHandler.error(expression.name, "ROUND function expects exactly one argument");
            return null;
        }
        Object argValue = evaluate(arguments.get(0));
        if (!(argValue instanceof Double)) {
            ErrorHandler.error(expression.name, "ROUND function expects a numeric argument");
            return null;
        }
        return Math.round((Double) argValue);
    }

    // RAND: Generate a random number
    private Object rand(Expression.FunctionCall expression) {
        return Math.random();
    }

    // COUNT: Count the number of rows and return a table with a single column
    private Object count(Expression.FunctionCall expression) {
        if (expression.arguments.size() != 1) {
            ErrorHandler.error(expression.name, "AVG function expects exactly one argument");
            return null;
        }

        Object expr = evaluateClause(expression.arguments.get(0));
        if (expr instanceof List) {
            // if the value is * return the number of rows
            if (((List) expr).size() == 1 && ((List) expr).get(0).equals("*")) {
                return ((List) expr).size();
            }

            // count the number of non-null values
            int count = 0;
            for (Object value : (List) expr) {
                if (value != null) {
                    count++;
                }
            }
            return count;
        } else {
            ErrorHandler.error(expression.name, "COUNT function expects a list argument");
        }

        return null;

    }

    // MIN: find the minimum value on a table
    private Object min(Expression.FunctionCall expression) {
        if (expression.arguments.size() != 1) {
            ErrorHandler.error(expression.name, "MIN function expects exactly one argument");
            return null;
        }

        Object expr = evaluateClause(expression.arguments.get(0));

        if (expr instanceof List) {
            // clear the list from nulls
            ((List) expr).removeIf(Objects::isNull);

            // get min value from the list
            return Collections.min((List) expr);
        } else {
            ErrorHandler.error(expression.name, "MIN function expects a list argument");
        }

        return null;
    }

    // MAX: find the maximum value on a table
    private Object max(Expression.FunctionCall expression) {
        if (expression.arguments.size() != 1) {
            ErrorHandler.error(expression.name, "MAX function expects exactly one argument");
            return null;
        }

        Object expr = evaluateClause(expression.arguments.get(0));

        if (expr instanceof List) {
            // clear the list from nulls
            ((List) expr).removeIf(Objects::isNull);

            // get max value from the list
            return Collections.max((List) expr);
        } else {
            ErrorHandler.error(expression.name, "MAX function expects a list argument");
        }
        return null;
    }

    // SUM: calculate the sum of values in a table
    private Object sum(Expression.FunctionCall expression) {
        if (expression.arguments.size() != 1) {
            ErrorHandler.error(expression.name, "SUM function expects exactly one argument");
            return null;
        }

        Object expr = evaluateClause(expression.arguments.get(0));

        if (expr instanceof List) {
            // verify is a list of numbers
            for (Object value : (List) expr) {
                if (!(value instanceof Double)) {
                    ErrorHandler.error(expression.name, "SUM function expects a list of numbers");
                }
            }

            // get sum value from the list
            double sum = 0;
            for (Object value : (List) expr) {
                sum += (double) value;
            }
            return sum;
        } else {
            ErrorHandler.error(expression.name, "SUM function expects a list argument");
        }
        return null;

    }

    // AVG: calculate the average of values in a table
    private Object avg(Expression.FunctionCall expression) {
        if (expression.arguments.size() != 1) {
            ErrorHandler.error(expression.name, "AVG function expects exactly one argument");
            return null;
        }

        Object expr = evaluateClause(expression.arguments.get(0));

        if (expr instanceof List) {
            // verify is a list of numbers
            for (Object value : (List) expr) {
                if (!(value instanceof Double)) {
                    ErrorHandler.error(expression.name, "AVG function expects a list of numbers");
                }
            }

            // get avg value from the list
            double sum = 0;
            for (Object value : (List) expr) {
                sum += (double) value;
            }
            return sum / ((List) expr).size();
        } else {
            ErrorHandler.error(expression.name, "AVG function expects a list argument");
        }
        return null;
    }

}
