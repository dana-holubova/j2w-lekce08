package cz.czechitas.java2webapps.lekce8.repository;

import cz.czechitas.java2webapps.lekce8.entity.Osoba;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Anotace @Repository.
 * Interface zdědím z jiného interface pomocí extends. Repository dědí z interface, které vytváří Spring, tj. z
 * CrudRepository. Crud = Create Read Update Delete. Repository bude umět vyvářet záznamy, číst je, aktualizovat je a
 * mazat.
 * Je to podobné jako s listem, tj. musím do ostrých závorek uvést typy. tj s jakými entitami bude repository pracovat
 * a jaké id má entita (tj. datový typ).
 * Díky tomu Spring zařídí, že můžu pracovat s tabulkou v aplikaci.
 * Nepřidávám žádné vlastní metody. Všechny přebírám z CrudRepository. Definuji to hlavně proto, abych mohla uvést
 * typy.
 */

@Repository
public interface OsobaRepository extends CrudRepository<Osoba, Long> {
}
