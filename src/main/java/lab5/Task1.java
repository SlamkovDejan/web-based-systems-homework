package lab5;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;

public class Task1 {

    private static final String myFoafDocUri = "https://raw.githubusercontent.com/SlamkovDejan/web-based-systems-homework/master/src/main/java/lab5/data/foaf.ttl";

    public static void main(String[] args) {

        Model myFoaf = ModelFactory.createDefaultModel();
        myFoaf.read(myFoafDocUri, "TURTLE");

        Resource me = myFoaf.getResource(String.format("%s#me", myFoafDocUri));

        List<String> friends = new ArrayList<>();
        StmtIterator iter = me.listProperties(FOAF.knows);
        while (iter.hasNext()){
            Resource friend = iter.nextStatement().getResource();
            String friendFoafDocUri = friend.getProperty(RDFS.seeAlso).getObject().toString();

            Model friedModel = ModelFactory.createDefaultModel();
            friedModel.read(friendFoafDocUri, "TURTLE");
            myFoaf = myFoaf.union(friedModel);

            friends.add(friendFoafDocUri);
        }

        for(String friendUri : friends){
            Resource friend = myFoaf.getResource(friendUri.concat("#me"));
            String name = friend.getProperty(FOAF.givenname).getObject().toString();
            String surname = friend.getProperty(FOAF.family_name).getObject().toString();
            String email = friend.getProperty(FOAF.mbox_sha1sum).getObject().toString();
            String imgURL = friend.getProperty(FOAF.depiction).getObject().toString();
            String homepage = friend.getProperty(FOAF.homepage).getObject().toString();
            System.out.printf("%s %s %s %s %s %s\n", friendUri, name, surname, email, homepage, imgURL);
        }

    }

}
