package it.addvalue.endpoint;

import it.addvalue.entity.Document;

import java.util.List;


import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface DocumentEndpoint
{
   
    List<Document> getList();

    
    Document insertDoc(String name);

   
    void deleteDoc(Document item);
}
