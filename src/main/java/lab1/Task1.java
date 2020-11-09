package lab1;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Task1 {


    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();

        Resource dejan = model.createResource("https://www.linkedin.com/in/dejan-slamkov/", FOAF.Person);
        Resource ttmk = model.createResource("https://github.com/SlamkovDejan/ttmk", FOAF.Project);
        Resource finkiWebpage = model.createResource("https://finki.ukim.mk/en", FOAF.Document);
        Resource venko = model.createResource("https://www.linkedin.com/in/venko-stojanov-3970751b4", FOAF.Person);

        dejan.addProperty(FOAF.name, "Dejan Slamkov", "en")
                .addProperty(FOAF.name, "Дејан Сламков", "mk")
                .addProperty(FOAF.gender, "male", "en")
                .addProperty(FOAF.gender, "машко", "mk")
                .addProperty(FOAF.birthday, "11-07")
                .addProperty(FOAF.age, "22")
                .addProperty(FOAF.made, ttmk)
                .addProperty(FOAF.pastProject, ttmk)
                .addProperty(FOAF.schoolHomepage, finkiWebpage)
                .addProperty(FOAF.knows, venko);

        venko.addProperty(FOAF.maker, ttmk).addProperty(FOAF.knows, dejan);
        ttmk.addProperty(FOAF.maker, dejan).addProperty(FOAF.maker, venko);

        System.out.println("\tPrinting with model.listStatements():");
        StmtIterator statementsIter = model.listStatements();
        while (statementsIter.hasNext()){
            Statement statement = statementsIter.nextStatement();

            String subject = statement.getSubject().toString();
            String predicate = statement.getPredicate().toString();

            RDFNode node = statement.getObject();
            String object = node instanceof Resource ? node.toString() : "\"" + node.toString() + "\"";

            System.out.printf("%s - %s - %s\n", subject, predicate, object);
        }
        System.out.println();

        String[] langs = {"RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE", "Turtle"};
        for(String lang : langs){
            System.out.printf("\tPrinting with model.write(), in %s\n", lang);
            model.write(System.out, lang);
            System.out.println();
        }

        File file = new File("C:\\Users\\Slamkov\\Desktop\\Java Projects\\Web Based Systems\\labs\\src\\main\\java\\lab1\\data\\model.xml");
        if(!file.exists()){
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                model.write(fos, "RDF/XML");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


}
