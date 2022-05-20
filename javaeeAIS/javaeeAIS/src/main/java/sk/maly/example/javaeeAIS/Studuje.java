package sk.maly.example.javaeeAIS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="studuje", schema="javaee")
public class Studuje {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	@Column(name="znamka", unique=false)
	private String znamka;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_osoba", nullable=false, updatable=false)
	private Osoby osoba;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_predmet", nullable=false, updatable=false)
	private Predmety predmet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZnamka() {
		return znamka;
	}

	public void setZnamka(String znamka) {
		this.znamka = znamka;
	}

	public Osoby getOsoba() {
		return osoba;
	}

	public void setOsoba(Osoby osoba) {
		this.osoba = osoba;
	}

	public Predmety getPredmet() {
		return predmet;
	}

	public void setPredmet(Predmety predmet) {
		this.predmet = predmet;
	}
	
	public String getMenoOsoby() {
		return this.osoba.getMeno() + " " + this.osoba.getPriezvisko();
	}
	
	public String getMenoPredmetu() {
		return this.predmet.getNazov();
	}
	
}
