package com.udacity.sandwichclub.utils;

import android.util.Log;
import com.udacity.sandwichclub.model.Sandwich;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

  private static final String TAG = JsonUtils.class.getSimpleName();

  public static Sandwich parseSandwichJson(String json) {
    Log.d(TAG, "JSON We are dealing with: " + json);

    Sandwich sandwich;

    try {
      JSONObject sandwichJSON = new JSONObject(json);

      //Get Main Name
      JSONObject sandwichNameObject = sandwichJSON.getJSONObject("name");
      String sandMainName = sandwichNameObject.getString("mainName");

      // Check if any value is empty.  If it is, set to null for future processing.
      // Get Origin.
      String originString = sandwichJSON.getString("placeOfOrigin");
      String sandOrigin = originString.equals("") ? null : originString;

      //Get Description
      String descriptionString = sandwichJSON.getString("description");
      String sandDescription = descriptionString.equals("") ? null : descriptionString;

      //Get Image
      String imageUrlString = sandwichJSON.getString("image");
      String sandImageUrl = imageUrlString.equals("") ? null : imageUrlString;

      //Get Ingredients (this will be set to null if empty / length 0
      List<String> sandIngredients = parseJsonArray(sandwichJSON.getJSONArray("ingredients"));

      //Get Other Names (this will be set to null if empty / length 0
      List<String> sandOtherNames = parseJsonArray(sandwichNameObject.getJSONArray("alsoKnownAs"));

      //Create new Sandwich
      sandwich =
          new Sandwich(
              sandMainName,
              sandOtherNames,
              sandOrigin,
              sandDescription,
              sandImageUrl,
              sandIngredients);

      Log.d(TAG, sandwich.toString());
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return sandwich;
  }

  private static List<String> parseJsonArray(JSONArray listOfOtherNames) {
    if (listOfOtherNames == null || listOfOtherNames.length() == 0) {
      return null;
    }

    ArrayList<String> listOfItems = new ArrayList<>();

    for (int i = 0; i <= listOfOtherNames.length(); i++) {
      try {
        listOfItems.add(listOfOtherNames.get(i).toString());
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return listOfItems;
  }
}
