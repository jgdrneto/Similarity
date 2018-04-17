/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.AutoNodeJpaController;
import controller.AutoScenarioJpaController;
import entity.AutoNode;
import entity.AutoScenario;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/*
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
*/
/**
 *
 * @author Jullian
 */
public class PerfMinerAutoTests {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {

        EntityManagerFactory emfAT = Persistence.createEntityManagerFactory("PerfMiner-AutoTestsPU");
        AutoScenarioJpaController daoScenario = new AutoScenarioJpaController(emfAT);
        
        List<AutoScenario> registros = daoScenario.findAutoScenarioEntities();        
        //ArrayList<String> tuplas = new ArrayList<>();
        
        AutoScenario s = registros.get(24);
        
        System.out.println(s.getName());
        
        System.out.println("Quantidade de cenários automáticos: " + registros.size());
         
    }
  
}
