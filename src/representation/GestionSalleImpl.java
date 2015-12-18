/**
 * 
 */
package representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BDD.BaseDeDonnee;

/**
 * @author Lenny Lucas but: implemente la construction de tout les objets de la BDD ainsi que toutes
 *         les fonctions. on suppose que l'interface a deja trouv� les id des elements.
 */
public class GestionSalleImpl implements GestionSalle {


  private BaseDeDonnee bdd;
  private FactorySalle factSalle;
  private List<String> listeDesClefs;


  public GestionSalleImpl() {
    this.bdd = new BaseDeDonnee();
    this.factSalle = new FactorySalleImpl();
    this.listeDesClefs = new ArrayList<String>();
  }

  public Object get(String key) {
    return bdd.get(key);
  }

  // --------------------------------------------------------------------//
  // pour les adresses //
  // --------------------------------------------------------------------//
  public boolean creationAdresse(int noRue, String rue, int codePostal, String ville,
      String complement) {
    Adresse a = factSalle.createAdresse(noRue, rue, codePostal, ville, complement);
    String key = bdd.put(a);
    return listeDesClefs.add(key);
  }

  public HashMap<String, Adresse> affichageAdresse() {
    HashMap<String, Adresse> liste = new HashMap<String, Adresse>();
    for (String s : listeDesClefs) {
      if (s.charAt(0) == 'A') {
        liste.put(s, (Adresse) bdd.get(s));
      }
    }
    return liste;
  }

  public boolean removeAdresse(String idAdr) {
    return (bdd.remove(idAdr) && listeDesClefs.remove(idAdr));
  }


  // --------------------------------------------------------------------//
  // pour les Batiments //
  // --------------------------------------------------------------------//

  public boolean creationBatiment(String adresseBat) {
    Batiment a = factSalle.createBatiment(adresseBat);
    String key = bdd.put(a);
    return listeDesClefs.add(key);
  }

  public HashMap<String, Batiment> affichageBatiment() {
    HashMap<String, Batiment> liste = new HashMap<String, Batiment>();
    for (String s : listeDesClefs) {
      if (s.charAt(0) == 'B') {
        liste.put(s, (Batiment) bdd.get(s));
      }
    }
    return liste;
  }

  public boolean removeBatiment(String idBat) {
    BatimentImpl bat = (BatimentImpl) bdd.get(idBat);
    // on detruit toutes les salles du batiment
    for (String s : bat.getSalles()) {
      removeSalle(s);
    }
    return (bdd.remove(idBat) && listeDesClefs.remove(idBat));
  }

  public boolean ajouterSalleABatiment(String idSalle, String idBat) {
    BatimentImpl bat = (BatimentImpl) bdd.get(idBat);
    SalleImpl salle = (SalleImpl) bdd.get(idSalle);
    salle.setIdBat(idBat);
    bat.addSalle(idSalle);
    return (bdd.update(idSalle, salle) && bdd.update(idBat, bat));
  }

  
  public boolean retirerSalleABatiment(String idSalle, String idBat) {
    BatimentImpl bat = (BatimentImpl) bdd.get(idBat);
    SalleImpl salle = (SalleImpl) bdd.get(idSalle);
    salle.setIdBat("");
    bat.removeSalle(idSalle);
    return (bdd.update(idSalle, salle) && bdd.update(idBat, bat));
  }


  // --------------------------------------------------------------------//
  // pour les Salles //
  // --------------------------------------------------------------------//

  public String creationSalle(int noEtage, int noSalle, int superficie, String typeSalle) {
    Salle s = factSalle.createSalle(noEtage, noSalle, "", superficie, typeSalle);
    String key = bdd.put(s);
    listeDesClefs.add(key);
    return key;
  }

  public HashMap<String, Salle> affichageSalle() {
    HashMap<String, Salle> liste = new HashMap<String, Salle>();
    for (String s : listeDesClefs) {
      if (s.charAt(0) == 'S') {
        liste.put(s, (Salle) bdd.get(s));
      }
    }
    return liste;
  }
  
  public boolean ajouterMaterielASalle(String idMat, String idSalle) {
    SalleImpl salle = (SalleImpl) bdd.get(idSalle);
    MaterielImpl mat = (MaterielImpl) bdd.get(idMat);
    salle.addMateriel(idMat);
    mat.addSalle(idSalle);
    return (bdd.update(idSalle, salle) && bdd.update(idBat, bat));
  }

  
  public boolean retirerMaterielASalle(String idMat, String idSalle) {
    BatimentImpl bat = (BatimentImpl) bdd.get(idBat);
    SalleImpl salle = (SalleImpl) bdd.get(idSalle);
    salle.setIdBat("");
    bat.removeSalle(idSalle);
    return (bdd.update(idSalle, salle) && bdd.update(idBat, bat));
  }

  public boolean removeSalle(String idSalle) {
    SalleImpl salle = (SalleImpl) bdd.get(idSalle);
    retirerSalleABatiment(idSalle, salle.getIdBat());
    return (bdd.remove(idSalle) || listeDesClefs.remove(idSalle));
  }

  // --------------------------------------------------------------------//
  // pour les Materiaux //
  // --------------------------------------------------------------------//

  public String creationMateriaux(String idSalle, String codeMateriel, String nomMateriel,
      String descrMateriel, double tarif) {
    Materiel s = factSalle.createMateriel(idSalle, codeMateriel, nomMateriel, descrMateriel, tarif);
    String key = bdd.put(s);
    listeDesClefs.add(key);
    return key;
  }

  public HashMap<String, Materiel> affichageMateriaux() {
    HashMap<String, Materiel> liste = new HashMap<String, Materiel>();
    for (String s : listeDesClefs) {
      if (s.charAt(0) == 'M') {
        liste.put(s, (Materiel) bdd.get(s));
      }
    }
    return liste;
  }

  public boolean removeMateriaux(String idMat) {
    MaterielImpl salle = (MaterielImpl) bdd.get(idMat);
    retirerSalleABatiment(idSalle, salle.getIdBat());
    return (bdd.remove(idSalle) || listeDesClefs.remove(idSalle));
  }
}
