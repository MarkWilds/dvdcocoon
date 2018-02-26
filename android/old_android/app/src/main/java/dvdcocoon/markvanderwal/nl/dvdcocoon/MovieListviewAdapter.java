package dvdcocoon.markvanderwal.nl.dvdcocoon;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Mark on 13-6-2015.
 */
public class MovieListviewAdapter extends ArrayAdapter<MovieData>
{
    private Context context;

    public MovieListviewAdapter(Context context, List<MovieData> data)
    {
        super(context, R.layout.movie_cell, data);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate( R.layout.movie_cell, null);
        }

        int bgColor = context.getResources().getColor(R.color.cellEvenColor);
        if(position % 2 == 0)
            view.setBackgroundColor(bgColor);

        MovieData movieData = getItem(position);
        TextView movieNameTextView = (TextView)view.findViewById(R.id.cellMovieName);
        TextView movieLabelTextView = (TextView)view.findViewById(R.id.cellMovieLabel);
        TextView moveMediumTextView = (TextView)view.findViewById(R.id.cellMovieMedium);

        if(movieNameTextView != null)
            movieNameTextView.setText(movieData.name);

        if(movieLabelTextView != null)
            movieLabelTextView.setText(movieData.label);

        if(moveMediumTextView != null)
            moveMediumTextView.setText(movieData.medium);

        return view;
    }
}
