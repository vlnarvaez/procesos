/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utpl.edu.ec.jena.lector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import java.util.logging.Logger;

/**
 *
 * @author luischalan
 */
//paquete necesarios para utilizer jena y realizar la lectura de un archivo.rdf
public class LeerJenaRDF extends Object {

    static final String inputFileName = "dcat.rdf";
    static String dcatURI = "";
    public static void main(String args[]) {
        
        //variables para lectura del archivo.rdf
        System.out.println("ONTOLOGIA DCAT");
        System.out.println("Filtro: \"es\"");
        File entrada = null;
        FileReader fr = null;
        BufferedReader br = null;
        
        LeerJenaRDF searchEngine = new LeerJenaRDF();
        String[] matches = null;

        try {
            // lectura del archivo .rdf
            entrada = new File("dcat.rdf");
            fr = new FileReader(entrada);
            br = new BufferedReader(fr);
            //Lectura linea por linea del archivo salida.rdf
            String texto = br.readLine();
            //Encontrar sujeto por patron de busqueda
            while ((texto = br.readLine()) != null) {
                matches = searchEngine.search("=\"http://www.w3.org/ns/dcat#", br.readLine());
                searchEngine.printMatches(matches);
            }
            //excepciones capturadas si no se encuentra el archivo .rdf
        } catch (Exception e) {
            System.out.print("Error: " + e);
        } finally {
            //se cierra la conexion de la lectura del archivo
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    public String[] search(String regex, String onText) {
        Pattern regPatt = Pattern.compile(regex);
        Matcher regMatch = regPatt.matcher(onText);
        ArrayList<String> matches = new ArrayList<>();
        String[] matchesArray;
        int indice, indiceComilla;

        while (regMatch.find()) {
            indice = onText.indexOf('"', 0);
            indiceComilla = onText.indexOf('"', indice + 1);

            //Obtener Sujeto
            String frase2 = onText.substring(indice + 1, indiceComilla);
            String match = frase2;
            matches.add(match);
        }
        matchesArray = new String[matches.size()];
        matches.toArray(matchesArray);
        return matchesArray;
    }

    public void printMatches(String[] matches) {
        int matchesCount = matches.length;

        for (int i = 0; i < matchesCount; i++) {

            dcatURI = matches[i];
            Model model = ModelFactory.createDefaultModel();
            //Usar FileManager para encontrar el archivo
            InputStream in = FileManager.get().open(inputFileName);
            if (in == null) {
                throw new IllegalArgumentException("File: " + inputFileName + " not found");
            }
            //Leer archivo RDF
            model.read(new InputStreamReader(in), "");

            Resource dcat = model.getResource(dcatURI);
            StmtIterator iter = dcat.listProperties();

            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Sujeto: " + dcatURI);
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("            Predicado                                                   Objeto");

            //Listado
            try {
                //Imprimir Predicado y Objeto
                while (iter.hasNext()) {
                    
                    Literal value = null;
                    //Obtener el siguiente statement
                    Statement stmt = iter.nextStatement();
                    Property predicate = stmt.getPredicate();
                    RDFNode object = stmt.getObject();

                    if (object.isLiteral()) {
                        if (value == null) {
                            value = object.asLiteral();
                        }
                        if (object.asLiteral().getLanguage().equals("es")) {
                            System.out.println(" " + predicate.toString() + " | " + object.asLiteral().getValue().toString() + ".");
                        }
                    }
                }
                System.out.println("\n");
            } catch (Exception e) {
            }
        }
    }
}
