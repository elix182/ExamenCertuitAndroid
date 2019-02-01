package com.certuit.pacheco.eliezer.examenclima;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_weather);
    setupToolbar();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    if(drawerLayout.isDrawerOpen(Gravity.START)) {
      drawerLayout.closeDrawer(Gravity.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    switch(item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(Gravity.START);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupToolbar(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.baseline_menu_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setupNavigationMenu();
  }

  private void setupNavigationMenu(){
    final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    NavigationView listView = findViewById(R.id.left_sidebar);
    listView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
          case R.id.favorite_menu_button:
            Toast.makeText(getApplicationContext(),"Favoritos",Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(Gravity.START);
            break;
          case R.id.search_menu_button:
            Toast.makeText(getApplicationContext(),"Buscar",Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(Gravity.START);
            break;
          default: break;
        }
        return true;
      }
    });
  }
}
