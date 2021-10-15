package de.Marcel1802.eventbot.service;

import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.ResponseMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonService {

    public Response getPersonByName(String pname){

        List<Person> returnPersons = Person.find("firstname LIKE ?1 OR lastname LIKE ?1 OR gamertag LIKE ?1", pname).list();

       if (returnPersons.isEmpty()){
           return Response.noContent().build(); // 204 No Content
       }
       else {
           return Response.ok().entity(returnPersons).build(); // 200 OK + Liste
       }
    }

    public Response getPersonByID(UUID id){
        Person p = Person.findById(id);

        if (p == null){
            return Response.status(400).entity(new ResponseMessage("Person cannot be found")).build();
        }

        return Response.ok().entity(p).build();
    }

}
