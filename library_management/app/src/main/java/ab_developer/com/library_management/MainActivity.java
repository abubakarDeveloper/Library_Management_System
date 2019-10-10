package ab_developer.com.library_management;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    Spinner spCategory, spDept;

    String cat;
    String dept;
    ArrayList<Book> bookList;
    RecyclerView rvBooks, rvDept, rvCat;

    TextView tvUser;
    ArrayList<Department> productList;

    EditText etDept, etCat;
    FloatingActionButton btnSearchDept, btnSearchCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*

*/
        spDept = findViewById(R.id.sp_dept);
        spCategory = findViewById(R.id.sp_cat);
  //      rvBooks = findViewById(R.id.rv_books);
        rvDept = findViewById(R.id.rv_dept);
        rvCat = findViewById(R.id.rv_cat);
/*
        etCat = findViewById(R.id.et_cat);
        etDept = findViewById(R.id.et_dept);
        btnSearchCat = findViewById(R.id.fab_cat);
        btnSearchDept = findViewById(R.id.fab_dept);

        btnSearchDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dept = etDept.getText().toString();
                fetchBookByDept(dept);
            }
        });
        btnSearchCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = etCat.getText().toString();
                fetchBookByCat(cat);
            }
        });*/
        updateLogOptions();
        fetchCat();
        fetchDept();
    }

    public void fetchDept(){
        String url = "http://www.skfertilizerpakistan.com/lms/test_dir/dept.php";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("leadStatustag", response);
                    Toast.makeText(getApplicationContext(), "SUCCES", Toast.LENGTH_SHORT).show();
                    JSONArray productsArray = new JSONArray(response);
                    final ArrayList<Department> productList;
                    productList = new ArrayList<>();
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        Department b = new Department();
                        b.dept_id = productObject.getInt("dept_id");
                        b.dept_name = productObject.getString("dept_name");
                        productList.add(b);
                    }
                    Log.i("produt_list", productList.toString());


                    ArrayAdapter<Department> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, productList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDept.setAdapter(arrayAdapter);
                    spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            dept = String.valueOf(productList.get(position).getDept_id());
                            String name = productList.get(position).getDept_name();
          //                  Toast.makeText(parent.getContext(), "Selected: " + name + "id:" + cat, Toast.LENGTH_LONG).show();
                            fetchBookByDept(dept);

                            //String StatusName = String.valueOf(sqlHelper.getLeadId(name));
                            //Log.i("statusname", StatusName);

                            //Toast.makeText(parent.getContext(), "Selected: " + StatusName, Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView <?> parent) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "PArsing error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "VOLLEY EROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }
    public void fetchCat(){
        String url = "http://www.skfertilizerpakistan.com/lms/test_dir/cat.php";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("leadStatustag", response);
                    Toast.makeText(getApplicationContext(), "SUCCES", Toast.LENGTH_SHORT).show();
                    JSONArray productsArray = new JSONArray(response);
                    //final ArrayList<Book> productList;
                    final ArrayList<Category> productList = new ArrayList<>();
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        Category b = new Category();
                        b.cat_id = productObject.getString("cat_id");
                        b.cat_name = productObject.getString("cat_name");
                        productList.add(b);
                    }
                    Log.i("produt_list", productList.toString());

                    ArrayAdapter<Category> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, productList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategory.setAdapter(arrayAdapter);
                    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cat = String.valueOf(productList.get(position).cat_id);
                            String name = productList.get(position).cat_name;
                           // Toast.makeText(parent.getContext(), "Selected: " + name + "id:" + cat, Toast.LENGTH_LONG).show();
                            fetchBookByCat(cat);

                            //String StatusName = String.valueOf(sqlHelper.getLeadId(name));
                            //Log.i("statusname", StatusName);

                            //Toast.makeText(parent.getContext(), "Selected: " + StatusName, Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView <?> parent) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "PArsing error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "VOLLEY EROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }
    public void fetchBookByCat(String cat){
        String url = "http://www.skfertilizerpakistan.com/lms/test_dir/search_cat.php?book_name=" + cat;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("leadStatustag", response);
                    Toast.makeText(getApplicationContext(), "SUCCES", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray productsArray = jsonObject.getJSONArray("extra_list");
                    //final ArrayList<Book> productList;
                    bookList = new ArrayList<>();
                    Book b;
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        b = new Book();
                        b.ID = productObject.getInt("book_id");
                        b.bookName = productObject.getString("book_name");
                        b.author_name = productObject.getString("author_name");
                        b.rake_name = productObject.getString("rake_name");
                        b.dept_name = productObject.getString("dept_name");

                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvCat.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(bookList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    rvCat.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "PArsing error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "VOLLEY EROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }

    public void fetchBookByDept(String dept){
        String url = "http://www.skfertilizerpakistan.com/lms/test_dir/book_api.php?book_name=" + dept;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("leadStatustag", response);
                    Toast.makeText(getApplicationContext(), "SUCCES", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray productsArray = jsonObject.getJSONArray("extra_list");
                    //final ArrayList<Book> productList;
                    bookList = new ArrayList<>();
                    Book b;
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        b = new Book();
                        b.ID = productObject.getInt("book_id");
                        b.bookName = productObject.getString("book_name");
                        b.author_name = productObject.getString("author_name");
                        b.rake_name = productObject.getString("rake_name");
                        b.dept_name = productObject.getString("dept_name");

                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvDept.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(bookList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    rvDept.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "PArsing error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "VOLLEY EROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }
    public void fetchBookAll(){
        String url = "http://www.skfertilizerpakistan.com/lms/test_dir/book.php";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rvBooks.setVisibility(View.VISIBLE);
                rvDept.setVisibility(View.GONE);
                rvCat.setVisibility(View.GONE);

                try {
                    Log.i("leadStatustag", response);
                    Toast.makeText(getApplicationContext(), "SUCCES", Toast.LENGTH_SHORT).show();
                    JSONArray productsArray = new JSONArray(response);
                    //final ArrayList<Book> productList;
                    bookList = new ArrayList<>();
                    Book b;
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        b = new Book();
                        b.ID = productObject.getInt("book_id");
                        b.bookName = productObject.getString("book_name");
                        b.author_name = productObject.getString("author_name");
                        b.rake_name = productObject.getString("rake_name");
                        b.dept_name = productObject.getString("dept_name");
                      b.returnDate = productObject.getString("return_date");
                        b.fine = productObject.getString("fine");
                        b.student_registration_no = productObject.getString("student_registration_no");
                        b.return_book = productObject.getString("return_book");

                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvBooks.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(bookList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                    rvBooks.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "PArsing error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "VOLLEY EROR", Toast.LENGTH_SHORT).show();
            }
        })
        {

        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            SessionHelper.logout(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this,"Logged Out", Toast.LENGTH_SHORT).show();
            updateLogOptions();

        } else if (id == R.id.nav_slideshow) {
            String number = "03088688693";

            if(number.isEmpty()){
                Toast.makeText(getApplicationContext(), "No Number", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Please Provide Permission", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
                    }
                }
                startActivity(intent);
            }

        } /*else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, IssuedBookActivity.class);
            startActivity(intent);

        } *//*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void updateLogOptions(){
        /*MenuItem logoutItem = navigationView.getMenu().findItem(R.id.nav_camera);
        MenuItem loginItem = navigationView.getMenu().findItem(R.id.nav_gallery);

        if (!SessionHelper.isUserLoggedIn(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, EmailActivity.class);
            startActivity(intent);
            finish();
      //      loginItem.setVisible(true);
    //        logoutItem.setVisible(true);
        } else {
//            loginItem.setVisible(false);
  //          logoutItem.setVisible(true);
            try {
                JSONObject userObject = SessionHelper.getCurrentUser(MainActivity.this);
                String name = userObject.getString("student_name");
                String email = userObject.getString("student_email");
                int userId = userObject.getInt("student_id");
                Log.i("UserName", name);
//                tvUser.setText(name);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        updateLogOptions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        invalidateOptionsMenu();
        updateLogOptions();

    }

}
