package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

  public static final String EXTRA_POSITION = "extra_position";
  private static final int DEFAULT_POSITION = -1;
  private static final String TAG = DetailActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ImageView ingredientsIv = findViewById(R.id.image_iv);

    Intent intent = getIntent();
    if (intent == null) {
      Log.d(TAG, "Failed when getting intent");
      closeOnError();
    }

    int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
    if (position == DEFAULT_POSITION) {
      // EXTRA_POSITION not found in intent
      Log.d(TAG, "Failed when getting position");
      closeOnError();
      return;
    }

    String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
    String json = sandwiches[position];
    Sandwich sandwich = JsonUtils.parseSandwichJson(json);
    if (sandwich == null) {
      // Sandwich data unavailable
      Log.d(TAG, "Failed when getting sandwich data");
      closeOnError();
      return;
    }

    populateUI(sandwich);
    Picasso.with(this)
        .load(sandwich.getImage())
        .into(ingredientsIv);

    setTitle(sandwich.getMainName());
  }

  /**
   * Close this Activity and provide message if unable to receive Sandwich Information
   */
  private void closeOnError() {
    finish();
    Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Populates the UI with the details of the Sandwich
   *
   * @param sandwich Sandwich Object containing the data to populate
   */
  private void populateUI(Sandwich sandwich) {
    // References for Labels
    TextView akaLabel = findViewById(R.id.aka_label);
    TextView originLabel = findViewById(R.id.origin_label);
    TextView ingredientsLabel = findViewById(R.id.ingredients_label);
    TextView descriptionLabel = findViewById(R.id.description_label);

    // References for views
    TextView akaTextView = findViewById(R.id.also_known_tv);
    TextView originTextView = findViewById(R.id.origin_tv);
    TextView ingredientsTextView = findViewById(R.id.ingredients_tv);
    TextView descriptionTextView = findViewById(R.id.description_tv);

    // Remove any item if the content is empty for that item

    if (sandwich.getAlsoKnownAs() == null) {
      akaLabel.setVisibility(View.GONE);
      akaTextView.setVisibility(View.GONE);
    } else {
      addListItems(akaTextView, sandwich.getAlsoKnownAs());
    }

    if (sandwich.getPlaceOfOrigin() == null) {
      originLabel.setVisibility(View.GONE);
      originTextView.setVisibility(View.GONE);
    } else {
      originTextView.setText(sandwich.getPlaceOfOrigin());
    }

    if (sandwich.getIngredients() == null) {
      ingredientsLabel.setVisibility(View.GONE);
      ingredientsTextView.setVisibility(View.GONE);
    } else {
      addListItems(ingredientsTextView, sandwich.getIngredients());
    }

    if (sandwich.getDescription() == null) {
      descriptionLabel.setVisibility(View.GONE);
      descriptionTextView.setVisibility(View.GONE);
    } else {
      descriptionTextView.setText(sandwich.getDescription());
    }
  }

  /**
   * Converts a List of Items to Individual Strings to add to TextView
   *
   * @param view The View to Add the Items To
   * @param itemsToAdd The Items to add to the View
   */
  private void addListItems(TextView view, List<String> itemsToAdd) {
    //If we only have one item, no need to add the comma after it.  Just add it to the list.
    for (int i = 0; i < itemsToAdd.size(); i++) {
      if (i == 0) {
        view.append(itemsToAdd.get(i));
      } else {
        view.append(", " + itemsToAdd.get(i));
      }
    }
  }
}
