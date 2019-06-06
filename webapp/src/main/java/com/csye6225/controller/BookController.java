package com.csye6225.controller;

import com.csye6225.models.Book;
import com.csye6225.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController("BookController")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    //private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/book", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String GetBooks(HttpServletRequest request, HttpServletResponse response, @RequestBody Book bookReq){

        List<Book> books = (List)bookRepository.findAll();
        String json = new Gson().toJson(books);

        return json;
    }

    // Post Method book Create

    @RequestMapping(value = "/book", method = RequestMethod.POST, produces = "application/json")

    @ResponseBody
    public String Book(HttpServletRequest request, HttpServletResponse response, @RequestBody Book bookReq){

        JsonObject jsonObject = new JsonObject();
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id.toString());
        book.setTitle(bookReq.getTitle());
        book.setAuthor(bookReq.getAuthor());
        book.setIsbn(bookReq.getIsbn());
        book.setQuantity(bookReq.getQuantity());
        bookRepository.save(book);
        String json = new Gson().toJson(book);
        response.setStatus(HttpServletResponse.SC_CREATED);

        return json;
    }

    // Put Method book Update

    @RequestMapping(value = "/book", method = RequestMethod.PUT, produces = "application/json")

    @ResponseBody
    public String BookUpdate(HttpServletRequest request, HttpServletResponse response, @RequestBody Book bookReq){

        JsonObject jsonObject = new JsonObject();
        Book book = bookRepository.findById(bookReq.getId());
        if (book!=null) {
            book.setTitle(bookReq.getTitle());
            book.setAuthor(bookReq.getAuthor());
            book.setIsbn(bookReq.getIsbn());
            book.setQuantity(bookReq.getQuantity());
            bookRepository.save(book);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return jsonObject.toString();
    }



    // Get a book method

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)

    @ResponseBody


    public String getBooksById(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

       Book book= bookRepository.findById(id);
        JsonObject jsonObject = new JsonObject();
       String json=new Gson().toJson(book);
        if (book!=null) {

            response.setStatus(HttpServletResponse.SC_OK);
            return json;
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return jsonObject.toString();
        }


    }

    // Delete a book

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE, produces="application/json")

    @ResponseBody
    public String BookDelete(@PathVariable("id") String id, HttpServletResponse response) throws IOException{

      Book book= bookRepository.findById(id);
      JsonObject jsonObject=new JsonObject();
      if(book!=null){
      bookRepository.delete(book);
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        }
      else{

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return jsonObject.toString();

    }





}
