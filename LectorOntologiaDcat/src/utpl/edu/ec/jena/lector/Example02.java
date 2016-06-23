/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturaexcel;

import java.io.BufferedReader;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.rdf.model.RDFWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;

/**
 *
 * @author luischalan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jachicaiza
 */
public class Example02 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) {
        Example02 example = new Example02();
        example.leerCSV();
    }

    public void leerCSV() {
        String separador = ",";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("datos.csv"));
            String line = br.readLine();
            // create an empty Model
            Model model = ModelFactory.createDefaultModel();

            //definición del fichero donde insertaremos los datos RDF
            File f = new File("/Users/luischalan/Desktop/people.rdf");
            FileOutputStream os = new FileOutputStream(f);

            // Create people
            String dataPrefix = "http://example.org/data/";
            int p = 0;
            while (null != line) {
                String[] fields = line.split(separador);
                for (int i = 0; i < fields.length; i++) {
                    
                    Resource person01;
                    person01 = model.createResource(dataPrefix + "person" + p)
                            .addProperty(RDF.type, VCARD.AGENT)
                            .addProperty(VCARD.FN, fields[0] + " " + fields[1])
                            .addProperty(VCARD.Given, fields[0])
                            .addProperty(VCARD.Family, fields[1])
                            .addProperty(RDF.type, FOAF.Person)
                            .addProperty(FOAF.name, fields[0] + " " + fields[1])
                            .addProperty(FOAF.aimChatID, fields[4])
                            .addProperty(FOAF.currentProject, fields[5])
                            .addProperty(FOAF.interest, fields[6])
                            .addProperty(FOAF.topic_interest, fields[7])
                            .addProperty(FOAF.topic_interest, fields[8]);
                    
                    /*/ Instituto Educativo
                    Resource institucion;
                    institucion = model.createResource(dataPrefix + "instituto")
                            .addProperty(RDF.type, FOAF.Organization)
                            .addProperty(VCARD.Given, fields[3]);

                    // Instituto Educativo
                    if (fields[9].indexOf("Película")!= -1) {
                        Resource pelicula;
                        pelicula = model.createResource(dataPrefix + "movie")
                                .addProperty(RDF.type, )
                                .addProperty(VCARD.Given, fields[3]);
                    } else {
                        
                    }
                    Resource favorite;
                    favorite = model.createResource(dataPrefix + "favorite")
                            .addProperty(RDF.type, FOAF.Organization)
                            .addProperty(VCARD.Given, fields[3]);
                    /*/// Concept SBC
                    Resource SBC = model.createResource(dataPrefix + "Subject:SBC")
                            .addProperty(RDFS.label, "Sistemas Basados en Conocimiento")
                            .addProperty(RDF.type, SKOS.Concept);

                    // Agregar concepto a recursos creados
                    model.add(person01, DCTerms.subject, SBC);

                    // list the statements in the Model
                    StmtIterator iter = model.listStatements();
                    // print out the predicate, subject and object of each statement
                    while (iter.hasNext()) {
                        Statement stmt = iter.nextStatement();  // get next statement
                        Resource subject = stmt.getSubject();     // get the subject
                        Property predicate = stmt.getPredicate();   // get the predicate
                        RDFNode object = stmt.getObject();      // get the object

                        System.out.print(subject.toString());
                        System.out.print(" " + predicate.toString() + " ");
                        if (object instanceof Resource) {
                            System.out.print(object.toString());
                        } else {
                            // object is a literal
                            System.out.print(" \"" + object.toString() + "\"");
                        }

                        System.out.println(" .");
                    }

                    // now write the model in XML form to a file
                    //System.out.println("MODELO ------");
                    //model.write(System.out, "RDF/XML-ABBREV");
                    // Save to a file
                    RDFWriter writer = model.getWriter("RDF/XML"); //RDF/XML
                    writer.write(model, os, dataPrefix);
                }
                p++;
                line = br.readLine();
            }

        } catch (Exception e) {

        }
    }
}
