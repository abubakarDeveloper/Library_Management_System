package ab_developer.com.library_management;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class SaerchActivity extends AppCompatActivity {


    Spinner spCategory, spDept;

    String cat;
    ArrayList<Book> bookList;
    RecyclerView rvBooks, rvDept, rvCat;

    TextView tvUser;
    ArrayAdapter<Department> arrayAdapter;
    ArrayList<Department> productList;

    EditText etDept, etCat;
    Button btnSearchDept, btnSearchCat, btnMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saerch);


//spDept = findViewById(R.id.sp_dept);
        rvBooks = findViewById(R.id.rv_books);
        rvDept = findViewById(R.id.rv_dept);
        rvCat = findViewById(R.id.rv_cat);
        tvUser = findViewById(R.id.tv_user);

        etCat = findViewById(R.id.et_cat);
        etDept = findViewById(R.id.et_dept);
        btnSearchCat = findViewById(R.id.btn_search_cat);
        btnSearchDept = findViewById(R.id.btn_search_dept);
        btnMain = findViewById(R.id.btn_main);

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
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaerchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        fetchBookAll();

    }

    public void fetchBookByCat(String cat){
        String url = "http://abubakar.androidstudent.net/test_dir/search_cat.php?book_name=" + cat;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rvBooks.setVisibility(View.GONE);
                rvDept.setVisibility(View.GONE);
                rvCat.setVisibility(View.VISIBLE);
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
  /*                      b.returnDate = productObject.getString("return_date");
                        b.fine = productObject.getString("fine");
                        b.student_registration_no = productObject.getString("student_registration_no");
                        b.return_book = productObject.getString("return_book");
  */                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rvCat.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(bookList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    adapter.notifyDataSetChanged();
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
        String url = "http://abubakar.androidstudent.net/test_dir/book_api.php?book_name=" + dept;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rvBooks.setVisibility(View.GONE);
                rvDept.setVisibility(View.VISIBLE);
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
  /*                      b.returnDate = productObject.getString("return_date");
                        b.fine = productObject.getString("fine");
                        b.student_registration_no = productObject.getString("student_registration_no");
                        b.return_book = productObject.getString("return_book");
  */                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rvDept.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(bookList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    adapter.notifyDataSetChanged();
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
        String url = "http://abubakar.androidstudent.net/test_dir/book.php";

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
  /*                      b.returnDate = productObject.getString("return_date");
                        b.fine = productObject.getString("fine");
                        b.student_registration_no = productObject.getString("student_registration_no");
                        b.return_book = productObject.getString("return_book");
  */                      bookList.add(b);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
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
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }
}
