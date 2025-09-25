package edu.ccrm.domain;
public interface Persistable {
    String toCSV();
    void fromCSV(String csvLine);
    default String getHeader() { return "id,name,email,active"; }
}
