package sk.maly.example.javaeeAIS;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="osoby", schema="javaee")
public class Osoby {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	@Column(name="meno", nullable=false, unique=false)
	private String meno;
	
	@Column(name="priezvisko", nullable=false, unique=false)
	private String priezvisko;
	
	@ManyToOne
	@JoinTable(name="osoby_zaradenia",
	        joinColumns=
	            @JoinColumn(name="id_osoba", referencedColumnName="id"),
	        inverseJoinColumns=
	            @JoinColumn(name="id_zaradenie", referencedColumnName="id")
	        )
	private Zaradenia zaradenia;
	
	
	@ManyToMany
	@JoinTable(name="uci",
	        joinColumns=
	            @JoinColumn(name="id_osoba", referencedColumnName="id"),
	        inverseJoinColumns=
	            @JoinColumn(name="id_predmet", referencedColumnName="id")
	        )
	private List<Predmety> zoznam_predmetov_uci;
	
	
	@OneToMany(mappedBy="osoba")
	private List<Studuje> zoznam_predmetov_studuje;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}

	public List<Predmety> getZoznam_predmetov_uci() {
		return zoznam_predmetov_uci;
	}

	public void setZoznam_predmetov_uci(List<Predmety> zoznam_predmetov_uci) {
		this.zoznam_predmetov_uci = zoznam_predmetov_uci;
	}

	public List<Studuje> getZoznam_predmetov_studuje() {
		return zoznam_predmetov_studuje;
	}

	public void setZoznam_predmetov_studuje(List<Studuje> zoznam_predmetov_studuje) {
		this.zoznam_predmetov_studuje = zoznam_predmetov_studuje;
	}

	public Zaradenia getZaradenia() {
		return zaradenia;
	}

	public void setZaradenia(Zaradenia zaradenia) {
		this.zaradenia = zaradenia;
	}

}
