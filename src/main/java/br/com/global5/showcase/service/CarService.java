/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.global5.showcase.service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.com.global5.showcase.model.Car;
import br.com.global5.showcase.model.Gerenciadoras;


public class CarService implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7493115855379626823L;

	private final static String[] colors;
	
	private final static String[] brands;
	
	private final static String[] gerenciadoras;
    
    static {
		colors = new String[10];
		colors[0] = "Black";
		colors[1] = "White";
		colors[2] = "Green";
		colors[3] = "Red";
		colors[4] = "Blue";
		colors[5] = "Orange";
		colors[6] = "Silver";
		colors[7] = "Yellow";
		colors[8] = "Brown";
		colors[9] = "Maroon";
		
		brands = new String[10];
		brands[0] = "BMW";
		brands[1] = "Mercedes";
		brands[2] = "Volvo";
		brands[3] = "Audi";
		brands[4] = "Renault";
		brands[5] = "Fiat";
		brands[6] = "Volkswagen";
		brands[7] = "Honda";
		brands[8] = "Jaguar";
		brands[9] = "Ford";
		
		gerenciadoras = new String[8];
		gerenciadoras[0] = "Global 5";
		gerenciadoras[1] = "OpenTech";
		gerenciadoras[2] = "buonny";
		gerenciadoras[3] = "skymark";
		gerenciadoras[4] = "Control Risk";
		gerenciadoras[5] = "Monytor";
		gerenciadoras[6] = "ControlWay";
		gerenciadoras[7] = "BrasilRisk";
	}
    
    public List<Car> createCars(int size) {
        List<Car> list = new ArrayList<Car>();
		for(int i = 0 ; i < size ; i++) {
			list.add(new Car(getRandomId(), getRandomBrand(), getRandomYear(), getRandomColor(), getRandomPrice(), getRandomSoldState()));
        }
        
        return list;
    }
    
    public List<Gerenciadoras> createGerenciadoras() {
    	List<Gerenciadoras> list = new ArrayList<Gerenciadoras>();
    	for(int i = 0; i < 8; i++) {
    		list.add(new Gerenciadoras(i+1, gerenciadoras[i]));
    	}
    	
    	return list;
    }
    
    private String getRandomId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
    
    private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}
	
	private String getRandomColor() {
		return colors[(int) (Math.random() * 10)];
	}
	
	private String getRandomBrand() {
		return brands[(int) (Math.random() * 10)];
	}
    
    private int getRandomPrice() {
		return (int) (Math.random() * 100000);
	}
    
    private boolean getRandomSoldState() {
		return Math.random() > 0.5;
	}
    
    public List<String> getColors() {
        return Arrays.asList(colors);
    }
    
    public List<String> getBrands() {
        return Arrays.asList(brands);
    }
}
