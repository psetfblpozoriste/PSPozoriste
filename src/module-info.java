module PS {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires itextpdf;

    opens net.etfbl.is.pozoriste.controller;
    opens net.etfbl.is.pozoriste.main;
    opens net.etfbl.is.pozoriste.model.dao.mysql;
    opens net.etfbl.is.pozoriste.model.dto;
    opens net.etfbl.is.pozoriste.resursi;
    opens net.etfbl.is.pozoriste.view;
}