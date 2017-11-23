package shop.ineed.app.ineed.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.ProductJsonParser;
import shop.ineed.app.ineed.util.Highlight;

/**
 * Created by jose on 10/27/17.
 *
 * Classe da plataforma Algolia, responsável pela marcação do temos buscado.
 */

public class SearchResultsJsonParser {
    private ProductJsonParser productParser = new ProductJsonParser();
    private Map<String, Highlight> highlights = new HashMap<>();

    public List<Product> parseResults(JSONObject object) {
        if (object == null)
            return null;

        List<Product> results = new ArrayList<>();
        JSONArray hits = object.optJSONArray("hits");
        if (hits == null)
            return null;

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null)
                continue;

            Product product = null;
            try {
                product = productParser.parse(hit);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (product == null)
                continue;

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null)
                continue;
            JSONObject highlightTitle = highlightResult.optJSONObject("name");
            if (highlightTitle == null)
                continue;
            String value = highlightTitle.optString("value");
            if (value == null)
                continue;
            //HighlightedResult result = new HighlightedResult(product, highlights);
            //result.addHighlight("name", new Highlight("name", value));
            results.add(product);
        }
        return results;
    }
}
