package com.certuit.pacheco.eliezer.examenclima;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.certuit.pacheco.eliezer.examenclima.model.City;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FavoritesActivity extends AppCompatActivity {

  private final int ADD_REQUEST = 0;
  private FavoriteAdapter adapter;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);
    setupToolbar();

    ArrayList<City> cities = loadFavoritesFile();
    cities = cities.size() == 0? getDefaultFavorites() : cities;

    recyclerView = findViewById(R.id.list_favorites);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
    adapter = new FavoriteAdapter(cities);
    recyclerView.setAdapter(adapter);
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

      @Override
      public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        int position = viewHolder.getAdapterPosition();
        adapter.dataset.remove(position);
        recyclerView.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position,  adapter.dataset.size());
      }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
  }

  @Override
  public void onDestroy(){
    saveFavoritesFile();
    super.onDestroy();
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ADD_REQUEST) {
      if (resultCode == RESULT_OK) {
        City city = new City();
        city.setName(data.getExtras().getString("cityName"));
        city.setCountryCode(data.getExtras().getString("countryCode"));
        adapter.dataset.add(city);
        adapter.notifyDataSetChanged();
      }
    }
  }

  private void setupToolbar(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void addAction(View v){
    Intent intent = new Intent(FavoritesActivity.this, SearchActivity.class);
    startActivityForResult(intent, ADD_REQUEST);
  }

  public ArrayList<City> getDefaultFavorites(){
    City[] citiesRaw = {
        new City("Mexicali","MX"),
        new City("San Diego", "US"),
        new City("Barcelona", "ES"),
        new City("Tijuana", "MX"),
        new City("Tokyo","JP")
    };
    return new ArrayList<>(Arrays.asList(citiesRaw));
  }

  public ArrayList<City> loadFavoritesFile(){
    ArrayList<City> cities = new ArrayList<>();
    try {
      FileInputStream fis = openFileInput("favorites");
      BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
      String data = reader.readLine();
      reader.close();
      City city;
      String[] column;
      for(String row : data.split(";")){
        column = row.split(",");
        city = new City(column[0],column[1]);
        cities.add(city);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cities;
  }

  public void saveFavoritesFile(){
    try {
      FileOutputStream fos = openFileOutput("favorites", MODE_PRIVATE);
      for(City city : adapter.dataset){
        fos.write(city.getName().getBytes());
        fos.write(",".getBytes());
        fos.write(city.getCountryCode().getBytes());
        fos.write(";".getBytes());
      }
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private ArrayList<City> dataset;

    class ViewHolder extends RecyclerView.ViewHolder {
      TextView cityNameLabel;
      TextView countryCodeLabel;
      ViewHolder(View v) {
        super(v);
        cityNameLabel = v.findViewById(R.id.cityName);
        countryCodeLabel = v.findViewById(R.id.countryCode);
      }
    }

    FavoriteAdapter(ArrayList<City> dataset){
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
      City city = dataset.get(i);
      viewHolder.cityNameLabel.setText(city.getName());
      viewHolder.countryCodeLabel.setText(city.getCountryCode());
    }

    @Override
    public int getItemCount() {
      return dataset != null? dataset.size() : 0;
    }
  }
}
