package lab1;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;

public class Task3 {

    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();
        model.read("./src/main/java/lab1/data/hifm-dataset.ttl", "TTL");

        Property typeProp = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        RDFNode drugObject = model.getRDFNode(NodeFactory.createURI("http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/drugs"));
        Property nameProp = model.getProperty("http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/brandName");

        System.out.println("\tEvery drug in the system:");
        ResIterator drugSubjects = model.listResourcesWithProperty(typeProp, drugObject);
        ArrayList<String> drugNames = new ArrayList<>();
        while (drugSubjects.hasNext()){
            Resource drugSubject = drugSubjects.next();
            String drugName = drugSubject.getProperty(nameProp).getString();
            drugNames.add(drugName);
        }
        drugNames.stream().sorted().forEach(System.out::println);

        Resource oneDrug = model.getResource("http://purl.org/net/hifm/data#83496");
        String oneDrugName = oneDrug.getProperty(nameProp).getString();

        System.out.printf("\tDetails about %s (%s):\n", oneDrugName, oneDrug.getURI());
        Task2.printResourcePropertiesWithValues(oneDrug);

        System.out.printf("\tDrugs similar to %s (%s):\n", oneDrugName, oneDrug.getURI());
        Property similarToProp = model.getProperty("http://purl.org/net/hifm/ontology#similarTo");
        StmtIterator similarDrugs = oneDrug.listProperties(similarToProp);
        while (similarDrugs.hasNext()){
//            String similarDrugURI = similarDrugs.nextStatement().getObject().toString();
//            String similarDrugName = model.getResource(similarDrugURI).getProperty(nameProp).getString();
            String similarDrugName = similarDrugs.nextStatement().getResource().getProperty(nameProp).getString();
            System.out.println(similarDrugName);
        }

        Property priceProp = model.getProperty("http://purl.org/net/hifm/ontology#refPriceWithVAT");
        double price = oneDrug.getProperty(priceProp).getDouble();
        System.out.printf("\tSimilar drugs to %s (%s) - %.2fMKD:\n", oneDrugName, oneDrug.getURI(), price);
        similarDrugs = oneDrug.listProperties(similarToProp);
        while (similarDrugs.hasNext()){
//            String similarDrugURI = similarDrugs.nextStatement().getObject().toString();
//            Resource similarDrug = model.getResource(similarDrugURI);
            Resource similarDrug = similarDrugs.nextStatement().getResource();
            String similarDrugName = similarDrug.getProperty(nameProp).getString();
            double similarDrugPrice = similarDrug.getProperty(priceProp).getDouble();
            System.out.printf("%s (%.2fMKD)\n", similarDrugName, similarDrugPrice);
        }

    }

}
