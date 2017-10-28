package shop.ineed.app.ineed.io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.domain.Product;

/**
 * Created by jose on 10/27/17.
 *
 * ArrayAdapter para gerenciar os elementos da pesquisa de produtos.
 *
 * Classe responsável pelo parser de objetos JSON, retornando um objeto
 * Java correspondente ao seu tipo, no nosso caso um objeto do tipo Product.
 */

public class ProductJsonParser {

    public Product parse(JSONObject object) throws JSONException {
        if(object == null)
            return null;

        Product product = new Product();

        JSONArray category = object.getJSONArray("categories");
        List<String> categoryList = new ArrayList<>();
        for(int i=0; i < category.length(); i++){
            categoryList.add(category.getString(i));
        }
        JSONArray jArray = object.getJSONArray("pictures");
        List<String> picture = new ArrayList<>();
        for(int i=0; i < jArray.length(); i++){
            picture.add(jArray.getString(i));
        }

        product.setCategories(categoryList);
        product.setDescription(object.optString("description"));
        product.setName(object.optString("name"));
        product.setPictures(picture);
        product.setPrice(object.optDouble("price"));
        product.setStore(object.optString("store"));
        product.setId(object.optString("objectID"));

        if(product != null){
            return product;
        }else{
            return null;
        }
    }
}
