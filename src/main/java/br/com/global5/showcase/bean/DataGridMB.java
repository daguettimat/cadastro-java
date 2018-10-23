
package br.com.global5.showcase.bean;


import br.com.global5.showcase.model.Car;
import br.com.global5.showcase.service.CarService;

import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DataGridMB implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4716197230295765615L;

	private List<Car> cars;
    
    private Car selectedCar;
    
    @Inject
    private CarService service;
    
    @PostConstruct
    public void init() {
        cars = service.createCars(48);
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setService(CarService service) {
        this.service = service;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
}
