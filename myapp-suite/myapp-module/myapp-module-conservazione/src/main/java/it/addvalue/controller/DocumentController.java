package it.addvalue.controller;

import it.addvalue.common.Speed;
import it.addvalue.entity.Document;
import it.addvalue.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/document")
public class DocumentController
{
    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/search/getList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getList() throws InterruptedException
    {
        long start = Speed.start();
        documentService.getList();

        start = Speed.stop(start);
        return new ResponseEntity<>(start, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/testPost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> testPost(@RequestBody String name) throws InterruptedException
    {
        long start = Speed.start();
        Document doc = documentService.insertDoc(name);

        start = Speed.stop(start);
        documentService.deleteDoc(doc);
        return new ResponseEntity<>(start, HttpStatus.OK);
    }
}
