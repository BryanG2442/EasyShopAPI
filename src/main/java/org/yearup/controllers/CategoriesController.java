package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(ProductDao productDao, CategoryDao categoryDao){
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    // add the appropriate annotation for a get action
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        // find and return all categories
        try {
            return categoryDao.getAllCategories();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error has occured");
        }
    }

    // add the appropriate annotation for a get action
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        try {
            var category = categoryDao.getById(id);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);


            return category;
        }
        catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Invalid ID");
        }
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return null;
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return null;
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
        try {
            var category = categoryDao.getById(id);

            if (category == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            categoryDao.delete(id);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There has been a server issue");
        }

    }
}