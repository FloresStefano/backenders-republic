package it.addvalue.controller;

import it.addvalue.entity.Item;
import it.addvalue.service.ItemService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/item")
public class ItemController
{
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/getItemList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Item>> getItemList()
    {
        List<Item> result = itemService.getItemList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
