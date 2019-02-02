package com.certuit.pacheco.eliezer.examenclima;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.certuit.pacheco.eliezer.examenclima.model.City;

public class FavoritesActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);
    setupToolbar();

    City[] cities = {
        new City("Mexicali","MX"),
        new City("San Diego", "US"),
        new City("Barcelona", "ES"),
        new City("Tijuana", "MX"),
        new City("Tokyo","JP")
    };

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list_favorites);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a linear layout manager
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    FavoriteAdapter mAdapter = new FavoriteAdapter(cities);
    mRecyclerView.setAdapter(mAdapter);

  }

  @Override
  public void onBackPressed(){
    NavUtils.navigateUpFromSameTask(this);
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupToolbar(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void addAction(View v){
    Toast.makeText(getApplicationContext(), "Agregar", Toast.LENGTH_SHORT).show();
  }

  private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private City[] dataset;

    class ViewHolder extends RecyclerView.ViewHolder {
      TextView cityNameLabel;
      TextView countryCodeLabel;
      ViewHolder(View v) {
        super(v);
        cityNameLabel = v.findViewById(R.id.cityName);
        countryCodeLabel = v.findViewById(R.id.countryCode);
      }
    }

    public FavoriteAdapter(City[] dataset){
      this.dataset = dataset;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_favorites, viewGroup, false);
      final ViewHolder holder = new ViewHolder(view);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          City city = new City(holder.cityNameLabel.getText().toString(),
              holder.countryCodeLabel.getText().toString());
          Intent intent = new Intent();
          intent.putExtra("cityName",city.getName());
          intent.putExtra("countryCode", city.getCountryCode());
          setResult(RESULT_OK, intent);
          finish();
        }
      });
      return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder viewHolder, int i) {
      City city = dataset[i];
      viewHolder.cityNameLabel.setText(city.getName());
      viewHolder.countryCodeLabel.setText(city.getCountryCode());
    }

    @Override
    public int getItemCount() {
      return dataset != null? dataset.length : 0;
    }
  }
}
