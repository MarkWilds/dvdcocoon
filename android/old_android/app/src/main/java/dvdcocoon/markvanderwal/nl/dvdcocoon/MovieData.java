package dvdcocoon.markvanderwal.nl.dvdcocoon;

/**
 * Created by Mark on 13-6-2015.
 */
public class MovieData
{
    public String name;
    public String label;
    public String medium;
    public String tag;

    public MovieData()
    {
        name = "";
        label = "";
        medium = "";
        tag = "";
    }

    @Override
    public String toString()
    {
        return label + " - " + medium + " - " + name;
    }
}
