package sk.maly.example.javaeeAIS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RestController
@RequestMapping("/osoby")
public class OsobyController {
	
	@Autowired
	OsobyRepository osobyRepository;
	
	@Autowired
	StudujeRepository studujeRepository;
	
	@Autowired
	PredmetyRepository predmetRepository;
	
	@Autowired
	ZaradeniaRepository zaradeniaRepository;
	
	
	@GetMapping("/zoznam")
	public List<Osoby> daj_zoznam_osob(){
		return osobyRepository.findAll();
	}
	
	@GetMapping("/zoznam_predmety")
	public List<Predmety> daj_zoznam_predmetov(){
		return predmetRepository.findAll();
	}
	
	@GetMapping("/zoznam_studuje")
	public List<Studuje> daj_zoznam_studuje(){
		return studujeRepository.findAll();
	}
	
	@PostMapping("/zoznam_studuje_student")
	public List<Studuje> daj_zoznam_studuje_student(@RequestBody String data_json){
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			Osoby osoba = osobyRepository.getById(id);
	
			System.out.println(data_json);
			return osoba.getZoznam_predmetov_studuje();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@GetMapping("/zoznam_zaradenia")
	public List<Zaradenia> daj_zoznam_zaradenia(){
		return zaradeniaRepository.findAll();
	}
	
	@PostMapping("/pridaj_osobu")
	public void pridaj_osobu(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			String meno = slovnik_json.get("meno").asText();
			String priezvisko = slovnik_json.get("priezvisko").asText();
			Integer zaradenia = slovnik_json.get("zaradenia").asInt();
			Osoby osoba = new Osoby();
			osoba.setMeno(meno);
			osoba.setPriezvisko(priezvisko);
			Zaradenia zaradenie = zaradeniaRepository.getById(zaradenia);
			osoba.setZaradenia(zaradenie);
			osobyRepository.save(osoba);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	@PostMapping("/uprav_osobu")
	public void uprav_osobu(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			String meno = slovnik_json.get("meno").asText();
			String priezvisko = slovnik_json.get("priezvisko").asText();
			Integer zaradenia = slovnik_json.get("zaradenia").asInt();
			Osoby osoba = osobyRepository.getById(id);
			osoba.setMeno(meno);
			osoba.setPriezvisko(priezvisko);
			Zaradenia zaradenie = zaradeniaRepository.getById(zaradenia);
			osoba.setZaradenia(zaradenie);
			osobyRepository.save(osoba);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/vymaz_osobu")
	public void vymaz_osobu(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			String meno = slovnik_json.get("meno").asText();
			String priezvisko = slovnik_json.get("priezvisko").asText();
			Integer zaradenia = slovnik_json.get("zaradenia").asInt();
			Osoby osoba = osobyRepository.getById(id);
			osobyRepository.delete(osoba);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/zapis_predmet")
	public void zapis_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id_osoba = slovnik_json.get("id_osoba").asInt();
			Integer id_predmet = slovnik_json.get("id_predmet").asInt();
			
			Osoby osoba = osobyRepository.getById(id_osoba);
			Predmety predmet = predmetRepository.getById(id_predmet);
			Studuje studuje = new Studuje();
			studuje.setOsoba(osoba);
			studuje.setPredmet(predmet);
			studujeRepository.save(studuje);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/odpis_predmet")
	public void odpis_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			
			Studuje studuje = studujeRepository.getById(id);
			studujeRepository.delete(studuje);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/uc_predmet")
	public void uc_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id_osoba = slovnik_json.get("id_osoba").asInt();
			Integer id_predmet = slovnik_json.get("id_predmet").asInt();
			
			Osoby osoba = osobyRepository.getById(id_osoba);
			Predmety predmet = predmetRepository.getById(id_predmet);
			if(!osoba.getZoznam_predmetov_uci().contains(predmet)) {
				osoba.getZoznam_predmetov_uci().add(predmet);
			}
			
			osobyRepository.save(osoba);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/oduc_predmet")
	public void oduc_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id_osoba = slovnik_json.get("id_osoba").asInt();
			Integer id_predmet = slovnik_json.get("id_predmet").asInt();
			
			Osoby osoba = osobyRepository.getById(id_osoba);
			Predmety predmet = predmetRepository.getById(id_predmet);
			osoba.getZoznam_predmetov_uci().remove(predmet);
			osobyRepository.save(osoba);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/pridaj_predmet")
	public void pridaj_predmet(@RequestBody String data_json) {
		
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			String nazov = slovnik_json.get("nazov").asText();
			Integer kredity = slovnik_json.get("kredity").asInt();
			Predmety predmet = new Predmety();
			predmet.setNazov(nazov);
			predmet.setKredity(kredity);
			predmetRepository.save(predmet);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/uprav_predmet")
	public void uprav_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			String nazov = slovnik_json.get("nazov").asText();
			Integer kredity = slovnik_json.get("kredity").asInt();
			Predmety predmet = predmetRepository.getById(id);
			predmet.setNazov(nazov);
			predmet.setKredity(kredity);
			predmetRepository.save(predmet);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping("/vymaz_predmet")
	public void vymaz_predmet(@RequestBody String data_json) {
		try {
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json;
			slovnik_json = (ObjectNode) objmap_json.readTree(data_json);
			Integer id = slovnik_json.get("id").asInt();
			String nazov = slovnik_json.get("nazov").asText();
			Integer kredity = slovnik_json.get("kredity").asInt();
			Predmety predmet = predmetRepository.getById(id);
			predmetRepository.delete(predmet);
			
			System.out.println(data_json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@PostMapping("/zapis_znamku")
	public void zapis_znamku(@RequestBody Studuje studuje) {
		if (studuje.getZnamka().equals("blank")) {
			studuje.setZnamka(null);
		}
		studujeRepository.save(studuje);
	}
	
}
