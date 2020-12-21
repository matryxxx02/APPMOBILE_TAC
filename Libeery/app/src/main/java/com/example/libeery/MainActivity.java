package com.example.libeery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.SparseArray;

import com.example.libeery.view.FavoritesFragment;
import com.example.libeery.view.ProfileFragment;
import com.example.libeery.view.SearchBeerFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private final static String FRAGMENT_STORED_KEY = "Fragment_Stored";

    private ChipNavigationBar navBar;
    private Fragment currentFragment;
    private SparseArray<Fragment> fragmentArray;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(currentFragment != null){
            getSupportFragmentManager().putFragment(outState, FRAGMENT_STORED_KEY, currentFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentArray = new SparseArray<>(3);
        navBar = findViewById(R.id.navBar);

        if(savedInstanceState != null){
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STORED_KEY);
        }else{
            currentFragment = new SearchBeerFragment();
            navBar.setItemSelected(R.id.beers, true);
        }
        replaceFragment(currentFragment);

        navBar.setOnItemSelectedListener(id -> {
            switch(id){
                case R.id.beers:
                    if(fragmentArray.get(0) == null) {
                        currentFragment = SearchBeerFragment.newInstance();
                        fragmentArray.append(0, currentFragment);
                    }else
                        currentFragment = fragmentArray.get(0);
                    break;
                case R.id.favorites:
                    if(fragmentArray.get(1) == null) {
                        currentFragment = FavoritesFragment.newInstance();
                        fragmentArray.append(1, currentFragment);
                    }else
                        currentFragment = fragmentArray.get(1);
                    break;
                case R.id.profile:
                    if(fragmentArray.get(2) == null) {
                        currentFragment = ProfileFragment.newInstance();
                        fragmentArray.append(2, currentFragment);
                    }else
                        currentFragment = fragmentArray.get(2);
                    break;
            }

            if(currentFragment != null )
                replaceFragment(currentFragment);
            else
                System.out.println("Error in creating fragment");
        });
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newFragment);
        ft.commit();
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
    }

}