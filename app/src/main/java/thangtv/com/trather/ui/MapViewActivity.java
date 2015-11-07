package thangtv.com.trather.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import thangtv.com.trather.R;

public class MapViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView navName;
    private TextView navMail;
    private CircleImageView navAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Main screen");
            }
        }

        //set up navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
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