package lab4;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFParser;

public class Task2 {

    private static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String dcterms = "http://purl.org/dc/terms/";

    public static void main(String[] args) {

        Model localDrugs = ModelFactory.createDefaultModel();
        localDrugs.read("src/main/java/lab4/data/hifm-dataset-bio2rdf.ttl", "TURTLE");
        Resource oneDrug = localDrugs.getResource("http://purl.org/net/hifm/data#83496");
        Property seeAlso = localDrugs.getProperty(String.format("%s%s", rdfs, "seeAlso"));

        StmtIterator iter = oneDrug.listProperties(seeAlso);
        while (iter.hasNext()){
            Resource similarDrug = iter.nextStatement().getResource();

            Model remoteDrugs = ModelFactory.createDefaultModel();
            RDFParser.source(similarDrug.getURI())
                    .httpAccept("text/turtle")
                    .parse(remoteDrugs.getGraph());

            Resource currDrug = remoteDrugs.getResource(similarDrug.getURI());
            Property dcTitle = remoteDrugs.getProperty(String.format("%s%s", dcterms, "title"));
            Property dcDescription = remoteDrugs.getProperty(String.format("%s%s", dcterms, "description"));

            System.out.printf(
                    "%s %s\n",
                    currDrug.getProperty(dcTitle).getString(),
                    currDrug.getProperty(dcDescription).getString()
            );
        }

    }

}
