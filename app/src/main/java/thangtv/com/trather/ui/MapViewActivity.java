package thangtv.com.trather.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import thangtv.com.trather.R;
import thangtv.com.trather.controller.PlaceData;
import thangtv.com.trather.controller.RotaTask;

public class MapViewActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView navName;
    private TextView navMail;
    private CircleImageView navAvatar;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        //set up navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.nav_open, R.string.nav_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_reorder_black_18dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }

        // setup navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get user
        ParseUser user = ParseUser.getCurrentUser();

        //connect textviews in the header of navigation drawer
        View header = navigationView.inflateHeaderView(R.layout.nav_header);
        navName = (TextView) header.findViewById(R.id.nav_user_name);
        navMail = (TextView) header.findViewById(R.id.nav_user_email);
        navAvatar = (CircleImageView) header.findViewById(R.id.nav_avatar);
        navName.setText(user.getString("name"));
        navMail.setText(user.getEmail());
        ParseFile file = user.getParseFile("avatar");
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                navAvatar.setImageBitmap(bitmap);
            }
        });
        //Must add header view after change its components
        /*navigationView.addHeaderView(header);*/
        navigationView.setCheckedItem(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_route_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        new RotaTask(this, map, "Thanh Nhan", "Dai hoc Bach Khoa Ha Noi").execute();
    }
    public void bt_map_click(View v){
        switch (v.getId()) {
            case R.id.bt_map_findRoute:
//                Intent intent = new Intent(MapViewActivity.this, ViewRouteActivity.class);
//                startActivity(intent);
                break;
            case R.id.bt_map_myPlaces:
                Location location = PlaceData.getGpsData(this);
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    addMarker(latLng, "Your location", 3000);
                }
                break;
        }
    }
    Marker addMarker(LatLng latLng,String name, int timeMoveCam_ms){
        //        LatLng sydney = new LatLng(-34, 151);
        Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name)
//                .snippet("Kiel is cool")
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.photo))
        );
        if (timeMoveCam_ms>0){
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,14,0,0)));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), timeMoveCam_ms, null);
        }
        return marker;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(MapViewActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.notifications:
                break;
            case R.id.map:
                break;
            case R.id.directions:
                break;
            case R.id.settings:
                break;
            case R.id.log_out:
                ParseUser user = ParseUser.getCurrentUser();
                user.logOut();
                Intent intent1 = new Intent(MapViewActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}