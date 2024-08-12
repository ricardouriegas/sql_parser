package edu.upvictoria.fpoo.SQL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;

public class Writer {
    // this class will write a DTD for all the database (path)
    // it will be used to validate the XML files
    // ! knowing that a XML file is the rules that a table must follow
    // ! and the DTD is the rules that a database must follow

    /**
     * function to write the standard DTD for the database
     * 
     * @param path
     */
    public void writeDTD(Path path) {
        // if the DTD already exists, do not write it again
        if (existsDTD(path))
            return;

        // else write the standard DTD
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path.toString() + "/.DATABASE.dtd"));
            /**
             * <!ELEMENT table (name, columns, constraints) >
             * <!ELEMENT name (#PCDATA)>
             * <!ELEMENT columns (column+)>
             * <!ELEMENT column (name, type, primary_key?, default?, check?, constraints?)>
             * <!ELEMENT default (#PCDATA)>
             * <!ELEMENT check (#PCDATA)>
             * <!ELEMENT type (#PCDATA)>
             * <!ELEMENT primary_key (#PCDATA)>
             * <!ELEMENT constraints (constraint+)>
             * <!ELEMENT constraint (#PCDATA)>
             * 
             * <!ATTLIST check condition CDATA #REQUIRED>
             */
            writer.write("<!ELEMENT table (name, columns, constraints) >\n");
            writer.write("<!ELEMENT name (#PCDATA)>\n");
            writer.write("<!ELEMENT columns (column+)>\n");
            writer.write("<!ELEMENT column (name, type, primary_key?, default?, check?, constraints?)>\n");
            writer.write("<!ELEMENT default (#PCDATA)>\n");
            writer.write("<!ELEMENT check (#PCDATA)>\n");
            writer.write("<!ELEMENT type (#PCDATA)>\n");
            writer.write("<!ELEMENT primary_key (#PCDATA)>\n");
            writer.write("<!ELEMENT constraints (constraint+)>\n");
            writer.write("<!ELEMENT constraint (#PCDATA)>\n");

            writer.write("<!ATTLIST check condition CDATA #REQUIRED>\n");

            writer.close();
        } catch (Exception e) {
            // e.printStackTrace();
            ErrorHandler.error("There was an error writing the DTD file");
        }
    }

    /**
     * function to check if the DTD file already exists
     * 
     * @param path
     * @return
     */
    public boolean existsDTD(Path path) {
        return Path.of(path.toString() + ".dtd").toFile().exists();
    }

    /**
     * function to write the XML file for a table
     * 
     * @param path
     * @param table
     */
    public void writeXML(Path path, Table table) {
        // // if the XML file already exists, do not write it again
        // if (existsXML(path, table))
        // return;

        // // else write the XML file
        // try {
        // BufferedWriter writer = new BufferedWriter(
        // new FileWriter(path.toString() + "/" + table.getName() + ".xml"));

        // writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        // writer.write("<table>\n");
        // /*
        // for (Row row : table.getRows()) {
        // writer.write("\t<row>\n");

        // for (Column column : table.getColumns()) {
        // writer.write("\t\t<" + column.getName() + ">");
        // writer.write(row.get(column.getName()));
        // writer.write("</" + column.getName() + ">\n");
        // }

        // writer.write("\t</row>\n");
        // }
        // */
        // writer.write("</table>");

        // writer.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // ErrorHandler.error("There was an error writing the XML file");
        // }
    }

    /**
     * function to check if the XML file already exists
     * 
     * @param path
     * @param table
     * @return
     */
    // public boolean existsXML(Path path, Table table) {
    // return Path.of(path.toString() + "/" + table.getName() +
    // ".xml").toFile().exists();
    // }
}
