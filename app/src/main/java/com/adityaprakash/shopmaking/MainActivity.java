package com.adityaprakash.shopmaking;

import android.content.Intent;
import android.os.Bundle;

import com.adityaprakash.shopmaking.DayWise.DateActivity;
import com.adityaprakash.shopmaking.DayWise.DayWiseCustomers;
import com.adityaprakash.shopmaking.Fragments.DateWiseFragment;
import com.adityaprakash.shopmaking.Fragments.InvoiceFragment;
import com.adityaprakash.shopmaking.Fragments.SearchFragment;
import com.adityaprakash.shopmaking.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private View navHeader;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);



         drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializer();



        //set home as default

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Items in Store");
        fragmentTransaction.commit();

    }

    private void initializer() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if(item.getItemId() == R.id.action_logout){
//            mAuth.signOut();
//            transferToLogin();
//        }
//        if(item.getItemId() == R.id.action_settings){
//
//            transferToBarcodeActivity();
//
//        }
//        if(item.getItemId() == R.id.action_items){
//
//            transferToItemsActivity();
//
//        }
//        if(item.getItemId() == R.id.action_search){
//
//            transferToSearchActivity();
//
//        }
//        if(item.getItemId() == R.id.action_invoice){
//
//            transferTocustomerActivity();
//
//        }
//        if(item.getItemId() == R.id.day_wise_customer){
//
//            trasferToDayWiseCustomerActivity();
//
//        }
//
//
//
//
//        return true;
//    }

    private void trasferToDayWiseCustomerActivity() {

        Intent dayWiseCustomer = new Intent(MainActivity.this, DateActivity.class);
        startActivity(dayWiseCustomer);

    }

    private void transferTocustomerActivity() {
        Intent customer = new Intent(MainActivity.this,CustomerActivity.class);
        startActivity(customer);

    }

    private void transferToSearchActivity() {
        Intent searchIntent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(searchIntent);

    }

    private void transferToItemsActivity() {
        Intent itemIntent = new Intent(MainActivity.this,ItemsActivity.class);
        startActivity(itemIntent
        );
    }

    private void transferToBarcodeActivity() {
        Intent barIntent = new Intent(MainActivity.this,BarcodeScannerActivity.class);
        startActivity(barIntent);
    }

    private void transferToLogin() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            transferToLogin();
        }
        if(item.getItemId() == R.id.action_barcode){

            transferToBarcodeActivity();

        }
        if(item.getItemId() == R.id.action_items){

            transferToHomeFragments();

        }
//        if(item.getItemId() == R.id.action_search){
//
//            transferToSearchActivity();
//
//        }
//        if(item.getItemId() == R.id.action_invoice){
//
//            transferTocustomerActivity();
//
//        }
//        if(item.getItemId() == R.id.day_wise_customer){
//
//            trasferToDayWiseCustomerActivity();
//
//        }



        return super.onOptionsItemSelected(item);
    }

    private void transferToHomeFragments() {
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, "Home");
        fragmentTransaction.commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Home");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_search) {
            SearchFragment fragment = new SearchFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Search");
            fragmentTransaction.commit();

        }  else if (id == R.id.nav_invoice) {
            InvoiceFragment fragment = new InvoiceFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Search");
            fragmentTransaction.commit();


        } else if (id == R.id.nav_sell) {
            DateWiseFragment fragment = new DateWiseFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Search");
            fragmentTransaction.commit();


        }


        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

//    private void displaySelectedFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//    }
}
