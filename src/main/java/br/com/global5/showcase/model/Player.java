
package br.com.global5.showcase.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {
    
    private String name;
    
    private Map<Integer,Integer> goals;
    
    public Player() {
        goals = new LinkedHashMap<Integer,Integer>();
    }
    
    public Player(String name, Map<Integer,Integer> goals) {
        this.name = name;
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getGoals(int year) {
        return goals.get(year);
    }
}
