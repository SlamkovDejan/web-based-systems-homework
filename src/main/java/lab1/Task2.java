package lab1;

import org.apache.jena.rdf.model.*;

public class Task2 {

    public static void printResourcePropertiesWithValues(Resource resource){
        StmtIterator iterator = resource.listProperties();
        while (iterator.hasNext()){
            Statement curr = iterator.nextStatement();

            String propertyId = curr.getPredicate().toString();
            RDFNode node = curr.getObject();
            String objectId = node instanceof Resource ? node.toString() : "\"" + node.toString() + "\"";

            System.out.printf("%s : %s\n", propertyId, objectId);
        }
    }

    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();
        model.read("./src/main/java/lab1/data/model.xml", "RDF/XML");
        model.write(System.out, "TTL");
        System.out.println();

        Resource dejan = model.getResource("https://www.linkedin.com/in/dejan-slamkov/");
        System.out.printf("\tProperties of the entity '%s':\n", dejan.getURI());
        printResourcePropertiesWithValues(dejan);

    }

}
