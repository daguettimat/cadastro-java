package br.com.global5.showcase.bean;

import br.com.global5.showcase.model.Car;
import br.com.global5.showcase.model.Gerenciadoras;
import br.com.global5.showcase.model.Stats;
import br.com.global5.showcase.model.Team;
import br.com.global5.showcase.service.CarService;

import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zielinski on 18/01/17.
 */
@Named
@ViewScoped
@SuppressWarnings("unchecked")
public class DatatableMB implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2305685314038766980L;
	private List<Team> teams;
    private List<Car> cars;
    private List<Gerenciadoras> gerenciadoras;

    private List<Car> filteredCars;

    @Inject
    private CarService carService;



    @PostConstruct
    public void init() {
        teams = new ArrayList<Team>();
        Team lakers = new Team("Los Angeles Lakers");
        lakers.getStats().add(new Stats("2005-2006", 50, 32));
        lakers.getStats().add(new Stats("2006-2007", 44, 38));
        lakers.getStats().add(new Stats("2007-2008", 40, 42));
        lakers.getStats().add(new Stats("2008-2009", 45, 37));
        lakers.getStats().add(new Stats("2009-2010", 48, 34));
        lakers.getStats().add(new Stats("2010-2011", 42, 42));
        teams.add(lakers);

        Team celtics = new Team("Boston Celtics");
        celtics.getStats().add(new Stats("2005-2006", 46, 36));
        celtics.getStats().add(new Stats("2006-2007", 50, 32));
        celtics.getStats().add(new Stats("2007-2008", 41, 41));
        celtics.getStats().add(new Stats("2008-2009", 45, 37));
        celtics.getStats().add(new Stats("2009-2010", 38, 44));
        celtics.getStats().add(new Stats("2010-2011", 35, 47));
        teams.add(celtics);

        cars = carService.createCars(30);
        
        gerenciadoras = carService.createGerenciadoras();
    }

    
	@SuppressWarnings("rawtypes")
	public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }

        if(value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }


    public List<Team> getTeams() {
        return teams;
    }

    public List<String> getBrands() {
        return carService.getBrands();
    }

    public List<String> getColors() {
        return carService.getColors();
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Car> getFilteredCars() {
        return filteredCars;
    }

    public void setFilteredCars(List<Car> filteredCars) {
        this.filteredCars = filteredCars;
    }

	public List<Gerenciadoras> getGerenciadoras() {
		return gerenciadoras;
	}

	public void setGerenciadoras(List<Gerenciadoras> gerenciadoras) {
		this.gerenciadoras = gerenciadoras;
	}


}
