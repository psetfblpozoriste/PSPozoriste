package net.etfbl.is.pozoriste.model.dto;

/**
 *
 * @author Ognjen
 */
public class Scena {
    
    private Integer idScene = null;
    
    private String nazivScene;
    
    private Integer brojRedova;
    
    private Integer brojKolona;
    
    public Scena(String naziv,Integer brojRedova, Integer brojKolona){
        this.nazivScene=naziv;
        this.brojRedova=brojRedova;
        this.brojKolona=brojKolona;
    }

    public Integer getIdScene() {
        return idScene;
    }

    public void setIdScene(Integer idScene) {
        this.idScene = idScene;
    }

    public String getNazivScene() {
        return nazivScene;
    }

    public void setNazivScene(String nazivScene) {
        this.nazivScene = nazivScene;
    }

    public Integer getBrojRedova() {
        return brojRedova;
    }

    public void setBrojRedova(Integer brojRedova) {
        this.brojRedova = brojRedova;
    }

    public Integer getBrojKolona() {
        return brojKolona;
    }

    public void setBrojKolona(Integer brojKolona) {
        this.brojKolona = brojKolona;
    }
    
    
    
}
