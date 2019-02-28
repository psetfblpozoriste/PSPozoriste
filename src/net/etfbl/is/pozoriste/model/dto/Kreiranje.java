package net.etfbl.is.pozoriste.model.dto;

import java.sql.Date;

public class Kreiranje {
    private Integer id,idPredstave,idRepertoara,idRadnik,idGostujucePredstave;
    private Date datum;

    public Integer getId() {
        return id;
    }

    public Integer getIdPredstave() {
        return idPredstave;
    }

    public Integer getIdRepertoara() {
        return idRepertoara;
    }

    public Integer getIdRadnik() {
        return idRadnik;
    }

    public Integer getIdGostujucePredstave() {
        return idGostujucePredstave;
    }

    public Date getDatum() {
        return datum;
    }

    public Kreiranje(Integer id, Integer idPredstave, Integer idRepertoara, Integer idRadnik, Integer idGostujucePredstave, Date datum) {
        this.id = id;
        this.idPredstave = idPredstave;
        this.idRepertoara = idRepertoara;
        this.idRadnik = idRadnik;
        this.idGostujucePredstave = idGostujucePredstave;
        this.datum = datum;
    }
    
}
