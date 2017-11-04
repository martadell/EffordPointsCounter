package edu.upc.eseiaat.pma.effordpointscounter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CounterActivity extends AppCompatActivity {

    private static final String FILENAME = "string_points.txt";
    private static final int MAX_BYTES = 8000;

    private EditText add_points;
    private TextView efford_points;
    private TextView level_points;
    private TextView total_points;

    //recuperar valors si l'aplicació es tanca
    @Override
    protected void onStop() {
        super.onStop();
        writePoints();
    }

    private void writePoints() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                String epoints = efford_points.getText().toString();
                String lpoints = level_points.getText().toString();
                String tpoints = total_points.getText().toString();
                String points = epoints + ";" + lpoints + ";" + tpoints;
                fos.write(points.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.cannotwrite, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.cannotwrite, Toast.LENGTH_SHORT).show();
        }
    }

    private void readPoints() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread > 0) {
                String content = new String (buffer, 0, nread);
                String[] points = content.split(";"); //separar els punts
                    efford_points.setText(points[0]);
                    level_points.setText(points[1]);
                    total_points.setText(points[2]);
                }
            else {
                efford_points.setText("0");
                level_points.setText("0");
                total_points.setText("0");
            }
            fis.close();

        } catch (FileNotFoundException e) {
            Log.i("e", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.cannotread, Toast.LENGTH_SHORT).show();
        }

       }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        efford_points = (TextView) findViewById(R.id.points_num);
        level_points = (TextView) findViewById(R.id.level_num);
        total_points = (TextView) findViewById(R.id.total_num);
        add_points = (EditText) findViewById(R.id.add_points);

        readPoints();

    }

    public void click_add_points(View view) {
        addPoints();

    }

    //Metode per afegir punts

    private void addPoints() {

        int points = Integer.parseInt(add_points.getText().toString());
        int e_points = Integer.parseInt(efford_points.getText().toString());
        int l_points = Integer.parseInt(level_points.getText().toString());
        int t_points = Integer.parseInt(total_points.getText().toString());

        int suma = points + e_points;

        if (points >= 100) {
            Toast.makeText(this, R.string.error_100, Toast.LENGTH_LONG).show();
            add_points.setText("");
        }

        else {

            int total = t_points + points;

            if (total < t_points) {

                e_points = e_points;
                l_points = l_points;
            }

            else {

                if (suma == 100) {
                  l_points++;
                  e_points = 0;

                 }  else {

                     if (suma > 100) {
                         l_points ++;
                         e_points = suma - 100;

                     } else {
                         e_points = suma;
                     }
                    }
            }

            efford_points.setText(String.valueOf(e_points));
            level_points.setText(String.valueOf(l_points));
            total_points.setText(String.valueOf(total));

            add_points.setText("");

        }
    }


    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.clear_points:
                total_points.setText(String.valueOf(Integer.parseInt(
                        total_points.getText().toString()) - Integer.parseInt(
                        efford_points.getText().toString())));
                efford_points.setText("0");

                return true;

            case R.id.clear_lvl:
                level_points.setText("0");

                return true;

            case R.id.clear_all:
                clearAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Metode netejar-ho tot

    private void clearAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.confirm_clear_all);
        builder.setPositiveButton(R.string.clear_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                efford_points.setText("0");
                level_points.setText("0");
                total_points.setText("0");
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();

    }
}
