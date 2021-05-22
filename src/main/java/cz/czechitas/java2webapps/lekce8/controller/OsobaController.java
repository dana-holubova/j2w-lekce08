package cz.czechitas.java2webapps.lekce8.controller;

import cz.czechitas.java2webapps.lekce8.entity.Osoba;
import cz.czechitas.java2webapps.lekce8.repository.OsobaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class OsobaController {

//    fieldy
    /**
     * final říká, že to musí být vyplněné už v okamžiku, kdy se třída vytváří. Proto to musí být vytvořené v konstruktoru.
     */

    private final OsobaRepository repository;

//    private final DataSource dataSource;

//    konstruktor
    /**
     * starý způsob, kdy si v konstruktoru musím vytvořit sama instanci třídy DataSource. Rovnou vytvářím třídu a nejde
     * to změnit jinak, než že přepíšu kód.
     public OsobaController () {
     dataSource = new DataSource();
     }
     */

    /**
     * Nový způsob, který využívá Spring. Do konstruktoru se to předá přes parametr.
     * Tím zařídím to, že pevnou závislost, kterou jsem zde měla (viz příklad výše), nahradím méně pevnou
     * vazbou. Závislost říká, že na vstupu tam potřebuji něco vložit, ale neříká co přesně. Když se to dává do parametru,
     * tak tam můžu vložit tu konkrétní třídu, ale také jejího potomka. Řeknu Springu, jaký typ třídy potřebuji a nechám
     * na něm, aby mi tam něco dosadil.
     * Spring zařídí, že je služba v celé aplikaci jen jednou a použije se všude, kde je potřeba.
     */
//  @Autowired
//  public OsobaController(DataSource dataSource) {
//      this.dataSource = dataSource;
//    }

    /**
     * repository má různé metody:
     * findAll - vypíše seznam všech záznamů v tabulce. Vrací to Iterable<>. Je to podobné jako list.
     * deleteAll - všechno smaže.
     * count - řekne, kolik je tam záznamů
     * findById - předám jí id a ona vrátí záznam, který pod tímto id je
     * deleteById - smaže záznam s konkrétním id
     * save - vezme celou entitu, zjistí jestli má entita id, a buď přidá nový záznam, nebo udělá aktualizaci
     * Když entita nemá id, tak vím, že to ještě nebylo uloženo v databázi. Vloží se to jako nový záznam, kterému se id přidělí.
     * delete - předává se mu entita, nejen id. On si z ní stejně id načte a záznam smaže podle něj.
     * deletaAll - předám mu seznam entit a on je všechny smaže
     * saveAll - předám mu seznam entit a on je všechny uloží
     * findAllById - předám seznam id a on mi načte záznamy, které mají příslušná id
     * existById - vrátí true/false, jestli záznam s takovým id existuje, nebo ne.
     */
    @Autowired
    public OsobaController(OsobaRepository repository) {
        this.repository = repository;
    }


  /* private final List<Osoba> seznamOsob = List.of(
          new Osoba(1L, "Božena", "Němcová", LocalDate.of(1820, 2, 4), "Vídeň", null, null)
  );
   */

    /**
     * @InitBinder - z prohlížeče nemůže nikdy přijít String s hodnotou null. Prázdné políčko ve formuláři přijde jako
     * prázdný textový řetězec. Tento kód zařídí, že se prázdné String překlopí do null hodnoty.
     */
    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        //prázdné textové řetězce nahradit null hodnotou
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Zobrazí seznam všech osob.
     */
    @GetMapping("/")
    public Object seznam() {
        /**
         * Iterable<> je podobné jako seznam. Používá se, když chci získat víc řádků z databáze a ty pak v template vypsat
         * pomocí th:each
         */
        Iterable<Osoba> vsechnyOsoby = repository.findAll();
        return new ModelAndView("seznam")
                .addObject("osoby", vsechnyOsoby);

        /**
         * Řešení od lektora
         */
//        return new ModelAndView("seznam")
//                .addObject("osoby", repository.findAll());
    }

    /**
     * Zobrazí formulář pro zadání nové osoby.
     */
    @GetMapping("/novy")
    public Object novy() {
        return new ModelAndView("detail")
                .addObject("osoba", new Osoba());
    }

    /**
     * Uloží novou osobu do databáze.
     * <p>
     * Pokud není splněna některá validace, znovu zobrazí formulář pro zadání osoby.
     */
    @PostMapping("/novy")
    public Object pridat(@ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        /**
         * osoba.setId(null) - jen pro jistotu. Podle toho Spring pozná, že má vytvořit nový záznam. Aby přes adresu
         * /novy nemohl uživatel upravit záznam např. když v prohlížeči (F12 přidá do formuláře políčko pro odeslání
         * id záznamu).
         */
        osoba.setId(null);
        repository.save(osoba);
        return "redirect:/";
    }

    /**
     * Zobrazí detail osoby
     *
     * @param id Identifikátor osoby zadaný v URL. Identifikátor může obsahovat jen číslice (musí být alespoň jedna).
     *           {id:[0-9]+}. To, co je za id je omezení. Tj. může tam být číslice 0-9. + znamená, že musí být alespoň jedna a může
     *           jich tam být víc. Je to regulární výraz (regexp).
     */
    @GetMapping("/{id:[0-9]+}")
    public Object detail(@PathVariable long id) {
        /**
         * Optional<> je speciální typ v Javě, který umožňuje nést v sobě informaci, jestli objekt exitstuje, nebo ne. Je to
         * modení náhrada za null. Dříve by se při zadání id, které v tabulce není, vracela null. Teď se vraci Optional, které
         * v sobě má Osoba. Optional<> má různé metody, např. isPresent(), get(). Kdyby to vrátilo klasické null, tak by tyto
         * metody nešly použít.
         */
        Optional<Osoba> osoba = repository.findById(id);
        /**
         * Zjisti jestli je osoba s daným id přítomna. Pokud ano, tak ji vlož do modelu. Pokud není přítomna, tak vrať null
         */
//        if (osoba.isPresent()) {
//            return new ModelAndView("detail")
//                    .addObject("osoba", osoba.get());
//        }
//        return null;

        /**
         * Řešení od lektora
         */
        if (osoba.isEmpty()) {
            /**
             * Vracím stránku 404 s vysvětlením chyby.
             */
            return ResponseEntity.notFound().build();
        }
        return new ModelAndView("detail")
                .addObject("osoba", osoba.get());
    }

    /**
     * Uloží změněné údaje o osobě do databáze (aktualizuje záznam).
     *
     * @param osoba         Údaje o osobě zadané uživatel plus identifikátor osoby převzatý z URL.
     * @param bindingResult Výsledek validace.
     */

    @PostMapping("/{id:[0-9]+}")
    public Object ulozit(@PathVariable long id, @ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        /**
         * osoba.setId(id) zaručuje, že tam opravdu bude id, takže se údaj v databázi zaktualizuje. Id není nikde ve
         * formuláři (ani skryté). Není proto v entitě osoba. Bere se z adresy. Spring dokáže id doplnit do entityi na
         * základě toho, že se identifikátor (v adrese) jmenuje id.
         */
        osoba.setId(id);
        repository.save(osoba);
        return "redirect:/";
    }

    /**
     * Smaže údaje o osobě z databáze.
     *
     * @param id Identifikátor osoby zadaný v URL. Identifikátor může obsahovat jen číslice (musí být alespoň jedna).
     */
    @PostMapping(value = "/{id:[0-9]+}", params = "akce=smazat")
    public Object smazat(@PathVariable long id) {
        repository.deleteById(id);
        return "redirect:/";
    }

}
