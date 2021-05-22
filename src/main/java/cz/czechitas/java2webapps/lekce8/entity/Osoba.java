package cz.czechitas.java2webapps.lekce8.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * S daty v databázi se pracuje v Javě pomocí entit. Entita reprezentuje jeden řádek v databázi.
 * Entita – údaje o jedné osobě.
 * Pojmenování properties musí zůstat zachováno, protože odpovídá názvům sloupečků v databázové tabulce. Stejně tak musí
 * být zachováno jméno třídy, odpovídá jménu tabulky v databázi.
 * Anotace @Entity. Říká Springu, že toto je entita. Spring díky tomu ví, že to má hledat v databázi v tabulce, která má
 * stejné jméno jako entita (např. OSOBA).
 */
@Entity
public class Osoba {
    /**
     * Field id je databázový identifikátor. Má na sobě anotaci @Id. To říká Springu, že toto je v databázi jednoznačný
     * identifikátor.
     *
     * @GeneratedID: říká, jakým způsobem se id v databázi generují. Pro id je nutné zařídit, aby bylo v tabulce unikátní
     * a aby ho každý záznam měl. GenerationType.IDENTITY znamená, že se o to postará samotná databáze.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 100)
    @NotBlank
    private String jmeno;

    @Length(max = 100)
    @NotBlank
    private String prijmeni;

    /**
     * Validace @PastOrPresent - musí to být v minulosti, nebo dnešek.
     */
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate datumNarozeni;

    @Length(max = 200)
    @NotBlank
    private String adresa;

    @Length(max = 100)
    @Email
    private String email;

    @Length(min = 9, max = 13)
    /**
     * Regexp = regulární výraz. Tento říká: na začátku může a nemusí být plus a dál musí být číslice.
     */
    @Pattern(regexp = "\\+?\\d+")
    private String telefon;

    public Osoba() {
    }

    public Osoba(Long id, String jmeno, String prijmeni, LocalDate datumNarozeni, String adresa, String email, String telefon) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.datumNarozeni = datumNarozeni;
        this.adresa = adresa;
        this.email = email;
        this.telefon = telefon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public LocalDate getDatumNarozeni() {
        return datumNarozeni;
    }

    public void setDatumNarozeni(LocalDate datumNarozeni) {
        this.datumNarozeni = datumNarozeni;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Integer getVek() {
        if (datumNarozeni == null) {
            return null;
        }
        return datumNarozeni.until(LocalDate.now()).getYears();
    }

}
