package dvdcocoon.markvanderwal.nl.dvdcocoon;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnItemClickListener
{
    private MovieListviewAdapter searchListviewAdapter;
    private DatabaseHelper myDbHelper;

    private TextView searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView searchListview = (ListView)findViewById(R.id.movieListview);
        searchListview.setOnItemClickListener(this);

        searchField = (TextView)findViewById(R.id.searchField);
        searchField.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            OnSearchPressed(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        myDbHelper = new DatabaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int index, long id)
    {
        MovieData movieData = searchListviewAdapter.getItem(index);
        String textToShow = movieData.toString();

        if(!movieData.tag.isEmpty() && movieData.tag != null)
            textToShow = movieData.tag;

        Toast.makeText(this, textToShow, Toast.LENGTH_LONG).show();
    }

    public void OnSearchPressed(View view)
    {
        ListView searchListview = (ListView)findViewById(R.id.movieListview);

        String searchKeyword = searchField.getText().toString();

        if(searchKeyword.isEmpty() || searchKeyword == null)
            return;

        List<MovieData> list = null;

        try {
            list = myDbHelper.GetMoviesLikeName(searchKeyword);
        }
        catch(Exception ex)
        {
            if(list == null)
                list = new ArrayList<MovieData>();

            MovieData data = new MovieData();

            data.name = "Error: Klik hierop voor het error bericht";
            data.tag = ex.getMessage();
            list.add(data);
        }

        if(list.size() <= 0)
        {
            MovieData data = new MovieData();

            data.name = "Bericht: Geen resultaten gevonden!!";
            list.add(data);
        }

        searchListviewAdapter = new MovieListviewAdapter(this, list);
        searchListview.setAdapter(searchListviewAdapter);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
