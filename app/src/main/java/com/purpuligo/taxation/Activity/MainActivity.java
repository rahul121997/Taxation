package com.purpuligo.taxation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.snackbar.Snackbar;
import com.purpuligo.taxation.Adapter.StatusListAdapter;
import com.purpuligo.taxation.Fragment.HomeFragment;
import com.purpuligo.taxation.Fragment.PrivacyFragment;
import com.purpuligo.taxation.Fragment.TaxSlabFragment;
import com.purpuligo.taxation.Fragment.StatusFragment;
import com.purpuligo.taxation.Global.NetworkState;
import com.purpuligo.taxation.R;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    private MeowBottomNavigation bottomNavigation;
    private int counter = 0;
    private NetworkState networkState;

    StatusListAdapter statusListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkState = new NetworkState();


        //Assign variable
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottom_navigation);
        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home_icon_new));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.history_icon_new));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.info_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.privacy_icon_new));


        bottomNavigationBar();

        //set notification count
        //bottomNavigation.setCount(1,"10");
        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(),
//                        "Your Clicked"+item.getId()
//                        ,Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(),
//                        "Your Releaseted "+item.getId()
//                        ,Toast.LENGTH_SHORT).show();
            }
        });

//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new StatusFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1) {
            Snackbar.make(bottomNavigation, "Do you really want exit", Snackbar.LENGTH_LONG).show();
        } else if (counter == 2) {
            finish();
        }
    }

    private void bottomNavigationBar() {

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;
                Activity activity = null;
                //Check condition
                switch (item.getId()) {
                    case 1:
                        //when id is 1
                        //Initialize Home fragment
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        //when id is 2
                        //Initialize Status fragment
                        fragment = new StatusFragment();
                        break;
                    case 3:
                        //when id is 3
                        //Initialize Profile fragment
                        fragment = new TaxSlabFragment();
                        break;
                    case 4:
                        fragment = new PrivacyFragment();
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        if (networkState.isNetworkAvailable(MainActivity.this)) {
            //Replace fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        } else {
            Toast.makeText(MainActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }
    }

}