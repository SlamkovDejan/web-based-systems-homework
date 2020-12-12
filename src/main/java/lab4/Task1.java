package lab4;

import org.apache.jena.rdf.model.*;

public class Task1 {

    private static final String dbo = "http://dbpedia.org/ontology/";
    private static final String dbr = "http://dbpedia.org/resource/";
    private static final String dbData = "http://dbpedia.org/data/";
    private static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String geo = "http://www.w3.org/2003/01/geo/wgs84_pos#";

    private static String getDataURL(String resourceURI){
        return resourceURI.replaceFirst("resource", "data").concat(".ttl");
    }

    public static void main(String[] args) {

        Model startingPoint = ModelFactory.createDefaultModel();
        startingPoint.read(String.format("%s%s", dbData, "Skopje.ttl"), "TURTLE");

        Resource skopje = startingPoint.getResource(String.format("%s%s", dbr, "Skopje"));
        Property country = startingPoint.getProperty(String.format("%s%s", dbo, "country"));
        Resource macedonia = (Resource) skopje.getProperty(country).getObject();
        //System.out.println(macedonia);

        String macedoniaDataUrl = getDataURL(macedonia.getURI());
        Model macedoniaModel = ModelFactory.createDefaultModel();
        macedoniaModel.read(macedoniaDataUrl, "TURTLE");
        macedonia = macedoniaModel.getProperty(String.format("%s%s", dbr, "Macedonia"));

        Resource gevgelija = macedoniaModel.getResource(String.format("%s%s", dbr, "Gevgelija"));
        //System.out.println(gevgelija);

        String gevgelijaDataUrl = getDataURL(gevgelija.getURI());
        Model gevgelijaModel = ModelFactory.createDefaultModel();
        gevgelijaModel.read(gevgelijaDataUrl, "TURTLE");
        gevgelija = gevgelijaModel.getResource(String.format("%s%s", dbr, "Gevgelija"));

        Property label = gevgelijaModel.getProperty(String.format("%s%s", rdfs, "label"));
        Property population = gevgelijaModel.getProperty(String.format("%s%s", dbo, "populationTotal"));
        Property postalCode = gevgelijaModel.getProperty(String.format("%s%s", dbo, "postalCode"));
        Property longitude = gevgelijaModel.getProperty(String.format("%s%s", geo, "long"));
        Property latitude = gevgelijaModel.getProperty(String.format("%s%s", geo, "lat"));

        System.out.printf(
                "%s %s %s %s %s %s\n",
                gevgelija.getProperty(label, "en").getString(),
                macedonia.getProperty(label, "en").getString(),
                gevgelija.getProperty(population).getString(),
                gevgelija.getProperty(postalCode).getString(),
                gevgelija.getProperty(longitude).getString(),
                gevgelija.getProperty(latitude).getString()
        );
    }

}
