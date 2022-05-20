package sk.maly.example.javaeeAIS;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="predmety", schema="javaee")
public class Predmety {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	@Column(name="nazov", nullable=false, unique=false)
	private String nazov;
	
	@Column(name="kredity", nullable=false, unique=false)
	private Integer kredity;
	
	@JsonIgnore
	@OneToMany(mappedBy="predmet")
	private List<Studuje> zoznam_predmetov_studuje;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public Integer getKredity() {
		return kredity;
	}

	public void setKredity(Integer kredity) {
		this.kredity = kredity;
	}

	public List<Studuje> getZoznam_predmetov_studuje() {
		return zoznam_predmetov_studuje;
	}

	public void setZoznam_predmetov_studuje(List<Studuje> zoznam_predmetov_studuje) {
		this.zoznam_predmetov_studuje = zoznam_predmetov_studuje;
	}
	
	
}
