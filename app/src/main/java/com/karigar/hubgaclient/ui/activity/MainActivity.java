package com.karigar.hubgaclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karigar.hubgaclient.R;
import com.karigar.hubgaclient.ui.Callback;
import com.karigar.hubgaclient.ui.fragment.BarCodeFragment;
import com.karigar.hubgaclient.ui.fragment.CategoryFragment;
import com.karigar.hubgaclient.ui.fragment.CruiseFragment;
import com.karigar.hubgaclient.ui.fragment.EventFragment;
import com.karigar.hubgaclient.ui.fragment.OfferFragment;
import com.karigar.hubgaclient.ui.fragment.PaymentFrament;
import com.karigar.hubgaclient.ui.fragment.SettingFragment;
import com.karigar.hubgaclient.ui.fragment.TicketFragment;
import com.karigar.hubgaclient.ui.fragment.UpdateUserFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback, View.OnClickListener {
    FragmentManager fragmentManager;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    RelativeLayout titleLayout;
    EditText searcheditText;
    ImageView searchImage;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView img_home;
    String currentfragment = "";
    Fragment fragment = null;
    FrameLayout searchText_layout;
    OfferFragment offerFragment;
    boolean callFromTicket = false;
    Bundle bundle;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);


        if (appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        img_home = (ImageView) findViewById(R.id.toolbar_home);
        tv_title=(TextView)findViewById(R.id.Vtext);
        searcheditText = (EditText) findViewById(R.id.searchtext);
        searchText_layout = (FrameLayout) findViewById(R.id.layout_toolbar_editext);
        searchImage = (ImageView) findViewById(R.id.img_search);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuicongray));

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        LinearLayout drawer_ticket = (LinearLayout) navigationView.findViewById(R.id.drawer_ticket);
        LinearLayout drawer_settting = (LinearLayout) navigationView.findViewById(R.id.drawer_setting);
        LinearLayout drawer_logout = (LinearLayout) navigationView.findViewById(R.id.logout_layout);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
/*// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// replace the FrameLayout with new Fragment
        Fragment fragment = new CategoryFragment(this);
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit(); // save the changes*/
        ChangeFragment("CategoryFragment", null);
        img_home.setOnClickListener(this);
        searchImage.setOnClickListener(this);
        drawer_ticket.setOnClickListener(this);
        drawer_settting.setOnClickListener(this);
        drawer_logout.setOnClickListener(this);
        ImageView drawer_close = (ImageView) navigationView.findViewById(R.id.drawer_off);
        drawer_close.setOnClickListener(this);
        searcheditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (currentfragment.equals("OfferFragment")) {
                    if (offerFragment != null)
                        offerFragment.SearchResult(searcheditText.getText().toString());

                }
               /* View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentfragment.equals("CruiseFragment")) {
                ChangeFragment("OfferFragment", null);
            } else if (currentfragment.equals("OfferFragment")) {
                ChangeFragment("CategoryFragment", null);
            } else if (currentfragment.equals("PaymentFragment")) {
                ChangeFragment("CategoryFragment", null);
            } else if (currentfragment.equals("EventFragment")) {
                ChangeFragment("TicketFragment", null);
            } else if (currentfragment.equals("TicketFragment")) {
                ChangeFragment("CategoryFragment", null);
            } else if (currentfragment.equals("BarcodeFragment")) {
                if (callFromTicket) {

                    ChangeFragment("TicketFragment", null);
                } else {
                    ChangeFragment("CategoryFragment", null);
                }
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  @Override
    public void ChangeFragment(Fragment fragment) {

    }*/

    private Drawable getNavigationIcon(int d) {
        Drawable drawable = getResources().getDrawable(d);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 80, true));


        return newdrawable;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.drawer_ticket) {
            callFromTicket = true;
            ChangeFragment("TicketFragment", null);
        } else if (id == R.id.drawer_setting) {
            ChangeFragment("SettingFragment", null);
        } else if (id == R.id.toolbar_home) {
            ChangeFragment("CategoryFragment", null);
        } else if (id == R.id.drawer_off) {
            drawer.closeDrawers();
        } else if (id == R.id.logout_layout) {
            final SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("login", false);
            editor.apply();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else if (id == R.id.img_search) {
           /* if(!searcheditText.getText().toString().isEmpty()){*/

            if (currentfragment.equals("OfferFragment")) {
                OfferFragment offerFragment = (OfferFragment) fragment;
                offerFragment.SearchResult(searcheditText.getText().toString());

            }
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
           /* }
            else {
                Toast.makeText(this,"Please write something first!",Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public void ChangeFragment(String name, Bundle bundle) {
        this.bundle=bundle;
        currentfragment = name;
        searcheditText.setText("");
        if (name.equals("OfferFragment")) {
            offerFragment = new OfferFragment(this);
            fragment = offerFragment;
            toolbar.setBackgroundColor(getResources().getColor(R.color.colormaroon));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuiconwhite));
            titleLayout.setVisibility(View.VISIBLE);
            tv_title.setText(bundle.getString("category"));

            searchText_layout.setVisibility(View.VISIBLE);

        } else if (name.equals("CategoryFragment")) {
            fragment = new CategoryFragment(this);
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuicongray));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.VISIBLE);

        } else if (name.equals("SettingFragment")) {
            drawer.closeDrawers();
            fragment = new SettingFragment(this);
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuicongray));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

        } else if (name.equals("UpdateUserFragment")) {
            fragment = new UpdateUserFragment(this);
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuicongray));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

        } else if (name.equals("CruiseFragment")) {
            fragment = new CruiseFragment(this);
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colormaroon));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuiconwhite));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

        } else if (name.equals("PaymentFragment")) {
            callFromTicket = false;
            fragment = new PaymentFrament(this);
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorpayment));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.sidebuttonicon));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

        } else if (name.equals("BarcodeFragment")) {
            fragment = new BarCodeFragment();
            fragment.setArguments(bundle);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.sidebuttonicon));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

        } else if (name.equals("EventFragment")) {
            drawer.closeDrawers();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorEvent));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuiconwhite));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);
            fragment = new EventFragment(this);
            fragment.setArguments(bundle);
        } else if (name.equals("TicketFragment")) {
            drawer.closeDrawers();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colormaroon));
            toolbar.setNavigationIcon(getNavigationIcon(R.drawable.menuiconwhite));
            titleLayout.setVisibility(View.INVISIBLE);
            searchText_layout.setVisibility(View.GONE);

            fragment = new TicketFragment(this);
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
/*
        fragmentTransaction.addToBackStack(null);
*/
        fragmentTransaction.commit();
    }


}
