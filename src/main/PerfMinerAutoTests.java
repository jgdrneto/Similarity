/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.NodeJpaController;
import controller.ScenarioJPAController;
import similarity.entity.Node;
import similarity.entity.Scenario;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jullian
 */
public class PerfMinerAutoTests {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
    	
    	//Entidade do banco de testes automáticos
        EntityManagerFactory emfAT = Persistence.createEntityManagerFactory("PerfMiner-AutoTestsPU");
        EntityManagerFactory emfMT = Persistence.createEntityManagerFactory("PerfMiner-ManualTestsPU");
        
        ScenarioJPAController scenarioAuto = new ScenarioJPAController(emfAT);
        ScenarioJPAController scenarioManual = new ScenarioJPAController(emfMT);
        
        List<Scenario> registrosAuto = scenarioAuto.findEntities();
        List<Scenario> registrosManual = scenarioManual.findEntities();
                
        System.out.println("Quantidade de cenários automáticos: " + registrosAuto.size());
        System.out.println("Quantidade de cenários manuais: " + registrosManual.size()); 
    }
  
}
